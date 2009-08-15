package de.eqc.srcds.handlers.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.handlers.CssHandler;
import de.eqc.srcds.handlers.ImageHandler;

/**
 * @author Holger Cremer
 */
public class SimpleTemplate {
    private final Map<String, String> attribute = new HashMap<String, String>();
    private final URL template;
    
    private final static Pattern IMG_TAG_PATTERN = Pattern.compile("\\$\\{img:([\\w\\.\\-]*)\\}"); 
    private final static Pattern CSS_TAG_PATTERN = Pattern.compile("\\$\\{css:([\\w\\.\\-]*)\\}"); 
    
    /**
     * @param templatePath
     * @throws FileNotFoundException 
     * @throws IOException 
     */
    public SimpleTemplate(final String templatePath) throws FileNotFoundException {

	this.template = getClass().getResource(templatePath);
	if (this.template == null) {
	    throw new FileNotFoundException(String.format("Cannot find the template '%s'", templatePath));
	}
    }    
    
    public void setAttribute(final String key, final String value) {

	this.attribute.put(key, value);
    }
    
    public String renderTemplate() throws IOException {

	String templateContent = Utils.getUrlContent(this.template);

	// add the images
	Matcher matcher = IMG_TAG_PATTERN.matcher(templateContent);
	while (matcher.find()) {
	    final MatchResult result = matcher.toMatchResult();
	    final String imageUrl = ImageHandler.HANDLER_PATH + "?name=" + result.group(1);
	    templateContent = templateContent.replace(result.group(0), imageUrl);
	}

	// add the stylesheets
	matcher = CSS_TAG_PATTERN.matcher(templateContent);
	while (matcher.find()) {
	    final MatchResult result = matcher.toMatchResult();
	    final String imageUrl = CssHandler.HANDLER_PATH + "?name=" + result.group(1);
	    templateContent = templateContent.replace(result.group(0), imageUrl);
	}	
	
	// add the attribute into the template
	for (Entry<String, String> entry : this.attribute.entrySet()) {
	    templateContent = templateContent.replace("${" + entry.getKey() + "}", entry.getValue());
	}
	return templateContent;
    }
}
