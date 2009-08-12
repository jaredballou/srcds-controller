package de.eqc.srcds.core;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class CryptoUtil {

    private final static String CRYPTO_KEY = "Aj17Ag%!&YJA!(.0";

    public enum Action {
	ENCRYPT, DECRYPT;
    }

    /** Hides the constructor of the utility class. */
    private CryptoUtil() {

	throw new UnsupportedOperationException();
    }

    public static String process(Action action, String text) {

	String ret = null;
	switch (action) {
	case ENCRYPT:
	    ret = encrypt(text);
	    break;
	case DECRYPT:
	    ret = decrypt(text);
	    break;
	}
	return ret;
    }

    public static String encrypt(String plain) {

	try {
	    SecretKey secret = new SecretKeySpec(CRYPTO_KEY.getBytes("UTF-8"),
		    "Blowfish");
	    Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, secret);
	    byte[] bytes = plain.getBytes("UTF-8");
	    return new BASE64Encoder().encode(cipher.doFinal(bytes));
	} catch (Exception e) {
	    throw new RuntimeException(String.format("Encryption failed: %s", e
		    .getLocalizedMessage()));
	}
    }

    public static String decrypt(String encoded) {

	try {
	    SecretKey secret = new SecretKeySpec(CRYPTO_KEY.getBytes("UTF-8"),
		    "Blowfish");
	    Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, secret);
	    byte[] bytes = new BASE64Decoder().decodeBuffer(encoded);
	    return new String(cipher.doFinal(bytes), "UTF-8");
	} catch (Exception e) {
	    throw new RuntimeException(String.format("Decryption failed: %s", e
		    .getLocalizedMessage()));
	}
    }
}