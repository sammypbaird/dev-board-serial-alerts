package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialIO;
import java.io.IOException;
import java.util.Random;

public class TestAlert 
{
	private Random random = new Random();
	
	public void onOff(SerialIO serialIo, int alertId) throws InterruptedException, IOException
	{
		while (true)
		{
			serialIo.sendCommand(alertId, 100);
			System.out.println("Alert " + alertId + ", level " + 100);
			Thread.sleep(4000);
			serialIo.sendCommand(alertId, 0);
			System.out.println("Alert " + alertId + ", level " + 0);
			Thread.sleep(4000);
		}
	}
	
	public void randomValues(SerialIO serialIo, int alertId) throws InterruptedException, IOException
	{
		while (true)
		{
			int level = random.nextInt(100);
			serialIo.sendCommand(alertId, level);
			System.out.println("Alert " + alertId + ", level " + level);
			Thread.sleep(4000);
		}
	}
}
