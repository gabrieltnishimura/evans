#include <VirtualWire.h>
#define RELAY_PIN 5
#define BUTTON_PIN 12
#define RX_MODULE_PIN 3
#define TOOGLE_SWITCH_PIN 13

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
// switch stuff
int readingSwitch;
int previousSwitch;
// random stuff
long time = 0;         // the last time the output pin was toggled
long debounce = 175;   // the debounce time, increase if the output flickers
// state
boolean on = false;
char *id = "o";

void setup()
{
    pinMode(BUTTON_PIN, INPUT);
    pinMode(TOOGLE_SWITCH_PIN, INPUT);
    previousSwitch = digitalRead(TOOGLE_SWITCH_PIN);
    
    pinMode(RELAY_PIN, OUTPUT);
    digitalWrite(RELAY_PIN, LOW);
    
    vw_setup(2000);	 // Bits per sec
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_rx_start();
}

void loop()
{
   if (wasButtonPressed()) {
     switchRelayState();
   } else if (wasMessageReceived()) {
     switchRelayState();
   } else if (wasSwitchToggled()) {
     switchRelayState();
   }
}

void switchRelayState() {
    if (on) {
      digitalWrite(RELAY_PIN, LOW);
      on = false;
    } else {
      digitalWrite(RELAY_PIN, HIGH);
      on = true;
    }
}

boolean wasMessageReceived() {
  uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
  uint8_t buflen = VW_MAX_MESSAGE_LEN;
  boolean rightString = false;
  
  if (vw_get_message(buf, &buflen)) {
     int i;
     if (strlen((char*)buf) != strlen(id)) {
       rightString = false;
     } else {
       rightString = true;
       for (i = 0; i < strlen((char*)buf)-1; i++) {
         if ((uint8_t)id[i] != buf[i]) {
           rightString = false;
         }
         Serial.println();
       }
     }
  }
  return rightString;
}

boolean wasSwitchToggled() {  
  readingSwitch = digitalRead(TOOGLE_SWITCH_PIN);
  boolean toggled = false;
  
  if (readingSwitch != previousSwitch) {
    toggled = true;
  }
  
previousSwitch = readingSwitch;
return toggled;
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

