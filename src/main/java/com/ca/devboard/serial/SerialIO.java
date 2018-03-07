package com.ca.devboard.serial;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Contains the methods to read and write to the serial connection.
 */
public class SerialIO implements SerialPortEventListener
{
	private static final int COMMAND_DIGITAL_WRITE = 0;
	
	private final SerialPort serialPort;
	
	/**
	 * Receives messages from serial
	 */
	private final SerialDataReceivedListener serialDataReceivedListener;
	
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private final BufferedReader input;
	/**
	 * The output stream to the port
	 */
	private final OutputStream output;

	public SerialIO(SerialPort serialPort, SerialDataReceivedListener serialDataReceivedListener, BufferedReader input, OutputStream output)
	{
		this.serialPort = serialPort;
		this.serialDataReceivedListener = serialDataReceivedListener;
		this.input = input;
		this.output = output;
	}
	
	public void digitalWrite(int pin, Level level) throws IOException
	{
		output.write(COMMAND_DIGITAL_WRITE);
		output.write(pin);
		output.write(level.toInt());
		output.flush();
	}
	
	public void writeChar(char c) throws IOException
	{
		output.write(c);
		output.flush();
	}
	
	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close()
	{
		if (serialPort != null)
		{
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				serialDataReceivedListener.dataReceived(input.readLine());
			}
			catch (Exception e)
			{
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
}
