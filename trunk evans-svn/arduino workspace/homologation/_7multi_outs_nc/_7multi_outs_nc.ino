#include <VirtualWire.h>
#define BUTTON_PIN 2
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define MAX_WORD_LENGTH 40

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin

// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
// state
char* id[7] = {"test1", "test2", "test3", "test4", "test5",  "test6", "test7"};
int pins[7] = {2, 3, 4, 5, 6, 7, 8};
boolean on[7] = {false, false, false, false, false, false, false};

int switchPin = 0;
int led_iterator;
char msg[MAX_WORD_LENGTH];

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    
    // multiple outputs
    for(led_iterator = 0; led_iterator < 7; led_iterator++) {
      pinMode(pins[led_iterator], OUTPUT);
    }   
    
    Serial.begin(9600);
    vw_set_ptt_inverted(true);        // Required for DR3100
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

void switchRelayState() {
//    Serial.print("Switched states for pin:"); Serial.print(pins[switchPin]); Serial.println("");
    if (on[switchPin]) {
      digitalWrite(pins[switchPin], LOW);
      on[switchPin] = false;
    } else {
      digitalWrite(pins[switchPin], HIGH);
      on[switchPin] = true;
    }
    
    sendStateMessage(on[switchPin]);
}

boolean wasMessageReceived() {
  uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
  uint8_t buflen = VW_MAX_MESSAGE_LEN;
  boolean rightString = false;
  
  if (vw_get_message(buf, &buflen)) {
     int i;
     for(led_iterator = 0; led_iterator < 7; led_iterator++) { // iterates for each device
       if (strlen((char*)buf) != strlen(id[led_iterator])) { // if length of received string != length of device string
         rightString = false;
       } else {
         rightString = true; // it is true until something says it's not
         for (i = 0; i < buflen; i++) {
           if ((uint8_t)id[led_iterator][i] != buf[i]) {
             rightString = false; // it is not true when letters are different
           } // if equals
         } // iterates through each letter
       } // if length is equal
       
       if (rightString) { // if the right led was found switchPin is kept
           switchPin = led_iterator;
           break;
       }
    }// iterates through all leds
  } // if message received
    return rightString;
}

void sendStateMessage(boolean state) {
  strcpy(msg, id[switchPin]);
  // id = list of id's, separator character = -, state = ON/OFF, ending character = -
  if (state) {
    strcat(msg, "-ON-_");
  } else {
    strcat(msg, "-OFF-_");
  }
  vw_send((uint8_t *)msg, strlen(msg));
  vw_wait_tx();
}



