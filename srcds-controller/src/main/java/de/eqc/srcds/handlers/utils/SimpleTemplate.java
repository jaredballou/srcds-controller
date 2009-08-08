package de.eqc.srcds.handlers.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.eqc.srcds.core.Utils;

/**
 * @author Holger Cremer
 */
public class SimpleTemplate {
    private Map<String, String> attribute = new HashMap<String, String>();
    private URL  template;
    
    /**
     * @param templatePath
     * @throws FileNotFoundException 
     * @throws IOException 
     */
    public SimpleTemplate(String templatePath) throws FileNotFoundException {
	this.template = getClass().getResource(templatePath);
	if (this.template == null) {
	    throw new FileNotFoundException(String.format("Cannot find the template '%s'", templatePath));
	}
    }
    
    
    public void setAttribute(String key, String value) {
	this.attribute.put(key, value);
    }
    
    public String renderTemplate() throws IOException {
	String templateContent = Utils.getUrlContent(this.template);
	
	for (Entry<String, String> entry : this.attribute.entrySet()) {
	    templateContent = templateContent.replace("${" + entry.getKey() + "}", entry.getValue());
	}
	return templateContent;
    }
}
