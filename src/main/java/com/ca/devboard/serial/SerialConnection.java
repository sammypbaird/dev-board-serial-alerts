package com.ca.devboard.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Encapsulates all the logic needed to connect to the Arduino through the serial port. You must call initialize before
 * using it.
 */
public class SerialConnection
{
	/**
	 * Milliseconds to block while waiting for port open
	 */
	private static final int TIME_OUT = 2000;

	public static SerialIO connect(int baudRate, SerialDataReceivedListener serialDataReceivedListener)
	{
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		List<CommPortIdentifier> foundSerialPorts = new ArrayList<>();
		while (portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId = (CommPortIdentifier)portEnum.nextElement();
			if (currPortId.getPortType() != CommPortIdentifier.PORT_SERIAL)
				continue;
			System.out.println("Found serial port: " + currPortId.getName());
			foundSerialPorts.add(currPortId);
		}
		for (CommPortIdentifier portId : foundSerialPorts)
		{
			try
			{
				// open serial port, and use class name for the appName.
				SerialPort serialPort = (SerialPort)portId.open(SerialConnection.class.getName(), TIME_OUT);
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
				System.out.println("Connected to Port " + portId.getName());
				return arduinoSerialIO;
			}
			catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e)
			{
				System.out.println(String.format("Unable to connect to %s: %s", portId.getName(), e.getLocalizedMessage()));
			}
		}

		throw new IllegalArgumentException("Could not any serial COM ports!");
	}
}
