//*****************************************
// NEC (Japanese) Infrared code sending library for the Arduino
// Send a standard NEC 4 byte protocol direct to an IR LED on the define pin
// Assumes an IR LED connected on I/O pin to ground, or equivalent driver.
// Tested on a Freetronics Eleven Uno compatible
// License: Creative Commons CC BY
//*****************************************
#include <VirtualWire.h>
#define IRLEDpin  2              //the arduino pin connected to IR LED to ground. HIGH=LED ON
#define BITtime   416            //length of the carrier bit in microseconds
#define Led 5
#define BITOnTIME 390
#define BITOffTIME 1200

#define BUTTON_PIN 12
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13

#define TEMPERATURE 8
#define FAN_MODE 9
#define FAN_SPEED 10
#define CHECKSUM 15

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
boolean turnOff;
byte sumTillNow;

char on = 'J';
char off = 'j';

byte message[16][8] = {
    {1, 1, 0, 1, 0, 1, 1, 1}, // marker code M1
    {0, 0, 1, 1, 1, 0, 0, 1}, // marker code M2
    {1, 1, 1, 1, 1, 1, 1, 1}, // marker code Parity P
    {1, 1, 1, 1, 0, 1, 1, 1}, // custom code C1
    {1, 1, 1, 1, 0, 1, 1, 1}, // sub custom code C2
    {1, 0, 0, 0, 0, 0, 0, 0}, // command code D
    {0, 1, 1, 0, 1, 1, 1, 1}, // 0x09
    {1, 1, 1, 1, 0, 0, 1, 1}, // 0x30
    
    // word 8
    {0, 1, 1, 1, 0, 1, 0, 1}, // chart A - TEMP=B4-B7, B0=wasON? 1 yes 0 no
    
    {1, 1, 1, 1, 1, 1, 1, 1}, // chart C -
    {1, 1, 1, 1, 1, 1, 1, 1}, // chart B && E
    
    //{1, 1, 0, 1, 1, 1, 1, 1}, // chart D - FAN
    {1, 1, 1, 1, 1, 1, 1, 1}, // chart D - FAN
    
    {1, 1, 1, 1, 1, 1, 1, 1}, // timer off value
    {1, 1, 1, 1, 1, 1, 1, 1}, // timer on value
    {1, 1, 1, 1, 1, 0, 1, 1}, // 0x20
    
    //{1, 1, 0, 1, 0, 1, 1, 1} // w8 + w16 = XX00H
    {0, 0, 0, 0, 0, 1, 0, 1} // w8 ~ 16 = XX00H
};

byte messageOff[7][8] = {
    {1, 1, 0, 1, 0, 1, 1, 1}, // marker code M1
    {0, 0, 1, 1, 1, 0, 0, 1}, // marker code M2
    {1, 1, 1, 1, 1, 1, 1, 1}, // marker code Parity P
    {1, 1, 1, 1, 0, 1, 1, 1}, // custom code C1
    {1, 1, 1, 1, 0, 1, 1, 1}, // sub custom code C2
    {1, 0, 1, 1, 1, 1, 1, 1}, // off command
    {0, 1, 0, 0, 0, 0, 0, 0} // checksum
};
boolean once = false;

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    vw_setup(2000);	 // Bits per sec
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_set_tx_pin(TX_MODULE_PIN);
    vw_rx_start();
    IRsetup();                          //Only need to call this once to setup
}

void loop()                           //some demo main code
{
    if(wasButtonPressed()) {
        digitalWrite(Led, HIGH);
        IRsendCode(message); 
        digitalWrite(Led, LOW);
    } else if (wasMessageReceived()) {
        digitalWrite(Led, HIGH);
        if (turnOff) {
            IRsendCode(messageOff);
        } else {
            IRsendCode(message);        
        }
        digitalWrite(Led, LOW);
    }
}

void IRsetup(void)
{
  pinMode(IRLEDpin, OUTPUT);
  pinMode(Led, OUTPUT);
  digitalWrite(IRLEDpin, LOW);    //turn off IR LED to start
}

