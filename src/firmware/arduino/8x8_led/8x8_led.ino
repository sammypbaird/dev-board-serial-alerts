int latchPin = 4; // pis connected to shift registors
int clockPin = 5;
int dataPin = 3;
int pins [8] = {6, 7, 8, 9, 10, 11, 12, 13}; // common cathode pins
unsigned long lastMillis = 0;
int animationStage=0;

byte smiley_happy[8] = 
  {   B01100110,
      B01100110,
      B00000000,
      B00000000,
      B10000001,
      B01000010,
      B00111100,
      B00000000
  };

byte smiley_blink[8] = 
  {   B00000000,
      B01100110,
      B00000000,
      B00000000,
      B10000001,
      B01000010,
      B00111100,
      B00000000
  };

byte smiley_meh[8] = 
  {   B00100010,
      B01100110,
      B00000000,
      B00000000,
      B00000000,
      B11111111,
      B00000000,
      B00000000
  };

byte smiley_sad[8] = 
  {   B01100110,
      B01100110,
      B00000000,
      B00000000,
      B00000000,
      B00111100,
      B01000010,
      B10000001
  };

byte smiley_o[8] = 
  {   B01100110,
      B01100110,
      B00000000,
      B00000000,
      B00111100,
      B01111110,
      B01111110,
      B00111100
  };

byte blank[8] = 
{   B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000
  };

int lastLevel0 = 0;
int lastLevel1 = 0;

void setup() 
{
  Serial.begin(9600); // Serial begin
  pinMode(2, OUTPUT);
  pinMode(latchPin, OUTPUT); // Pin configuration
  pinMode(clockPin, OUTPUT);
  pinMode(dataPin, OUTPUT);
  for (int i = 0; i < 8; i++) { // for loop is used to configure common cathodes
    pinMode(pins[i], OUTPUT);
    digitalWrite(pins[i], HIGH);
  }
  digitalWrite(2, LOW);
}

void loop() 
{
  if (Serial.available() >= 3)
  {
	int command = Serial.read();
    int alertId = Serial.read();
    int level = Serial.read();
    if (alertId == 0)
      lastLevel0 = level;
    else if (alertId = 1)
      lastLevel1 = level;
  }

  //smiley face
  if (lastLevel0 < 50)
    display_char(smiley_happy);
  else if (lastLevel0 < 70)
    display_char(smiley_meh);
  else if (lastLevel0 < 90)
    display_char(smiley_sad);
  else
    display_char(smiley_o);
  
 //spinny thing
  if (lastLevel1 > 0)
    digitalWrite(2, HIGH);
  else
    digitalWrite(2, LOW);
}

void display_char(byte ch[8]) { // Method do the multiplexing
  for (int j = 0; j < 8; j++) {
    digitalWrite(latchPin, LOW);
    digitalWrite(pins[j], LOW);

    shiftOut(dataPin, clockPin, LSBFIRST, ch[j]);
    digitalWrite(latchPin, HIGH);
    //delay(1);
    digitalWrite(latchPin, LOW);
    shiftOut(dataPin, clockPin, LSBFIRST, B00000000); // to get rid of flicker when
    digitalWrite(latchPin, HIGH);
    digitalWrite(pins[j], HIGH);

  }
}
