package com.ca.devboard.serial;

import java.io.IOException;

public class Main implements SerialDataReceivedListener
{
	public Main() throws InterruptedException, IOException
	{
		SerialIO arduinoSerialIO = SerialConnection.connect(9600, this);

		while (true)
		{
			arduinoSerialIO.digitalWrite(2, Level.LOW);
			arduinoSerialIO.digitalWrite(3, Level.LOW);
			Thread.sleep(5000);
			arduinoSerialIO.digitalWrite(2, Level.HIGH);
			arduinoSerialIO.digitalWrite(3, Level.LOW);
			Thread.sleep(2000);
		}
	}
	
	@Override
	public void dataReceived(String string)
	{
		System.out.println(string);
	}
	
	public static void main(String[] args) throws Exception
	{
		Main main = new Main();
	}
}
