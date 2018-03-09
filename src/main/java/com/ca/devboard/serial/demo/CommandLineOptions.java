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
	private DemoType type = DemoType.DATABASE_CPU;
	private String comPort;

	public CommandLineOptions(String[] args)
	{
		this.args = args;
		options.addOption("h", "help", false, "show help.");
		options.addOption("c", "comPort", true, "If specified, it will use this COM port instead of attaching to the first available one");
		options.addOption("t", "type", true, "The type of demo you want to perform (" + DemoType.allTypesToString() + ")");

	}
	
	public DemoType getType()
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
				type = DemoType.fromName(t);
				if (type == null)
				{
					System.out.println("Type \"" + t + "\" not understood. Allowed types: " + DemoType.allTypesToString());
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

	public enum DemoType
	{
		DATABASE_CPU("db"),
		DATABASE_CPU_MOCK("db-mock"),
		JIRA("jira"),
		RANDOM("random"),
		ON_OFF("onoff"),
		PROGRESSION("progression")
		;
		
		private final String name;
		
		DemoType(String option)
		{
			this.name = option;
		}

		public String getName()
		{
			return name;
		}
		
		public static DemoType fromName(String name)
		{
			for (DemoType type : DemoType.values())
			{
				if (type.getName().equalsIgnoreCase(name))
					return type;
			}
			return null;
		}
		
		public static String allTypesToString()
		{
			StringBuilder sb = new StringBuilder();
			DemoType[] demoTypes = DemoType.values();
			for (int i=0; i<demoTypes.length; i++)
			{
				sb.append(demoTypes[i].getName());
				if (i < demoTypes.length - 1)
					sb.append(", ");
			}
			return sb.toString();
		}
	}
}
