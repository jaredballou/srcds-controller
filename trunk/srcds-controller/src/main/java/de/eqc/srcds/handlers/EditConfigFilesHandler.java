package de.eqc.srcds.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.handlers.utils.SimpleTemplate;


/**
 * @author Holger Cremer
 */
public class EditConfigFilesHandler extends AbstractRegisteredHandler {

    /*
     * @see de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws Exception {
	List<String> filesForEdit = getServerController().getGameType().getImplementation().getFilesForEdit();

	if (isPost()) {
	    String fileIdParam = getPostParameter("fileId");
	    String newContent = getPostParameter("content");
	    if (fileIdParam == null || newContent == null) {
		throw new IllegalArgumentException("fileId or content was null!");
	    }
	    saveFile(filesForEdit.get(Integer.parseInt(fileIdParam)), newContent);
	} else {
	    String fileIdParam = getParameter("fileId");
	    if (fileIdParam == null) {
		listFilesForEdit(filesForEdit);
	    } else {
		int fileId = Integer.parseInt(fileIdParam);
		showFile(fileId, filesForEdit.get(fileId));
	    }
	}
    }

    /**
     * @param string
     * @param newContent
     * @throws IOException 
     * @throws ConfigurationException 
     */
    private void saveFile(String file, String newContent) throws IOException, ConfigurationException {
	File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	// TODO: unescape content!
	
	Utils.saveToFile(fileToEdit, newContent);
	
	SimpleTemplate template = new SimpleTemplate("/html/editConfigFiles/fileSaved.html");
	template.setAttribute("url", this.getPath());
	outputHtmlContent(template.renderTemplate());
    }

    /**
     * @param string
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ConfigurationException 
     */
    private void showFile(int fileId, String file) throws FileNotFoundException, IOException, ConfigurationException {
	File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	String content = Utils.getFileContent(fileToEdit);
	// TODO: escape the content!
	
	SimpleTemplate template = new SimpleTemplate("/html/editConfigFiles/showFile.html");
	template.setAttribute("fileId", String.valueOf(fileId));
	template.setAttribute("content", content);
	
	outputHtmlContent(template.renderTemplate());
    }

    /**
     * @param filesForEdit
     * @throws IOException 
     */
    private void listFilesForEdit(List<String> filesForEdit) throws IOException {
	StringBuilder builder = new StringBuilder();
	builder.append("<html><body><ul>");
	for (int i=0; i<filesForEdit.size(); i++) {
	    builder.append("<li><a href='?fileId=").append(i).append("'>").append(filesForEdit.get(i)).append("</li>");
	}
	builder.append("</ul></body></html>");
	outputHtmlContent(builder.toString());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/editConfigFiles";
    }
}
