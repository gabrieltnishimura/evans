#include <VirtualWire.h>
#define BUTTON_PIN 2
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define NDEV 4

/** R5 uses somewhat a new concept. 
 * As the mainframe is now sending messages telling the appliance to switch whether 
 * on or off, implementation of a new  message protocol had to be designed. 
 * In R5, major changes to wasMessageReceived, modifications in the loop structure and 
 * added message feedback. As now it should be a half-duplex communication. The problem is
 * that different frequencies need to be used in order to prevent interference of rf 
 * sending and receiving waves. Either way, tests needs to be done. Maybe adding a 
 * little delay before I send the feedback message can workaround destructive interference. 
 * Half-duplex communication tested and working unstable, need to optimize code. */

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin

// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers

// state
char *id_off[NDEV] = {"a", "c", "e", "g"};
char *feedback[NDEV*2] = {"B", "b", "D", "d", "F", "f", "H", "h"};
char *id_on[NDEV] = {"A", "C", "E", "G"};
int pins[NDEV] = {2, 3, 4, 5};

boolean on[NDEV] = {false, false, false, false};
int switchPin = 0;
int led_iterator;
char msg[1]; // for sending messages

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    
    // multiple outputs - iterates through all NDEVices
    for(led_iterator = 0; led_iterator < NDEV; led_iterator++) {
      pinMode(pins[led_iterator], OUTPUT);
      digitalWrite(pins[switchPin], LOW);
    }

    vw_setup(2000);	 // Bits per sec
    vw_set_ptt_inverted(true);        // Required for DR3100
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_set_tx_pin(TX_MODULE_PIN);
    vw_rx_start();
}

void loop()
{
	if (wasMessageReceived()) {
		switchRelayState();
	}
}

void switchRelayState()
{
    if (on[switchPin]) {
      digitalWrite(pins[switchPin], LOW);
      on[switchPin] = false;
    } else {
      digitalWrite(pins[switchPin], HIGH);
      on[switchPin] = true;
    }
    delay(300);
    sendStateMessage(on[switchPin]); // since it's changed states already, why not report to the mainframe
}

boolean wasMessageReceived()
{
	uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
	uint8_t buflen = VW_MAX_MESSAGE_LEN;
	boolean rightString = false;
  
	if (vw_get_message(buf, &buflen)) {
		for(led_iterator = 0; led_iterator < NDEV; led_iterator++) {
			switchPin = led_iterator;
			if (strlen((char*)buf) != strlen(id_on[led_iterator])) {
			    rightString = false;
			} else {
		            rightString = true;
                            int i;
                            for (i = 0; i < strlen((char*)buf); i++) {
				if (on[led_iterator]) { // if is on, see if it commands to switch off
					if ((uint8_t)id_off[led_iterator][i] != buf[i]) {
						rightString = false;
					} // if equals
				} else { // if is off, see if it commands to switch on
					if ((uint8_t)id_on[led_iterator][i] != buf[i]) {
						rightString = false;
					} // if equals
				}
                            }
                            if (rightString) {
			        break;
		            }
			} // if length is equal
			 
			if (rightString) {
				break;
			}
		}// iterates through all leds
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

void sendStateMessage(boolean state)
{
  	vw_rx_stop(); // stop listening so that it can send messages

	if (state) {
		strcpy(msg, feedback[switchPin]); // sends the next char
	} else {
		strcpy(msg, feedback[switchPin + 1]);
	}
	vw_send((uint8_t *)msg, strlen(msg)); // send the message
	vw_wait_tx(); // wait until all message is gone

	vw_rx_start(); // start listening so that it can receive messages
        vw_wait_rx();
}
