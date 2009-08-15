package de.eqc.srcds.enums;

import java.util.Locale;


public enum ImageType {

    JPG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    ICO("image/x-icon");
    
    private String mimeType;
    
    private ImageType(final String mimeType) {

	this.mimeType = mimeType;
    }    
    
    public String getMimeType() {

	return mimeType;
    }
    
    public static String getMimeTypeForImageFile(final String filename) {
	
	String mimeType = "image/jpeg";
	for (ImageType instance : values()) {
	    
	    if (filename.toUpperCase(Locale.getDefault()).endsWith(instance.name())) {
		mimeType = instance.getMimeType();
	    }
	}
	return mimeType;
    }
}
