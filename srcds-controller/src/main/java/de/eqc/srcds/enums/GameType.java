package de.eqc.srcds.enums;

import de.eqc.srcds.games.AbstractGame;
import de.eqc.srcds.games.impl.CounterStrikeSource;
import de.eqc.srcds.games.impl.Left4Dead;

public enum GameType {

    LEFT4DEAD(new Left4Dead()),
    COUNTERSTRIKE_SOURCE(new CounterStrikeSource());

    private final AbstractGame implementation;

    private GameType(final AbstractGame implementation) {

	this.implementation = implementation;
    }

    public AbstractGame getImplementation() {

	return implementation;
    }

}