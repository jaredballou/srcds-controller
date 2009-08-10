package de.eqc.srcds.core;

import com.sun.net.httpserver.BasicAuthenticator;

public class HttpAuthenticator extends BasicAuthenticator {

    private final String username;
    private final String password;

    public HttpAuthenticator(String username, String password) {

	this("Secured Page", username, password);
    }

    public HttpAuthenticator(String realmName, String username,
	    String password) {

	super(realmName);
	this.username = username;
	this.password = password;
    }

    @Override
    public boolean checkCredentials(String username, String password) {

	return this.username.equals(username) && this.password.equals(password);
    }
}
