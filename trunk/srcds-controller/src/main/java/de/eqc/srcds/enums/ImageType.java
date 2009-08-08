package de.eqc.srcds.enums;


public enum ImageType {

    JPG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif");
    
    private String mimeType;
    
    private ImageType(String mimeType) {

	this.mimeType = mimeType;
    }
    
    
    public String getMimeType() {

	return mimeType;
    }
    
    public static String getMimeTypeForImageFile(String filename) {
	
	String mimeType = "text/plain";
	for (ImageType instance : values()) {
	    
	    if (filename.toUpperCase().endsWith(instance.name())) {
		mimeType = instance.getMimeType();
	    }
	}
	return mimeType;
    }
}
