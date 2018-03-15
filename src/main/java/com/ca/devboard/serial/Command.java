package com.ca.devboard.serial;

public enum Command 
{
	/**
	 * An alert command contains 2 bytes. Byte 1 is the alertId, byte 2 is the alertLevel
	 */
	ALERT(0),
	/**
	 * An ascii command contains a string of characters, and is terminated by a NULL character (ascii 0)
	 */
	ASCII(1);
	
	private int id;
	
	Command(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
}
