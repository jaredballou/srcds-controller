import java.io.IOException;
import java.net.SocketTimeoutException;

import de.eqc.srcds.rcon.RconConnection;
import de.eqc.srcds.rcon.exceptions.AuthenticationException;
import de.eqc.srcds.rcon.exceptions.ResponseEmptyException;
import de.eqc.srcds.rcon.exceptions.TimeoutException;




/**
 * @author Holger Cremer
 */
public class RconTest {

    /**
     * @param args
     * @throws TimeoutException 
     * @throws AuthenticationException 
     * @throws ResponseEmptyException 
     * @throws ResponseEmpty 
     * @throws BadRcon 
     * @throws SocketTimeoutException 
     * @throws IOException 
     */
    public static void main(String[] args) throws AuthenticationException, TimeoutException, ResponseEmptyException, IOException {

	RconConnection rcon = new RconConnection("circlelab.de", "glockwise");
	System.out.println(rcon.send("Status"));
	
	rcon.close();
    }

}
