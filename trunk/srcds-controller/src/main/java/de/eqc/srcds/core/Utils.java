package de.eqc.srcds.core;

import static de.eqc.srcds.core.Constants.MILLIS_PER_SEC;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 * @author Holger Cremer
 */
public class Utils {

    public static String getUrlContent(URL url) throws IOException {

	return getInputStreamContent(url.openStream());
    }

    public static String getFileContent(File file)
	    throws FileNotFoundException, IOException {

	return getInputStreamContent(new BufferedInputStream(
		new FileInputStream(file)));
    }

    /**
     * Get the content of the input stream and closes the stream.
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static String getInputStreamContent(InputStream input)
	    throws IOException {

	StringBuilder builder = new StringBuilder();
	try {
	    byte[] buffer = new byte[1024];
	    for (int len = 0; (len = input.read(buffer)) != -1;) {
		builder.append(new String(buffer, 0, len));
	    }
	} finally {
	    closeQuietly(input);
	}
	return builder.toString();
    }
    
    /**
     * @param file
     * @param newContent
     * @throws IOException 
     */
    public static void saveToFile(File file, String fileContent) throws IOException {
	BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
	try {
	    output.write(fileContent.getBytes());
	} finally {
	    closeQuietly(output);
	}
    }

    public static void closeQuietly(Closeable closeable) {

	if (closeable == null) {
	    return;
	}
	try {
	    closeable.close();
	} catch (IOException excp) {
	    // Ignore
	}
    }
    
    public static long millisToSecs(long millis) {
	
	return millis / MILLIS_PER_SEC;
    }
}