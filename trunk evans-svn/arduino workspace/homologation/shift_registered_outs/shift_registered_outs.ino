#include <VirtualWire.h>
#include <stdlib.h>
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define LATCH_PIN 4 // shift register
#define CLOCK_PIN 3 // shift register
#define DATA_PIN 2 // shift register
#define NDEV 4 // eight devices
#define MAX_WORD_LENGTH 40
  
// SEND & RECEIVE -> when is not sending, it turns into a listener
// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin

// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
// state
//char *id[NDEV] = {"test1", "test3", "test2", "test4", "test5", "test6", "test7", "test8"};
char *id_off[NDEV] = {"a", "b", "c", "d"};
char *id_on[NDEV] = {"A", "B", "C", "D"};
//boolean on[NDEV] = {false, false, false, false, false, false, false, false};
boolean on[NDEV] = {false, false, false, false};

int switchPin = 0; // the pin the output is supposed to switch states
int led_iterator; // just a counter
char msg[MAX_WORD_LENGTH]; // for sending messages

void setup()
{
    //Serial.begin(9600); // only for @locus debug
    
    //set pins to output so you can control the shift register
    pinMode(LATCH_PIN, OUTPUT);
    pinMode(CLOCK_PIN, OUTPUT);
    pinMode(DATA_PIN, OUTPUT);
    
    outputToShiftRegister(); // init shift register
    
    vw_setup(2000);	 // Bits per sec
    vw_set_ptt_inverted(true);        // Required for DR3100
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_set_tx_pin(TX_MODULE_PIN);
    vw_rx_start(); // starts as listener
}

void loop()
{ 
   if (wasMessageReceived()) {
     switchRelayState();
   }
}

void switchRelayState() {
	// switching pin state so shift register function can work
    if (on[switchPin]) {
        on[switchPin] = false;
    } else {
        on[switchPin] = true;
    }
    
    outputToShiftRegister();
    sendStateMessage(on[switchPin]);
}

boolean wasMessageReceived() {
  uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
  uint8_t buflen = VW_MAX_MESSAGE_LEN;
  boolean rightString = false;
  
    if (vw_get_message(buf, &buflen)) {
        int i;
        for(led_iterator = 0; led_iterator < NDEV; led_iterator++) {
            switchPin = led_iterator;
            if (strlen((char*)buf) != strlen(id[led_iterator])) {
                rightString = false;
            } else {
                rightString = true;
                for (i = 0; i < strlen((char*)buf); i++) {
                    if ((uint8_t)id[led_iterator][i] != buf[i]) {
                        rightString = false;
                    } // if equals
                } // iterates through each letter
                if (rightString) {
                    break;
                }
            } // if length is equal
        if (rightString)
            break;
        } // iterates through all leds
    } // if message received
  return rightString;
}

/**
 * Output the changed states to the shift register at pins 
 * LATCH_PIN, DATA_PIN, CLOCK_PIN
 */
void outputToShiftRegister() {
    digitalWrite(LATCH_PIN, LOW); // so OUTPUT don't change while you're sending in bits
    shiftOut(DATA_PIN, CLOCK_PIN, MSBFIRST, shiftRegisterCode());  // shift out the bits
    digitalWrite(LATCH_PIN, HIGH); // change last state to current state
}

/**
 * This function generates the shift register code, 
 * one which the outputToShiftRegister code will use
 * @return shiftRegisterCode
 */
int shiftRegisterCode() {
    int val = 0;
        for (led_iterator = NDEV - 1; led_iterator >= 0; led_iterator--) {
            if (on[led_iterator])
                val += (1 << (led_iterator)); // bitshift operator
            else
                val += (0 << (led_iterator)); // bitshift operator
    }
    return val;
}

void sendStateMessage(boolean state) {
	vw_rx_stop(); // stop listening so that it can send messages
	
	strcpy(msg, id[switchPin]);
	// id = list of id's, separator character = -, state = ON/OFF, ending character = -
	if (state) 
	    strcat(msg, "-ON-_");
	else
	    strcat(msg, "-OFF-_");

	vw_send((uint8_t *)msg, strlen(msg)); // send the message
	vw_wait_tx(); // wait until all message is gone

	vw_rx_start(); // start listening so that it can receive messages
}
