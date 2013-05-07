#include <VirtualWire.h>
#define BUTTON_PIN 2
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define NDEV 4

/** R5 uses somewhat a new concept. 
 * As the mainframe is now sending messages 
 * telling the appliance to switch whether 
 * on or off, implementation of a new 
 * message identifier had to be done. 
 * In R5, major changes to wasMessageReceived 
 * and the loop structure was modified a bit as
 * well. */


// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin

// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
// state
char *id_off[NDEV] = {"a", "b", "c", "d"};
char *id_on[NDEV] = {"A", "B", "C", "D"};
int pins[NDEV] = {2, 3, 4, 5};

boolean on[NDEV] = {false, false, false, false};
int switchPin = 0;
int led_iterator;

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    
    // multiple outputs
    for(led_iterator = 0; led_iterator < NDEV; led_iterator++) {
      pinMode(pins[led_iterator], OUTPUT);
    }   
    
    vw_setup(2000);	 // Bits per sec
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
}

boolean wasMessageReceived()
{
	uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
	uint8_t buflen = VW_MAX_MESSAGE_LEN;
	boolean rightString = false;
  
	if (vw_get_message(buf, &buflen)) {
		for(led_iterator = 0; led_iterator < NDEV; led_iterator++) {
			switchPin = led_iterator;
			if (strlen((char*)buf) != 1) { // received message length must be 1
			 rightString = false;
			} else {
				rightString = true;
				if (on[led_iterator]) { // if is on, see if it commands to switch off
					if ((uint8_t)id_off[led_iterator][0] != buf[0]) {
						rightString = false;
					} // if equals
				} else { // if is off, see if it commands to switch on
					if ((uint8_t)id_on[led_iterator][0] != buf[0]) {
						rightString = false;
					} // if equals
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
