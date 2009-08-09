package de.eqc.srcds.configuration.datatypes;


public class Password {
    
    private final String value;
    
    
    public Password(String value) {

	this.value = value;
    }

    @Override
    public String toString() {
    
        return value.toString();
    }
    
    
    public static Password valueOf(String value) {

	return new Password(value);
    }
}
