package de.eqc.srcds.configuration.datatypes;


public class Password {
    
    private final String value;
    
    
    public Password(final String value) {

	this.value = value;
    }

    @Override
    public String toString() {
    
        return value;
    }
    
    
    public static Password valueOf(final String value) {

	return new Password(value);
    }
}
