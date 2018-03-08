package com.ca.devboard.serial;

import java.io.IOException;

/**
 * Example of how to use the library
 */
public class Main implements SerialDataReceivedListener
{
	private static final int ALERT_0 = 0;
	
	public Main() throws InterruptedException, IOException
	{
		SerialIO serialIO = new SerialIO(9600, this);

		while (true)
		{
			serialIO.sendCommand(ALERT_0, 0);
			Thread.sleep(1000);
			serialIO.sendCommand(ALERT_0, 100);
			Thread.sleep(1000);
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
