#include <VirtualWire.h>
#define BUTTON_PIN 2
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define IF_PIN 8

// ----- FOR TWEAKING PURPOSES ----- 
#define CARRIER_PERIOD 26.3
#define LEADER_ON 125
#define LEADER_OFF 62
#define TRAILER_ON 16
#define TRAILER_OFF 46
#define ONE_ON 16
#define ONE_OFF 16
#define ZERO_ON 16
#define ZERO_OFF 46
// ---------------------------------

#define MESSAGE_LENGHT 1
#define WORDS_SIZE 16
#define BITS_SIZE 8

/** Air Conditioner R0 
 * Code contains 16 words, each word has 8bits that follow the rules: 
 * Word 1	: Marker Code 		(M1) = 0x14 
 * Word 2	: Marker Code 		(M2) = 0x63 
 * Word 3	: Marker Code Parity (P) = 0x00
 * Word 4	: Custom Code		(C1) = 0x10
 * Word 5	: Sub Custom Code 	(C2) = 0x10 
 * Word 6	: Command Code		 (D) = 0xFE
 * Word 7	: Unknown			 	 = 0x09
 * Word 8	: Unknown				 = 0x30
 * Word 9	: B4 - B7 =  Temp		 = Chart A
			  B0 - 1 now is ON, 0 was already ON
 * A { LSB = 0 if change in temp 
 *           1 if unit coming on top nibble is temp
 * 	66deg ~ 88 deg => 02H ~ 0EH (B4 - B7)
 * }
 * Word 10	: B4 - B7 = Timer Mode
	          B0 - B3 = Master Control
                  B4 - B7 : Timer Off, Sleep Mode, Timer Off Time, Timer On Time, Off->On => 00H, 01H, 02H, 03H, 04H
                  B0 - B3 : Auto, Cool, Dry, Fan
			  00H,  01H,  02H, 03H
 * Word 11	: 
	B4 - B7 : Off, Vertical Oscilation, Horizontal Oscilation, Both => 
	B0 - B3 : Auto, High, Med, Low, Quiet
 * Word 12	:
 * Word 13	:
 * Word 14	:
 * Word 15	:
 * Word 16	:
 * Structure: Function that outputs to led pin, and function that creates an coded array 
 */
 

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers

int message[WORDS_SIZE][BITS_SIZE] = {
{0, 0, 1, 0, 1, 0, 0, 0}, // marker code M1
{1, 1, 0, 0, 0, 1, 1, 0}, // marker code M2
{0, 0, 0, 0, 0, 0, 0, 0}, // marker code Parity P
{0, 0, 0, 0, 1, 0, 0, 0}, // custom code C1
{0, 0, 0, 0, 1, 0, 0, 0}, // sub custom code C2
{0, 1, 1, 1, 1, 1, 1, 1}, // command code D
{1, 0, 0, 1, 0, 0, 0, 0}, // 0x09
{0, 0, 0, 0, 1, 1, 0, 0}, // 0x30
{0, 1, 0, 0, 0, 0, 0, 0}, // chart A - TEMP=B4-B7, B0=
{0, 0, 0, 0, 0, 0, 0, 0}, // chart C -
{0, 0, 0, 0, 0, 0, 0, 0}, // chart B && E
{0, 0, 1, 0, 0, 0, 0, 0}, // chart D - FAN
{0, 0, 0, 0, 0, 0, 0, 0}, // timer off value
{0, 0, 0, 0, 0, 0, 0, 0}, // timer on value
{0, 0, 0, 0, 0, 1, 0, 0}, // 0x20
{0, 0, 1, 0, 1, 0, 0, 0} // w8 + w16 = XX00H
};


