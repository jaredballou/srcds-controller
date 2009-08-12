import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * @author Holger Cremer
 */
public class StartProcTest {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

//	Runtime.getRuntime().exec("notepad.exe");
//	System.in.read();
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
	String d = dateFormat.format(new Date(System.currentTimeMillis() + 86400 * 1000));
	System.out.println(d);

    }

}
