#include <VirtualWire.h>
#define RELAY_PIN 3
#define BUTTON_PIN 2
#define RX_MODULE_PIN 11
#define TX_PIN 13
#define MAX_WORD_LENGTH 100

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin

// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
// state
char *id[5] = {"test1", "test2", "test3", "test4", "test5"};
int pins[5] = {2, 3, 4, 5, 6};

boolean on[5] = {false, false, false, false, false};
int switchPin = 0;
int led_iterator;
char msg[MAX_WORD_LENGTH];

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    pinMode(RELAY_PIN, OUTPUT);
    
    // multiple outputs
    for(led_iterator = 0; led_iterator <= 4; led_iterator++) {
      pinMode(pins[led_iterator], OUTPUT);
    }   
    
    digitalWrite(RELAY_PIN, LOW);
    Serial.begin(9600);
    vw_setup(2000);	 // Bits per sec
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_rx_start();
    
    vw_set_ptt_inverted(true);        // Required for DR3100
    vw_set_tx_pin(TX_PIN);
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
}

boolean wasMessageReceived() {
  uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
  uint8_t buflen = VW_MAX_MESSAGE_LEN;
  boolean rightString = false;
  
  if (vw_get_message(buf, &buflen)) {

     int i;
     for(led_iterator = 0; led_iterator <= 4; led_iterator++) {
       switchPin = led_iterator;
//    Serial.print("For led: ");Serial.println(led_iterator);
//    Serial.print("Received: '");Serial.print((char*)buf);Serial.print("'");
//    Serial.print("--------------------");
//    Serial.print("Length: '");Serial.print(strlen((char*)buf));;Serial.print("'");
//    Serial.print("Length: '");Serial.print(strlen(id[led_iterator]));;Serial.print("'");
//    Serial.print("Compare with: '");Serial.print((char*)id[led_iterator]);;Serial.print("'");
//    Serial.println("");
       if (strlen((char*)buf) != strlen(id[led_iterator])) {
         rightString = false;
       } else {
         rightString = true;
           for (i = 0; i < strlen((char*)buf); i++) {
//            Serial.print((uint8_t)id[led_iterator][i]);
//            Serial.print("vs");Serial.print(buf[i]);
//            Serial.println("");
             if ((uint8_t)id[led_iterator][i] != buf[i]) {
               rightString = false;
             } // if equals
           } // iterates through each letter
           if (rightString) {
             break;
           }
         } // if length is equal
         
         if (rightString) {
             break;
         }
       }// iterates through all leds
  }
  
  if (rightString) {
    id[led_iterator][i]
    strcpy(msg, )
    vw_send((uint8_t *)msg, strlen(msg));      //Send the message
    vw_wait_tx();                              // Wait until the whole message is gone
  }
  return rightString;
}

boolean wasButtonPressed() {
  readingButton = digitalRead(BUTTON_PIN);
  boolean pressed = false;
  
  if (readingButton == HIGH && previousButton == LOW && millis() - time > debounce) {
    time = millis();
    pressed = true;
  }
  
  previousButton = readingButton;
  return pressed;
}
