package com.ca.devboard.serial;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

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

	public static SerialPort connect(int baudRate)
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
				
				System.out.println("Connected to Port " + portId.getName());
				return serialPort;
			}
			catch (PortInUseException | UnsupportedCommOperationException e)
			{
				System.out.println(String.format("Unable to connect to %s: %s", portId.getName(), e.getLocalizedMessage()));
			}
		}

		throw new IllegalArgumentException("Could not any serial COM ports!");
	}
}
