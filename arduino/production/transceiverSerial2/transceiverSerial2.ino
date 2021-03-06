/**
 * Program for the arduino connected to the serial
 * Receives and sends messages from/to others appliances, 
 * monitors temperature. Needed
 */
#include <VirtualWire.h>
#define BUTTON_PIN 3
#define RX_MODULE_PIN 11
#define TX_MODULE_PIN 13
#define MAX_WORD_LENGTH 40
#define aref_voltage 3.3         // we tie 3.3V to ARef and measure it with a multimeter!
#define TEMPERATURE_PIN 1

// button stuff
int readingButton;           // the current reading from the input pin
int previousButton = LOW;    // the previous reading from the input pin
long time = 0;               // the last time the output pin was toggled
long debounce = 250;         // the debounce time, increase if the output flickers
int state = HIGH;            // the current state of the output pin

long timeLastSent = 0;
long debounceTransmitter = 50;

char myChar;
char curChar;
boolean endedSerial = false;
String roomCode;
String msgReceived;
String stateMessage;
char msg[MAX_WORD_LENGTH];


boolean measureTemperature = true;
int tempReading;        // the analog reading from the sensor
float lastTemp;

void setup() {
    Serial.begin(9600);
    pinMode(BUTTON_PIN, INPUT);

    vw_setup(2000);                   // Bits per sec
    vw_set_ptt_inverted(true);        // Required for DR3100
    vw_set_rx_pin(RX_MODULE_PIN);
    vw_set_tx_pin(TX_MODULE_PIN);
    vw_rx_start();
    
    analogReference(EXTERNAL); // TEMPERATURE READING
    lastTemp = ((analogRead(TEMPERATURE_PIN) * aref_voltage) / (1024.0) - 0.5) * 100;
}

void loop() {
    if (wasButtonPressed()) {
        sendMessage("o");
    } else if (measureTemperature) {
        tempReading = analogRead(TEMPERATURE_PIN);  

        float temperatureC = ((tempReading * aref_voltage) / (1024.0) - 0.5) * 100 ;  //converting from 10 mv per degree wit 500 mV offset
                                                     //to degrees ((volatge - 500mV) times 100)

        if (temperatureC > lastTemp && temperatureC - lastTemp > 1) {
            lastTemp = temperatureC;
            Serial.print("temp_");Serial.println(temperatureC);
        } else if (lastTemp > temperatureC && lastTemp - temperatureC > 1) {
            lastTemp = temperatureC;
            Serial.print("temp_");Serial.println(temperatureC);
        }
         // now convert to Fahrenheight - if needed
        /** float temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;
        Serial.print(temperatureF); Serial.println(" degrees F"); */
    }

    if (Serial.available() > 0) {    //processing incoming data from serial <- from mainframe          
        myChar = Serial.read();
        if (myChar == '_') {
	    endedSerial = true;
	} else if ((uint8_t)myChar != 10) { // \n
	    roomCode += myChar;
        }
    }

    if (endedSerial) {
        //Serial.println("snd="+roomCode);
	roomCode.toCharArray(msg, roomCode.length() + 1);
        sendMessage(msg);
	roomCode = "";
        endedSerial = false;
    }
	
    if (wasMessageReceived() && !vx_tx_active()) { // if the arduino received something
        //sendMessage("o");
    }
}

void sendMessage(char msg[MAX_WORD_LENGTH]) {
    if (millis() - timeLastSent > debounceTransmitter) {
        timeLastSent = millis();
        vw_rx_stop();
        vw_send((uint8_t *)msg, strlen(msg));
        
        vw_wait_tx();
        vw_rx_start();
    } // else output would flicker (2 messages with the same meaning) - notice it won't matter anymore.
    // Changes were made to the identification of the appliances -> uppercase words and lowercase words,
    // so after correcting patches are deployed this if can be stripped of the code
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
        Serial.print("rcv=");
        Serial.println((char*)buf); // somehow buffer has one extra '/n'
    }
}
