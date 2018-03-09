package com.ca.devboard.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.Duration;
import java.util.TooManyListenersException;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;

/**
 * Contains the methods to read and write to the serial connection.
 */
public class SerialIO implements SerialPortEventListener
{
	private static final long RECONNECT_SEC = 2;

	private SerialPort serialPort = null;
	private int baudRate = 9600;
	private String comPort = null;
	
	/**
	 * Receives messages from serial
	 */
	private final SerialDataReceivedListener serialDataReceivedListener;
	
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input = null;
	/**
	 * The output stream to the port
	 */
	private OutputStream output = null;
	
	/**
	 * If you want to use defaults for some, use the builder() method to create a SerialIO.
	 */
	public SerialIO(int baudRate, String comPort, SerialDataReceivedListener serialDataReceivedListener)
	{
		this.baudRate = baudRate;
		this.comPort = comPort;
		this.serialDataReceivedListener = serialDataReceivedListener;
	}

	public static SerialIOBuilder builder()
	{
		return new SerialIOBuilder();
	}

	public void sendCommand(int alertId, int alertLevel) throws IOException, InterruptedException
	{
		connect();
		try
		{
			output.write(alertId);
			output.write(alertLevel);
			output.flush();
		}
		catch (Exception ex)
		{
			serialPort = null;
			ex.printStackTrace();
			Thread.sleep(Duration.ofSeconds(5).toMillis());
		}
	}

	private void connect() throws InterruptedException, IOException
	{
		if (serialPort != null)
			return;
		System.out.println("Serial port connection not established. Attempting to connect...");
		while (true)
		{
			try
			{
				serialPort = SerialConnection.connect(baudRate, comPort);
				input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				output = serialPort.getOutputStream();
				try
				{
					serialPort.addEventListener(this);
				}
				catch (TooManyListenersException ex)
				{
					throw new IllegalArgumentException("Failed to setup serial event listener", ex);
				}
				break;
			}
			catch (Exception ex)
			{
				System.out.println(String.format("Unable to connect: %s. Trying again in %d seconds",
												 ex.getLocalizedMessage(), RECONNECT_SEC));
				Thread.sleep(Duration.ofSeconds(RECONNECT_SEC).toMillis());
			}
			
		}
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
				if (input != null && serialDataReceivedListener != null)
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
