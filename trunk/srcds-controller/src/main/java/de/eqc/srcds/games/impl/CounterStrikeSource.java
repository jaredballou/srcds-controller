package de.eqc.srcds.games.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.eqc.srcds.games.Game;

public class CounterStrikeSource extends Game {

    public CounterStrikeSource() {

	super("css");

	addParameter("game", "css");
    }

    @Override
    public List<String> getFilesForEdit() {

	List<String> files = new ArrayList<String>();
//	Collections.addAll(files, "left4dead/gameinfo.txt",
//		"left4dead/missioncycle.txt", "left4dead/motd.txt",
//		"left4dead/cfg/private_server.cfg", "left4dead/cfg/server.cfg");
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
