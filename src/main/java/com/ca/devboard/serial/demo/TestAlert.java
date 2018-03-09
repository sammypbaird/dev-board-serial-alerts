package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialIO;
import java.io.IOException;
import java.util.Random;

public class TestAlert 
{
	private static final Random RANDOM = new Random();

	private static void sendCommand(SerialIO serialIo, int alertId, int level) throws IOException, InterruptedException
	{
		serialIo.sendCommand(alertId, level);
		System.out.println("Alert " + alertId + ", level " + level);
	}

	public static void onOff(SerialIO serialIo) throws InterruptedException, IOException
	{
		while (true)
		{
			sendCommand(serialIo, 0, 100);
			sendCommand(serialIo, 1, 0);
			Thread.sleep(4000);
			sendCommand(serialIo, 0, 0);
			sendCommand(serialIo, 1, 100);
			Thread.sleep(4000);
		}
	}

	public static void randomValues(SerialIO serialIo) throws InterruptedException, IOException
	{
		while (true)
		{
			sendCommand(serialIo, 0, RANDOM.nextInt(100));
			sendCommand(serialIo, 1, RANDOM.nextInt(100));
			Thread.sleep(4000);
		}
	}
}
