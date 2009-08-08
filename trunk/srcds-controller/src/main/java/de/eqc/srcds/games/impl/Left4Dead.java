package de.eqc.srcds.games.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.eqc.srcds.games.Game;

public class Left4Dead extends Game {

    public Left4Dead() {

	super("left4dead");

	addParameter("game", "left4dead");
    }

    @Override
    public List<String> getFilesForEdit() {

	List<String> files = new ArrayList<String>();
	Collections.addAll(files, "left4dead/gameinfo.txt",
		"left4dead/missioncycle.txt", "left4dead/motd.txt",
		"left4dead/cfg/private_server.cfg", "left4dead/cfg/server.cfg");
	return files;
    }

    @Override
    public List<String> getFilesForSync() {

	List<String> files = new ArrayList<String>();
	Collections.addAll(files, "left4dead/gameinfo.txt",
		"left4dead/missioncycle.txt", "left4dead/motd.txt",
		"left4dead/cfg/server.cfg");
	return files;
    }
}
