package de.eqc.srcds.enums;

import de.eqc.srcds.games.Game;
import de.eqc.srcds.games.impl.Left4Dead;

public enum GameType {

	LEFT4DEAD(new Left4Dead());
	
	private final Game implementation;
	
	private GameType(Game implementation) {
		
		this.implementation = implementation;
	}
	
	public Game getImplementation() {

		return implementation;
	}
}
