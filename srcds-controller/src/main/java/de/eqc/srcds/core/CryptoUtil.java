/**
 * This file is part of the Source Dedicated Server Controller project.
 * It is distributed under GPL 3 license.
 *
 * The srcds-controller is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the GNU General Public License
 * along with the srcds-controller. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://www.earthquake-clan.de/srcds/>
 *    <http://code.google.com/p/srcds-controller/>
 */
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