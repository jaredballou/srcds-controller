package de.eqc.srcds.xmlbeans.impl;

import static de.eqc.srcds.configuration.impl.ConfigurationRegistry.SRCDS_GAMETYPE;
import static de.eqc.srcds.core.Constants.DEFAULT_CONFIG_FILENAME;

import java.io.File;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.games.Game;
import de.eqc.srcds.xmlbeans.XmlBean;


public class GameConfiguration extends XmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -7008363335234493278L;
    private final Configuration config;
    private final int selectedFileIndex;
    
    public GameConfiguration(Configuration config, int selectedFileIndex) {

	super(true);
	this.config = config;
	this.selectedFileIndex = selectedFileIndex;
    }

    @Override
    protected String toXml(int indent) {
	
	StringBuilder sb = new StringBuilder(header(indent));
	try {
        	Game game = config.getValue(SRCDS_GAMETYPE, GameType.class).getImplementation();
        	File file = new File(config.getValue("srcds.controller.srcds.path", String.class), game.getFilesForEdit().get(selectedFileIndex));      	
        	
        	sb.append(indent("<ConfigurationFiles>\n", indent + 1));
        	for (int id = 0; id < game.getFilesForEdit().size(); id++) {
        	    String configFile = game.getFilesForEdit().get(id);
        	    sb.append(indent(String.format("<ConfigurationFile id=\"%d\" name=\"%s\" />\n", id, configFile), indent + 2));
        	}
        	sb.append(indent("</ConfigurationFiles>\n", indent + 1));
        
        	sb.append(indent(String.format("<FileContent id=\"%d\">", selectedFileIndex), indent + 1));
        	sb.append("<![CDATA[");
        	sb.append(Utils.getFileContent(file));
        	sb.append("]]>");
        	sb.append("</FileContent>\n");
	} catch(Exception e) {
	    sb.append("<Error>Unable to load required resources</Error>");
	}
	
	return sb.append(footer(indent)).toString();
    }

//    public static void main(String[] args) throws ConfigurationException {
//	File configFile = new File(DEFAULT_CONFIG_FILENAME);
//	Configuration config = new XmlPropertiesConfiguration(configFile);
//	System.out.println(new GameConfiguration(config, 0).toXml());
//    }
}