package com.ca.devboard.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Encapsulates all the logic needed to connect to the Arduino through
 * the serial port. You must call initialize before using it.
 */
public class SerialConnection
{
	/**
	 * The port we're normally going to use.
	 */
	private static final String PORT_NAMES[] =
	{
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
		"/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"COM1",
		"COM2",
		"COM3",
		"COM4",
		"COM5",
		"COM6",
		"COM7",
		"COM8"
	};
	
	/**
	 * Milliseconds to block while waiting for port open
	 */
	private static final int TIME_OUT = 2000;
	
	public static SerialIO connect(int baudRate, SerialDataReceivedListener serialDataReceivedListener)
	{
		DllLoader.loadLibrary();
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES)
			{
				if (currPortId.getName().equals(portName))
				{
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null)
		{
			throw new IllegalArgumentException("Could not find COM port!");
		}
		
		System.out.println("Connected to Port " + portId.getName());

		try
		{
			// open serial port, and use class name for the appName.
			SerialPort serialPort = (SerialPort) portId.open(SerialConnection.class.getName(), TIME_OUT);
			serialPort.disableReceiveTimeout();
			serialPort.enableReceiveThreshold(1);
			serialPort.notifyOnDataAvailable(true);

			// set port parameters
			serialPort.setSerialPortParams(baudRate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			BufferedReader input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			
			SerialIO arduinoSerialIO = new SerialIO(serialPort, serialDataReceivedListener, input, serialPort.getOutputStream());
			serialPort.addEventListener(arduinoSerialIO);
			return arduinoSerialIO;
		}
		catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e)
		{
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	
}
