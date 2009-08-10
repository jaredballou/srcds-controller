package de.eqc.srcds.rcon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.eqc.srcds.rcon.exceptions.AuthenticationException;
import de.eqc.srcds.rcon.exceptions.ResponseEmptyException;
import de.eqc.srcds.rcon.exceptions.TimeoutException;

/**
 * This class implements a RCON connection to a SRCDS server. It is based on the
 * RconEd library version 0.4. See <http://rconed.sourceforge.net> for more
 * information.
 * 
 * @author DeadEd
 * @author oscahie (aka PiTaGoRaS)
 * @author David Hayes
 */
public class RconConnection {

    final static int SERVERDATA_EXECCOMMAND = 2;
    final static int SERVERDATA_AUTH = 3;
    final static int SERVERDATA_RESPONSE_VALUE = 0;
    final static int SERVERDATA_AUTH_RESPONSE = 2;
    final static int RESPONSE_TIMEOUT = 2000;
    final static int MULTIPLE_PACKETS_TIMEOUT = 300;
    final static int DEFAULT_RCON_PORT = 27015;

    private final Socket rconSocket;
    private final InputStream in;
    private final OutputStream out;

    public RconConnection(String ip, String password)
	    throws AuthenticationException, TimeoutException {

	this(ip, DEFAULT_RCON_PORT, password);
    }

    public RconConnection(String ip, int port, String password)
	    throws AuthenticationException, TimeoutException {

	rconSocket = new Socket();
	try {
	    rconSocket.connect(new InetSocketAddress(ip, port), 1000);
	    rconSocket.setSoTimeout(RESPONSE_TIMEOUT);
	    out = rconSocket.getOutputStream();
	    in = rconSocket.getInputStream();
	} catch (IOException e) {
	    throw new AuthenticationException(e.getLocalizedMessage());
	}

	try {
	    if (!authenticate(password)) {
		throw new AuthenticationException("Authentication failed");
	    }
	} catch (SocketTimeoutException timeout) {
	    throw new TimeoutException("Timeout occured during authentication");
	}
    }

    public void close() throws IOException {

	out.close();
	in.close();
	rconSocket.close();
    }

    public String send(String command) throws SocketTimeoutException,
	    AuthenticationException, ResponseEmptyException {

	String response = null;
	ByteBuffer[] resp = sendCommand(command);
	if (resp != null) {
	    response = assemblePackets(resp);
	}
	if (response == null || response.length() == 0) {
	    throw new ResponseEmptyException("Response is empty");
	}
	return response;
    }

    private ByteBuffer[] sendCommand(String command)
	    throws SocketTimeoutException {

	byte[] request = contructPacket(2, SERVERDATA_EXECCOMMAND, command);

	ByteBuffer[] resp = new ByteBuffer[128];
	int i = 0;
	try {
	    out.write(request);
	    resp[i] = receivePacket(); // First and maybe the unique response
	    // packet
	    try {
		// We don't know how many packets will return in response, so
		// we'll
		// read() the socket until TimeoutException occurs.
		rconSocket.setSoTimeout(MULTIPLE_PACKETS_TIMEOUT);
		while (true) {
		    resp[++i] = receivePacket();
		}
	    } catch (SocketTimeoutException e) {
		// No more packets in the response, go on
		return resp;
	    }

	} catch (SocketTimeoutException timeout) {
	    // Timeout while connecting to the server
	    throw timeout;
	} catch (Exception e) {
	    System.err.println("I/O error on socket\n");
	}
	return null;
    }

    private static byte[] contructPacket(int id, int cmdtype, String s1) {

	ByteBuffer p = ByteBuffer.allocate(s1.length() + 16);
	p.order(ByteOrder.LITTLE_ENDIAN);

	// length of the packet
	p.putInt(s1.length() + 12);
	// request id
	p.putInt(id);
	// type of command
	p.putInt(cmdtype);
	// the command itself
	p.put(s1.getBytes());
	// two null bytes at the end
	p.put((byte) 0x00);
	p.put((byte) 0x00);
	// null string2 (see Source protocol)
	p.put((byte) 0x00);
	p.put((byte) 0x00);

	return p.array();
    }

    private ByteBuffer receivePacket() throws Exception {

	ByteBuffer p = ByteBuffer.allocate(4120);
	p.order(ByteOrder.LITTLE_ENDIAN);

	byte[] length = new byte[4];

	if (in.read(length, 0, 4) == 4) {
	    // Now we've the length of the packet, let's go read the bytes
	    p.put(length);
	    int i = 0;
	    while (i < p.getInt(0)) {
		p.put((byte) in.read());
		i++;
	    }
	    return p;
	} else {
	    return null;
	}
    }

    private static String assemblePackets(ByteBuffer[] packets) {

	// Return the text from all the response packets together
	String response = "";
	for (int i = 0; i < packets.length; i++) {
	    if (packets[i] != null) {
		response = response.concat(new String(packets[i].array(), 12,
			packets[i].position() - 14));
	    }
	}
	return response;
    }

    private boolean authenticate(String password) throws SocketTimeoutException {

	byte[] authRequest = contructPacket(1337, SERVERDATA_AUTH, password);
	ByteBuffer response = ByteBuffer.allocate(64);
	try {
	    out.write(authRequest);
	    response = receivePacket(); // junk response packet
	    response = receivePacket();

	    // Lets see if the received request_id is leet enougth ;)
	    if ((response.getInt(4) == 1337)
		    && (response.getInt(8) == SERVERDATA_AUTH_RESPONSE)) {
		return true;
	    }
	} catch (SocketTimeoutException e) {
	    throw e;
	} catch (Exception e) {
	    // Ignore: authentication failed anyway
	}

	return false;
    }

}