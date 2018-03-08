package com.ca.devboard.serial;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Loads the appropriate dll for serial communication.
 */
public class DllLoader
{
	/**
	 * This method must be called before using serial communication
	 */
	public static void loadLibrary()
	{
		String system = System.getProperty("os.name");
		String libExtension = ".dll"; //assume windows, unless it's unix
		if (system.equals("unix"))
		{
			libExtension = ".so";
		}
		String libraryName = "rxtxSerial" + libExtension;

		String tempFolderPath = System.getProperty("java.io.tmpdir") + "rxtx";
		File fileOut = new File(tempFolderPath + File.separator + libraryName);
		try (InputStream in = DllLoader.class.getResourceAsStream("/lib/" + libraryName); 
		     OutputStream out = FileUtils.openOutputStream(fileOut))
		{
			System.out.println("Writing dll to: " + fileOut.getAbsolutePath());
			IOUtils.copy(in, out);
			addPathToClassPath(tempFolderPath);
		}
		catch (Exception ex)
		{
			throw new IllegalArgumentException("Unabled to load rxtxSerial library: " + ex.getMessage());
		}
	}
	
	private static void addPathToClassPath(String rxtxLibraryPath)throws Exception
	{
		String libraryPath = System.getProperty("java.library.path") + File.pathSeparator + rxtxLibraryPath;
		System.setProperty("java.library.path", libraryPath);
		Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );
	}

	public static void main(String[] args) throws IOException
	{
		loadLibrary();
	}
}
