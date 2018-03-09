package com.ca.devboard.serial.demo;

import com.ca.devboard.serial.SerialDataReceivedListener;
import com.ca.devboard.serial.SerialIO;
import java.io.IOException;

public class Main implements SerialDataReceivedListener
{
	private final CommandLineOptions cli;
	private SerialIO serialIo;
	
	public Main(CommandLineOptions cli)
	{
		this.cli = cli;
	}
	
	public void start() throws InterruptedException, IOException
	{
		serialIo = SerialIO.builder().comPort(cli.getComPort()).serialDataReceivedListener(this).build();
		if (null != cli.getType())
		switch (cli.getType())
		{
			case DATABASE_CPU:
				GraphiteAlert.run(serialIo, false);
				break;
			case DATABASE_CPU_MOCK:
				GraphiteAlert.run(serialIo, true);
				break;
			case JIRA:
				JiraCaseCountAlert.runDemo(serialIo);
				break;
			case RANDOM:
				TestAlert.randomValues(serialIo);
				break;
			case ON_OFF:
				TestAlert.onOff(serialIo);
				break;
			case TIMER:
				TimerAlert.start(serialIo);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void dataReceived(String string)
	{
		System.out.println(string);
	}
	
	public static void main(String[] args) throws Exception
	{
		CommandLineOptions cli = new CommandLineOptions(args);
		cli.parse();
		Main main = new Main(cli);
		main.start();
	}
}
