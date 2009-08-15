package de.eqc.srcds.core;

import com.sun.net.httpserver.BasicAuthenticator;

public class HttpAuthenticator extends BasicAuthenticator {

    private final String username;
    private final String password;

    public HttpAuthenticator(final String username, final String password) {

	this("Secured Page", username, password);
    }

    public HttpAuthenticator(final String realmName, final String username,
	    final String password) {

	super(realmName);
	this.username = username;
	this.password = password;
    }

    @Override
    public boolean checkCredentials(final String username, final String password) {

	return this.username.equals(username) && this.password.equals(password);
    }
}