// Ouput the 38KHz carrier frequency for the required time in microseconds
// This is timing critial and just do-able on an Arduino using the standard I/O functions.
// If you are using interrupts, ensure they disabled for the duration.
void IRcarrier(unsigned int IRtimemicroseconds)
{
  for(int i=0; i < (IRtimemicroseconds / 26); i++)
    {
    digitalWrite(IRLEDpin, HIGH);   //turn on the IR LED
    //NOTE: digitalWrite takes about 3.5us to execute, so we need to factor that into the timing.
    delayMicroseconds(9);          //delay for 13us (9us + digitalWrite), half the carrier frequnecy
    digitalWrite(IRLEDpin, LOW);    //turn off the IR LED
    delayMicroseconds(9);          //delay for 13us (9us + digitalWrite), half the carrier frequnecy
    }
}

//Sends the IR code in 16 byte NEC format
void IRsendCode(byte message[16][8])
{
  //send the leading pulse
  IRcarrier(3340);            //3.2ms of carrier
  delayMicroseconds(1570);    //1.5ms of silence
  
  //send the user defined 4 byte/32bit code
  for (int m = 0; m < 16; m++) {
        for (int i = 0; i < 8; i++) {
            IRcarrier(BITtime);                     //turn on the carrier for one bit time
            if (message[m][i] == 1)                 //not inverted bit
              delayMicroseconds(BITOnTIME);        //a LOW is only 1 bit time period
            else
              delayMicroseconds(BITOffTIME);    //a HIGH is 3 bit time periods
        }
  }
  
  IRcarrier(BITOnTIME+40);                 //send a single STOP bit.
}

boolean wasButtonPressed()
{
	readingButton = digitalRead(BUTTON_PIN);
	boolean pressed = false;
  
	if (readingButton == HIGH && previousButton == LOW && millis() - time > debounce) {
		time = millis();
		pressed = true;
	}
  
	previousButton = readingButton;
	return pressed;
}

boolean wasMessageReceived()
{
	uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
	uint8_t buflen = VW_MAX_MESSAGE_LEN;
	boolean rightString = false;
	if (vw_get_message(buf, &buflen)) {
            if (strlen((char*)buf) == 1 && buf[0] == off) {
                turnOff = true;
                rightString = true;
            } else if (strlen((char*)buf) == 4 && buf[0] == on) {
                turnOff = false;
                sumTillNow = 0x50;             
                populateTemperatureArray(buf[1]);
                populateFanMode(buf[2]);
                populateFanSpeed(buf[3]);
                populateChecksum();
                rightString = true;
            }
	}
	return rightString;
}

void populateTemperatureArray(char code) {
      int converted = hex2int(code);
      converted *= 16; // shift right 4 bits

      sumTillNow += converted;
      
      for (int i = 4; i < 8; i++) {
          message[TEMPERATURE][i] = bitRead(converted, i) == 1 ? 0 : 1;
      }
}

void populateFanMode(char code) {
      int converted = hex2int(code);
      sumTillNow += converted;
      
      for (int i = 0; i < 4; i++) {
          message[FAN_MODE][i] = bitRead(converted, i) == 1 ? 0 : 1;
      }
}

void populateFanSpeed(char code) {
      int converted = hex2int(code);
      sumTillNow += converted;
      
      for (int i = 0; i < 4; i++) {
          message[FAN_SPEED][i] = bitRead(converted, i) == 1 ? 0 : 1;
      }
}

void populateChecksum() {
    for (int i = 0; i < 8; i++) {
        message[CHECKSUM][i] = bitRead(sumTillNow, i);
    }
}


int hex2int(char c) {
    int cInt = c - '0';
    if (cInt >= 0 && cInt <= 9) {
        return cInt;
    } else {
        c = toupper(c);
        if (c == 'A') {return 10;} 
        else if (c == 'B') {return 11;} 
        else if (c == 'C') {return 12;} 
        else if (c == 'D') {return 13;} 
        else if (c == 'E') {return 14;} 
        else if (c == 'F') {return 15;}
    }
}