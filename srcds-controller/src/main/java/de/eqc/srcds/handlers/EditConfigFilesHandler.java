package de.eqc.srcds.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.xmlbeans.impl.GameConfiguration;


/**
 * @author Holger Cremer
 */
public class EditConfigFilesHandler extends AbstractRegisteredHandler {

    /*
     * @see de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(final HttpExchange httpExchange) throws Exception {
	final List<String> filesForEdit = getServerController().getGameType().getImplementation().getFilesForEdit();

	if (isPost()) {
	    final String fileIdParam = getPostParameter("id");
	    final String newContent = getPostParameter("content");
	    if (fileIdParam == null || newContent == null) {
		throw new IllegalArgumentException("id or content was null");
	    }
	    final int fileId = Integer.parseInt(fileIdParam);
	    saveFile(fileId, filesForEdit.get(fileId), newContent);
	} else {
	    final String fileIdParam = getParameter("id");
	    int fileId = 0;
	    if (fileIdParam != null) {
		fileId = Integer.parseInt(fileIdParam);
	    }
	    showFile(fileId, filesForEdit.get(fileId));
	}
    }

    /**
     * @param string
     * @param newContent
     * @throws IOException 
     * @throws ConfigurationException 
     */
    private void saveFile(final int fileId, final String file, final String newContent) throws IOException, ConfigurationException {
	final File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	// TODO: unescape content!
	Utils.saveToFile(fileToEdit, newContent);

	showFile(fileId, file);
//	SimpleTemplate template = new SimpleTemplate("/html/editConfigFiles/fileSaved.html");
//	template.setAttribute("url", this.getPath());
//	outputHtmlContent(template.renderTemplate());
    }

    /**
     * @param string
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ConfigurationException 
     */
    private void showFile(final int fileId, final String file) throws FileNotFoundException, IOException, ConfigurationException {
	final File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	if (!fileToEdit.exists()) {
	    throw new FileNotFoundException(String.format("Cannot find file %s", file));
	}

	final GameConfiguration gameConfiguration = new GameConfiguration(getConfig(), fileId);
	outputXmlContent(gameConfiguration.toXml());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/editConfigFiles";
    }
}