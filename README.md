#dev-board-serial-alerts

A sample arduino application to communicate with the java application:

```
void setup() 
{
  // Start the hardware serial port
  Serial.begin(9600);

  //init the pin
  pinMode(10, OUTPUT);
}

void loop() 
{
  if (Serial.available() >= 2)
  {
    int alertId = Serial.read();
    int level = Serial.read();
    if (alertId == 0)
    {
      if (level == 0)
        digitalWrite(10, LOW);
      else
        digitalWrite(10, HIGH);
    }
    
    Serial.print("Received command: alertId: ");
    Serial.print(alertId, DEC);
    Serial.print(", level ");
    Serial.println(level);
    Serial.flush();
  }
}
```
