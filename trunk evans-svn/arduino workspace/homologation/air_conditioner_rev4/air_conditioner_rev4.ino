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
#define SPEED_MODE 10

#define CHECKSUM 15

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
int turnOff;

char on = 'J';
char off = 'j';
boolean tryToComposeMessage = false;

int message[16][8] = {
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

int messageOff[7][8] = {
{1, 1, 0, 1, 0, 1, 1, 1}, // marker code M1
{0, 0, 1, 1, 1, 0, 0, 1}, // marker code M2
{1, 1, 1, 1, 1, 1, 1, 1}, // marker code Parity P
{1, 1, 1, 1, 0, 1, 1, 1}, // custom code C1
{1, 1, 1, 1, 0, 1, 1, 1}, // sub custom code C2
{1, 0, 1, 1, 1, 1, 1, 1}, // off command
{0, 1, 0, 0, 0, 0, 0, 0} // checksum
};

void setup()
{
    Serial.begin(9600);
    Serial.print("Starting up");
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
        if (turnOff == 1) {
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
void IRsendCode(int message[16][8])
{
  //send the leading pulse
  IRcarrier(3340);            //3.2ms of carrier
  delayMicroseconds(1570);    //1.5ms of silence
  
  //send the user defined 4 byte/32bit code
  for (int m = 0; m < 16; m++) {
        for (int i = 0; i < 8; i++) {
            IRcarrier(BITtime);                     //turn on the carrier for one bit time
            if (message[m][i] == 1)        //get the current bit
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
            Serial.print((char*)buf);
            Serial.print(",");
            Serial.println(strlen((char*)buf));
            if (strlen((char*)buf) == 1 && buf[0] == off) {
                turnOff = 1;
                rightString = true;
            } else if (strlen((char*)buf) == 4 && buf[0] == on) {
                turnOff = 0;
                if (tryToComposeMessage) {
                    populateTemperatureArray(buf[1]);
                    populateFanMode(buf[2]);
                    populateSpeedMode(buf[3]);
                }
                rightString = true;
            }
	}
	return rightString;
}

// {0, 1, 1, 1, 0, 1, 0, 1}, // chart A - TEMP=B4-B7, B0=wasON? 1 yes 0 no
void populateTemperatureArray(char code) {
      for (int i = 4; i < 8; i++) {
          message[TEMPERATURE][i] = bitRead(code, i - 4) == 1 ? 0 : 1;
      }
}

void populateFanMode(char code) {
      long converted = hex2int(code);
      for (int i = 0; i < 3; i++) {
          message[FAN_MODE][i] = bitRead(converted, i) == 1 ? 0 : 1;
      }
}

void populateSpeedMode(char code) {
      long converted = hex2int(code);
      for (int i = 0; i < 3; i++) {
          message[SPEED_MODE][i] = bitRead(converted, i) == 1 ? 0 : 1;
      }
}

unsigned long hex2int(char a)
{
    unsigned long val = 0;

       if(a <= 57)
        val += (a-48)*(1<<(4));
       else
        val += (a-55)*(1<<(4));
    return val;
}
