package com.ca.devboard.serial;

import java.io.IOException;
import java.time.Duration;

/**
 * Example of how to use the library
 */
public class Main implements SerialDataReceivedListener
{
	private static final int ALERT_0 = 0;
	private static final long RECONNECT_SEC = 2;
	
	public Main() throws InterruptedException, IOException
	{
		SerialIO arduinoSerialIO = null;

		while (true)
		{
			if (arduinoSerialIO == null)
			{
				try
				{
					arduinoSerialIO = SerialConnection.connect(9600, this);
				}
				catch (Exception ex)
				{
					System.out.println(String.format("Unable to connect: %s. Trying again in %d seconds",
													 ex.getLocalizedMessage(), RECONNECT_SEC));
					Thread.sleep(Duration.ofSeconds(RECONNECT_SEC).toMillis());
					continue;
				}
			}
			try
			{
				arduinoSerialIO.sendCommand(ALERT_0, 0);
				Thread.sleep(1000);
				arduinoSerialIO.sendCommand(ALERT_0, 100);
				Thread.sleep(1000);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				Thread.sleep(Duration.ofSeconds(5).toMillis());
			}
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
