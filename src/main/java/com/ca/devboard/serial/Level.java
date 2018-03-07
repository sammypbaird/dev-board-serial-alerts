package com.ca.devboard.serial;

public enum Level 
{
	LOW,
	HIGH;
	
	public int toInt()
	{
		if (this == LOW)
			return 0;
		else
			return 1;
	}
}