int message[WORDS_SIZE][BITS_SIZE] = {
{1, 1, 0, 1, 0, 1, 1, 1}, // marker code M1
{0, 0, 1, 1, 1, 0, 0, 1}, // marker code M2
{1, 1, 1, 1, 1, 1, 1, 1}, // marker code Parity P
{1, 1, 1, 1, 0, 1, 1, 1}, // custom code C1
{1, 1, 1, 1, 0, 1, 1, 1}, // sub custom code C2
{1, 0, 0, 0, 0, 0, 0, 0}, // command code D
{0, 1, 1, 0, 1, 1, 1, 1}, // 0x09
{1, 1, 1, 1, 0, 0, 1, 1}, // 0x30
{1, 0, 1, 1, 1, 1, 1, 1}, // chart A - TEMP=B4-B7, B0=
{1, 1, 1, 1, 1, 1, 1, 1}, // chart C -
{1, 1, 1, 1, 1, 1, 1, 1}, // chart B && E
{1, 1, 0, 1, 1, 1, 1, 1}, // chart D - FAN
{1, 1, 1, 1, 1, 1, 1, 1}, // timer off value
{1, 1, 1, 1, 1, 1, 1, 1}, // timer on value
{1, 1, 1, 1, 1, 0, 1, 1}, // 0x20
{1, 1, 0, 1, 0, 1, 1, 1} // w8 + w16 = XX00H
};

boolean debug = false; // set true to debug message comm
int wordIterator, bitIterator;

void setup()
{
  Serial.begin(9600);
    pinMode(BUTTON_PIN, INPUT);
    pinMode(IF_PIN, OUTPUT); // infrared pin
    pinMode(5, OUTPUT);
    vw_setup(2000);	 // Bits per sec
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_set_tx_pin(TX_MODULE_PIN);
    vw_rx_start();
}

void loop()
{
  
        if (wasButtonPressed()) {
                digitalWrite(5, HIGH);
                sendIFMessage();
                digitalWrite(5, LOW);
        }
        if (wasMessageReceived()) {
                digitalWrite(5, HIGH);
                sendIFMessage();
                digitalWrite(5, LOW);
	}
}

void sendIFMessage() {
        sendLeader();
            for(wordIterator = 0; wordIterator < WORDS_SIZE; wordIterator++){
                for(bitIterator = 0; bitIterator < BITS_SIZE; bitIterator++) {
                    if (message[wordIterator][bitIterator] == 1) {
                        sendOne();
                    } else {
                        sendZero();
                    }
                }
            }  
        sendTrailer();
}


void sendLeader() {
	digitalWrite(IF_PIN, HIGH);
	delayMicroseconds(LEADER_ON * CARRIER_PERIOD);
	digitalWrite(IF_PIN, LOW);
	delayMicroseconds(LEADER_OFF * CARRIER_PERIOD);
}

void sendOne() {
	digitalWrite(IF_PIN, HIGH);
	delayMicroseconds(ONE_ON * CARRIER_PERIOD);
	digitalWrite(IF_PIN, LOW);
	delayMicroseconds(ONE_OFF * CARRIER_PERIOD);
}

void sendZero() {
	digitalWrite(IF_PIN, HIGH);
	delayMicroseconds(ZERO_ON * CARRIER_PERIOD);
	digitalWrite(IF_PIN, LOW);
	delayMicroseconds(ZERO_OFF * CARRIER_PERIOD);
}

void sendTrailer() {
	digitalWrite(IF_PIN, HIGH);
	delayMicroseconds(TRAILER_ON * CARRIER_PERIOD);
	digitalWrite(IF_PIN, LOW);
	delayMicroseconds(TRAILER_OFF * CARRIER_PERIOD);
}

boolean wasMessageReceived()
{
	uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
	uint8_t buflen = VW_MAX_MESSAGE_LEN;
	boolean rightString = false;
  
	if (vw_get_message(buf, &buflen)) {
            if (strlen((char*)buf) != MESSAGE_LENGHT) { // received message length must be MESSAGE_LENGHT
                rightString = false;
	    } else {
	        rightString = true;
                // check message integrity
	    } 

	}
	return rightString;
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

int dectobin(int decimalNum, int * binaryArray) {
int zeros = 8 - String(decimalNum,BIN).length();
String myStr;
for (int i=0; i<zeros; i++) {
  myStr = myStr + "0";
}
myStr = myStr + String(decimalNum,BIN);         
Serial.println(myStr);
}
