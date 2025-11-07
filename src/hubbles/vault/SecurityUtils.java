/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author dreel
 */
public class SecurityUtils {
    private static final SecureRandom RNG = new SecureRandom();
    
    private SecurityUtils(){}
    
    public static byte[] randomBytes(int n){
        byte[] b = new byte[n];
        RNG.nextBytes(b);
        return b;
    }
    
    public static boolean constantTimeEquals(byte[] a, byte[] b){
        return MessageDigest.isEqual(a,b);
    }
    // pbkdf2 = Password-Based Key Deriviation Function
    public static byte[] pbkdf(char[] password, byte[] salt, int iterations, int keyBits) throws Exception{
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyBits);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return(skf.generateSecret(spec).getEncoded());
    }
    
    public static String b64(byte[] raw){
        return Base64.getEncoder().encodeToString(raw);
    }
    
    public static byte[] deb64(String s) {
        return Base64.getDecoder().decode(s);
    }
}
