/**
 *
 */
#include <VirtualWire.h>
#define BUTTON_PIN 12
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define MAX_WORD_LENGTH 40

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
long time = 0;               // the last time the output pin was toggled
long debounce = 200;         // the debounce time, increase if the output flickers
int state = HIGH;            // the current state of the output pin

char myChar;
char curChar;
boolean endedSerial = false;
String roomCode;
String msgReceived;
char msg[MAX_WORD_LENGTH];

void setup() {
  Serial.begin(9600);
  pinMode(TX_MODULE_PIN, OUTPUT);
  pinMode(BUTTON_PIN, INPUT);

  vw_set_ptt_inverted(true);        // Required for DR3100
  vw_setup(2000);                   // Bits per sec
  vw_set_tx_pin(TX_MODULE_PIN);
  vw_set_rx_pin(RX_MODULE_PIN);
  vw_rx_start();
}

void loop() {
  if (wasMessageReceived()) {
    Serial.print("test");
  }
  
  if (wasButtonPressed()) {
    strcpy(msg,"test2");
    vw_send((uint8_t *)msg, strlen(msg));      //Send the message
    vw_wait_tx();                              // Wait until the whole message is gone
  }

  if (Serial.available() > 0) {    //processing incoming data
    myChar = Serial.read();
    if (myChar == '_') {
      endedSerial = true;
    } 
    else if ((uint8_t)myChar != 10) { // \n
      roomCode += myChar;
    }
  }

  if (endedSerial) {
    Serial.print("rf_send="+roomCode);
    roomCode.toCharArray(msg, roomCode.length() + 1);
    sendMessage(msg);
    roomCode = "";
    endedSerial = false;
  }
 
}

void sendMessage(char msg[MAX_WORD_LENGTH]) {
  vw_send((uint8_t *)msg, strlen(msg));
  vw_wait_tx();
  delay(50);
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

boolean wasMessageReceived() {
  uint8_t buf[VW_MAX_MESSAGE_LEN] = "";
  uint8_t buflen = VW_MAX_MESSAGE_LEN;
  
  if (vw_get_message(buf, &buflen)) { // received a message
    strcpy(msg,"o");
    vw_send((uint8_t *)msg, strlen(msg));      //Send the message
    vw_wait_tx();                              // Wait until the whole message is gone
    Serial.print((char *)buf);
    return true;
  }
}
