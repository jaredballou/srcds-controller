package de.eqc.srcds.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


/**
 * Mostly like the {@link SimpleFormatter} but outputs only one line per log record.
 * 
 * @author Holger Cremer
 */
public class CompactSimpleFormatter extends Formatter {

    Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;

    private Object args[] = new Object[1];

    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String lineSeparator = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));

    private String getClassNameWithoutPackage(String sourceClassName) {
	int lastIndexOf = sourceClassName.lastIndexOf(".");
	if (lastIndexOf == -1) {
	    return sourceClassName;
	}
	return sourceClassName.substring(lastIndexOf + 1); 
    }
    
    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
	StringBuilder sb = new StringBuilder();
	// Minimize memory allocations here.
	dat.setTime(record.getMillis());
	args[0] = dat;
	StringBuffer text = new StringBuffer();
	if (formatter == null) {
	    formatter = new MessageFormat(format);
	}
	formatter.format(args, text, null);
	sb.append(text);
	sb.append(" [");
	if (record.getSourceClassName() != null) {	
	    sb.append(getClassNameWithoutPackage(record.getSourceClassName()));
	} else {
	    sb.append(record.getLoggerName());
	}
	sb.append("] ");

	String message = formatMessage(record);
	sb.append(record.getLevel().getLocalizedName());
	sb.append(": ");
	sb.append(message);
	sb.append(lineSeparator);
	if (record.getThrown() != null) {
	    try {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        record.getThrown().printStackTrace(pw);
	        pw.close();
		sb.append(sw.toString());
	    } catch (Exception ex) {
	    }
	}
	return sb.toString();
    }
}
