// Firmware for Particle.io Photon
// by gpeterson
// Drives a quad 7-segment digit display
// Be sure to use caution wiring and use resistors where needed to not burn anything out!

SYSTEM_MODE(SEMI_AUTOMATIC);  // Secific to Particle platform

int digit1 = A0;
int digit2 = A1;
int digit3 = A2;
int digit4 = A3;

int segA = D0;
int segB = D1;
int segC = D2;
int segD = D3;
int segE = D4;
int segF = D5;
int segG = D6;
int segH = D7;

void setup()
{
    pinMode(digit1, OUTPUT);
    pinMode(digit2, OUTPUT);
    pinMode(digit3, OUTPUT);
    pinMode(digit4, OUTPUT);
    digitalWrite(digit1, HIGH);
    digitalWrite(digit2, HIGH);
    digitalWrite(digit3, HIGH);
    digitalWrite(digit4, HIGH);
    pinMode(segA, OUTPUT);
    pinMode(segB, OUTPUT);
    pinMode(segC, OUTPUT);
    pinMode(segD, OUTPUT);
    pinMode(segE, OUTPUT);
    pinMode(segF, OUTPUT);
    pinMode(segG, OUTPUT);
    pinMode(segH, OUTPUT);
    digitalWrite(segA, HIGH);
    digitalWrite(segB, HIGH);
    digitalWrite(segC, HIGH);
    digitalWrite(segD, HIGH);
    digitalWrite(segE, HIGH);
    digitalWrite(segF, HIGH);
    digitalWrite(segG, HIGH);
    // digitalWrite(segH, HIGH);

    Serial.begin(9600);
    RGB.control(true);  // Secific to Particle platform
}

void blue() {RGB.color(0, 0, 255);}
void red() {RGB.color(255, 0, 0);}
void orange() {RGB.color(125, 51, 0);}
void green() {RGB.color(0, 255, 0);}

void setColor(int level)
{
    if (level <= 0)
        green();
    else if (level > 50 && level < 90)
        orange();
    else
        red();
}

int numberDisplay = 0;
bool connected = false;

void loop()
{
    if (Serial.available() >= 3)
    {
		int command = Serial.read();
        int alertId = Serial.read();
        int level = Serial.read();
        // setColor(level);
        // numberDisplay = level;

        switch (alertId)
        {
            case 1:
                setColor(level);
                break;
            case 0:
                numberDisplay = level;
                break;
        }
    }

    displayNumber(numberDisplay);

    // Secific to Particle platform. Allows for connection to cloud to be trigger by manual button press
    if (System.buttonPushed() && !connected)
    {
        RGB.control(false);
        Particle.connect();
        connected = true;
    }
}

void displayNumber(int toDisplay)
{
    #define DISPLAY_BRIGHTNESS  600

    #define DIGIT_ON  LOW
    #define DIGIT_OFF  HIGH

    // Disect into individual digits
    int toDisplay4 = toDisplay % 10;
    toDisplay /= 10;
    int toDisplay3 = toDisplay % 10;
    toDisplay /= 10;
    int toDisplay2 = toDisplay % 10;
    toDisplay /= 10;
    int toDisplay1 = toDisplay % 10;

    // Clear out leading zeros
    if (toDisplay1 == 0)
    {
        toDisplay1 = 10;
        if (toDisplay2 == 0)
        {
            toDisplay2 = 10;
            if (toDisplay3 == 0)
                toDisplay3 = 10;
        }
    }

    for(int digit = 4 ; digit > 0 ; digit--)
    {
        //Turn on a digit for a short amount of time
        switch(digit)
        {
            case 1:
                digitalWrite(digit1, DIGIT_ON);
                lightNumber(toDisplay1);
                break;
            case 2:
                digitalWrite(digit2, DIGIT_ON);
                lightNumber(toDisplay2);
                break;
            case 3:
                digitalWrite(digit3, DIGIT_ON);
                lightNumber(toDisplay3);
                break;
            case 4:
                digitalWrite(digit4, DIGIT_ON);
                lightNumber(toDisplay4);
                break;
        }

        delayMicroseconds(DISPLAY_BRIGHTNESS); //Display this digit for a fraction of a second (between 1us and 5000us, 500 is pretty good)

        //Turn off all segments
        lightNumber(10);

        //Turn off all digits
        digitalWrite(digit1, DIGIT_OFF);
        digitalWrite(digit2, DIGIT_OFF);
        digitalWrite(digit3, DIGIT_OFF);
        digitalWrite(digit4, DIGIT_OFF);

        // delay(20);
    }
}

void lightNumber(int numberToDisplay) {

#define SEGMENT_ON  HIGH
#define SEGMENT_OFF LOW

  switch (numberToDisplay){

  case 0:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_ON);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_OFF);
    break;

  case 1:
    digitalWrite(segA, SEGMENT_OFF);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_OFF);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_OFF);
    digitalWrite(segG, SEGMENT_OFF);
    break;

  case 2:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_OFF);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_ON);
    digitalWrite(segF, SEGMENT_OFF);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 3:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_OFF);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 4:
    digitalWrite(segA, SEGMENT_OFF);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_OFF);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 5:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_OFF);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 6:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_OFF);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_ON);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 7:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_OFF);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_OFF);
    digitalWrite(segG, SEGMENT_OFF);
    break;

  case 8:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_ON);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 9:
    digitalWrite(segA, SEGMENT_ON);
    digitalWrite(segB, SEGMENT_ON);
    digitalWrite(segC, SEGMENT_ON);
    digitalWrite(segD, SEGMENT_ON);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_ON);
    digitalWrite(segG, SEGMENT_ON);
    break;

  case 10:
    digitalWrite(segA, SEGMENT_OFF);
    digitalWrite(segB, SEGMENT_OFF);
    digitalWrite(segC, SEGMENT_OFF);
    digitalWrite(segD, SEGMENT_OFF);
    digitalWrite(segE, SEGMENT_OFF);
    digitalWrite(segF, SEGMENT_OFF);
    digitalWrite(segG, SEGMENT_OFF);
    break;
  }
}
