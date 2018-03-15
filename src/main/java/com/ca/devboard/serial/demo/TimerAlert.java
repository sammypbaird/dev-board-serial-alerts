package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialIO;
import java.io.IOException;
import javafx.util.Duration;

public class TimerAlert 
{
	private static void on(SerialIO serialIo, long millis) throws IOException, InterruptedException
	{
		serialIo.sendAlert(0, 100);
		serialIo.sendAlert(1, 100);
		System.out.println("Spinner on for " + millis + " ms");
		Thread.sleep(millis);
	}
	
	private static void off(SerialIO serialIo, long millis) throws IOException, InterruptedException
	{
		serialIo.sendAlert(0, 0);
		serialIo.sendAlert(1, 0);
		System.out.println("Spinner off for " + millis + " ms");
		Thread.sleep(millis);
	}
	
	public static void start(SerialIO serialIo) throws InterruptedException, IOException
	{
		off(serialIo, (long)Duration.minutes(4).toMillis());
		on(serialIo, (long)Duration.seconds(10).toMillis());
		off(serialIo, (long)Duration.seconds(50).toMillis());
		on(serialIo, (long)Duration.seconds(60).toMillis());
		off(serialIo, (long)Duration.seconds(10).toMillis());
	}
}
