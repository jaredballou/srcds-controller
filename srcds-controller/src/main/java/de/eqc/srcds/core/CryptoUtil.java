package de.eqc.srcds.core;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import de.eqc.srcds.exceptions.CryptoException;

public final class CryptoUtil {

    private static final String CHARSET = "UTF-8";
    private final static String CRYPTO_KEY = "Aj17Ag%!&YJA!(.0";

    public enum Action {
	ENCRYPT, DECRYPT;
    }

    /** Hides the constructor of the utility class. */
    private CryptoUtil() {

	throw new UnsupportedOperationException();
    }

    public static String process(final Action action, final String text) throws CryptoException {

	String ret = null;
	if (action == Action.ENCRYPT) {
	    ret = encrypt(text);
	} else if (action == Action.DECRYPT) {
	    ret = decrypt(text);
	}
	return ret;
    }

    public static String encrypt(final String plain) throws CryptoException {

	try {
	    final SecretKey secret = new SecretKeySpec(CRYPTO_KEY.getBytes(CHARSET),
		    "Blowfish");
	    final Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, secret);
	    final byte[] bytes = plain.getBytes(CHARSET);
	    return new BASE64Encoder().encode(cipher.doFinal(bytes));
	} catch (Exception e) {
	    throw new CryptoException(String.format("Encryption failed: %s", e
		    .getLocalizedMessage()), e);
	}
    }

    public static String decrypt(final String encoded) throws CryptoException {

	try {
	    final SecretKey secret = new SecretKeySpec(CRYPTO_KEY.getBytes(CHARSET),
		    "Blowfish");
	    final Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, secret);
	    final byte[] bytes = new BASE64Decoder().decodeBuffer(encoded);
	    return new String(cipher.doFinal(bytes), CHARSET);
	} catch (Exception e) {
	    throw new CryptoException(String.format("Decryption failed: %s", e
		    .getLocalizedMessage()), e);
	}
    }
}