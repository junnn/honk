package bloop.honk;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by Jun Hao Ng on 21/10/2017.
 */

public class AuthController {

    public String hashPassword(final char[] password, final byte[] salt){
        final int interation = 1;
        final int keyLength = 256;
        try{
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, interation, keyLength );
            SecretKey key = skf.generateSecret( spec );

            return Base64.encodeToString(key.getEncoded(),Base64.DEFAULT);
        }
        catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
