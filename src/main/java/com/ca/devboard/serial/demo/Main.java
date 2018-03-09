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
		if (cli.getType() == CommandLineOptions.DemoType.DATABASE_CPU)
		{
			new GraphiteAlert().run(serialIo, false);
		}
		else if (cli.getType() == CommandLineOptions.DemoType.DATABASE_CPU_MOCK)
		{
			new GraphiteAlert().run(serialIo, true);
		}
		else if (cli.getType() == CommandLineOptions.DemoType.JIRA)
		{
			new JiraCaseCountAlert().run(serialIo);
		}
		else if (cli.getType() == CommandLineOptions.DemoType.RANDOM)
		{
			new TestAlert().randomValues(serialIo, 0);
		}
		else if (cli.getType() == CommandLineOptions.DemoType.ON_OFF)
		{
			new TestAlert().onOff(serialIo, 0);
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
