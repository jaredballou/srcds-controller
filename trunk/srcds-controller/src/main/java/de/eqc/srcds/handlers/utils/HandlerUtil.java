package de.eqc.srcds.handlers.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.eqc.srcds.handlers.RegisteredHandler;

public final class HandlerUtil {

    /** Hides the constructor of the utility class. */
    private HandlerUtil() {

	throw new UnsupportedOperationException();
    }

    /**
     * This method gets all classes in the same package as
     * RegisterHandlerByReflection which implements the
     * RegisterHandlerByReflection interface.
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    public static Collection<RegisteredHandler> getRegisteredHandlerImplementations()
	    throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedEncodingException {

	String pckgname = RegisteredHandler.class.getPackage().getName();
	Collection<RegisteredHandler> handlers = new ArrayList<RegisteredHandler>();
	Collection<String> classNames = null;
	try {
	    String path = '/' + pckgname.replace('.', '/');
	    URL resource = HandlerUtil.class.getResource(path);
	    if (resource == null) {
		throw new ClassNotFoundException("No resource for " + path);
	    }

	    classNames = findHandlerClassNames(resource.getPath().toString(),
		    pckgname);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ClassNotFoundException(pckgname + " (" + pckgname
		    + ") does not appear to be a valid package");
	}

	for (String className : classNames) {
	    Class<?> clazz = Class.forName(className);
	    if (!clazz.isInterface()
		    && !Modifier.isAbstract(clazz.getModifiers())
		    && RegisteredHandler.class.isAssignableFrom(clazz)) {

		handlers.add((RegisteredHandler) clazz.newInstance());
	    }
	}

	return handlers;
    }

    public static Collection<String> findHandlerClassNames(String path,
	    String packageName) throws URISyntaxException, IOException {

	Collection<String> classNames = new ArrayList<String>();

	if (path.contains(".jar!/")) {
	    packageName = packageName.replaceAll("\\.", "/");
	    path = path.split("!")[0];
	    FileInputStream fis = new FileInputStream(new File(new URI(path)));
	    JarInputStream jarFile = new JarInputStream(fis);
	    JarEntry jarEntry;
	    while ((jarEntry = jarFile.getNextJarEntry()) != null) {
		if ((jarEntry.getName().startsWith(packageName))
			&& (jarEntry.getName().endsWith(".class"))) {

		    String className = jarEntry.getName()
			    .replaceAll("/", "\\.").substring(0,
				    jarEntry.getName().length() - 6);
		    classNames.add(className);
		}
	    }
	} else {
	    File directory = new File(path);
	    if (directory.exists()) {
		// Get the list of the files contained in the package
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
		    // we are only interested in .class files
		    if (files[i].endsWith(".class")) {
			String className = packageName + '.'
				+ files[i].substring(0, files[i].length() - 6);
			classNames.add(className);
		    }
		}
	    }
	}

	return classNames;
    }

}