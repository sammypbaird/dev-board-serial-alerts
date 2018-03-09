package com.ca.devboard.serial.demo;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineOptions
{
	private final String[] args;
	private final Options options = new Options();
	private Type type = Type.DATABASE_CPU;
	private String comPort;

	public CommandLineOptions(String[] args)
	{
		this.args = args;
		options.addOption("h", "help", false, "show help.");
		options.addOption("c", "comPort", true, "If specified, it will use this COM port instead of attaching to the first available one");
		options.addOption("t", "type", true, "The type of alert you want to do (db, db-mock, random, onoff");

	}
	
	public Type getType()
	{
		return type;
	}
	
	public String getComPort()
	{
		return comPort;
	}

	public void parse()
	{
		CommandLineParser parser = new BasicParser();

		CommandLine cmd = null;
		try
		{
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			
			if (cmd.hasOption("c"))
			{
				comPort = cmd.getOptionValue("c");
			}

			if (cmd.hasOption("t"))
			{
				String t = cmd.getOptionValue("t");
				if (t.equalsIgnoreCase("db"))
					type = Type.DATABASE_CPU;
				else if (t.equalsIgnoreCase("db-mock"))
					type = Type.DATABASE_CPU_MOCK;
				else if (t.equalsIgnoreCase("random"))
					type = Type.RANDOM;
				else if (t.equalsIgnoreCase("jira"))
					type = Type.JIRA;
				else if (t.equalsIgnoreCase("onoff"))
					type = Type.ON_OFF;
				else
				{
					System.out.println("Type \"" + t + "\" not understood. Allowed types: db, db-mock, jira, random, onoff");
					System.exit(0);
				}
			}

		}
		catch (ParseException e)
		{
			System.out.println("Failed to parse comand line properties: " + e.getMessage());
			help();
		}
	}

	private void help()
	{
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();

		formater.printHelp("Main", options);
		System.exit(0);
	}

	public enum Type
	{
		DATABASE_CPU,
		DATABASE_CPU_MOCK,
		JIRA,
		RANDOM,
		ON_OFF;
	}
}
