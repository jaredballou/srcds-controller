package de.eqc.srcds.core;

import com.sun.net.httpserver.BasicAuthenticator;

public class DefaultAuthenticator extends BasicAuthenticator {

	public DefaultAuthenticator() {

		this("Secured Page");
	}
	
	public DefaultAuthenticator(String realmName) {
		
		super(realmName);
	}

	@Override
	public boolean checkCredentials(String username, String password) {
		return "admin".equals(username) && "joshua".equals(password);
	}

}
