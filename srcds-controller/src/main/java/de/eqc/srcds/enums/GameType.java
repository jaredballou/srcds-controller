package de.eqc.srcds.enums;

import de.eqc.srcds.games.Game;
import de.eqc.srcds.games.impl.CounterStrikeSource;
import de.eqc.srcds.games.impl.Left4Dead;

public enum GameType {

    LEFT4DEAD(new Left4Dead()),
    COUNTERSTRIKE_SOURCE(new CounterStrikeSource());

    private final Game implementation;

    private GameType(Game implementation) {

	this.implementation = implementation;
    }

    public Game getImplementation() {

	return implementation;
    }

}