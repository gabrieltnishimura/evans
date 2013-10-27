//*****************************************
// NEC (Japanese) Infrared code sending library for the Arduino
// Send a standard NEC 4 byte protocol direct to an IR LED on the define pin
// Assumes an IR LED connected on I/O pin to ground, or equivalent driver.
// Tested on a Freetronics Eleven Uno compatible
// Written by David L. Jones www.eevblog.com
// Youtube video explaining this code: http://www.youtube.com/watch?v=BUvFGTxZBG8
// License: Creative Commons CC BY
//*****************************************
#define BUTTON_PIN 2
#define IRLEDpin  8              //the arduino pin connected to IR LED to ground. HIGH=LED ON
#define BITtime   416            //length of the carrier bit in microseconds
#define Led 5
#define BITOnTIME 390
#define BITOffTIME 1200

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers

/*byte message[16] = {
B00101000, B11000110, B00000000, // m1, m2, parity
B00001000, B00001000, B01111111, // c1, c2, D
B10010000, B00001100, B01000000, // 0x09, 0x30, chart A
B00000000, B00000000, B00100000, // chart c, chart b && e, chart d
B00000000, B00000000, B00000100, // timer, timer, 0x20
B00101000 // w8 + w16
};
*/
/*byte message[16] = {
B11010111, B00111001, B11111111,
B11110111, B11110111, B10000000, 
B01101111, B11110011, B01110101,
B01111111, B00111111, B11111111,
B11111111, B11111111, B11111011,
B00100101
};*/

int message[16][8] = {
{1, 1, 0, 1, 0, 1, 1, 1}, // marker code M1
{0, 0, 1, 1, 1, 0, 0, 1}, // marker code M2
{1, 1, 1, 1, 1, 1, 1, 1}, // marker code Parity P
{1, 1, 1, 1, 0, 1, 1, 1}, // custom code C1
{1, 1, 1, 1, 0, 1, 1, 1}, // sub custom code C2
{1, 0, 0, 0, 0, 0, 0, 0}, // command code D
{0, 1, 1, 0, 1, 1, 1, 1}, // 0x09
{1, 1, 1, 1, 0, 0, 1, 1}, // 0x30

//{1, 0, 1, 1, 1, 1, 1, 1}, // chart A - TEMP=B4-B7, B0=
{0, 1, 1, 1, 0, 1, 0, 1}, // chart A - TEMP=B4-B7, B0=wasON? 1 yes 0 no

{1, 1, 1, 1, 1, 1, 1, 1}, // chart C -
{1, 1, 1, 1, 1, 1, 1, 1}, // chart B && E

//{1, 1, 0, 1, 1, 1, 1, 1}, // chart D - FAN
{1, 1, 1, 1, 1, 1, 1, 1}, // chart D - FAN

{1, 1, 1, 1, 1, 1, 1, 1}, // timer off value
{1, 1, 1, 1, 1, 1, 1, 1}, // timer on value
{1, 1, 1, 1, 1, 0, 1, 1}, // 0x20

//{1, 1, 0, 1, 0, 1, 1, 1} // w8 + w16 = XX00H
{0, 0, 0, 0, 0, 1, 0, 1} // w8 + w16 = XX00H
};


void setup()
{
    IRsetup();                          //Only need to call this once to setup
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

//Sends the IR code in 4 byte NEC format
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

void loop()                           //some demo main code
{
  if(wasButtonPressed()) {
      digitalWrite(Led, HIGH);
      IRsendCode(message); 
      digitalWrite(Led, LOW);
   }
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
