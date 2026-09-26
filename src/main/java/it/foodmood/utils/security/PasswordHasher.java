package it.foodmood.utils.security;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import it.foodmood.exception.PasswordException;

public class PasswordHasher {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final String ALGORITM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT = 16;

    
    public String hash(char[] password){

        byte[] salt = new byte[SALT];
        SECURE_RANDOM.nextBytes(salt);
        PBEKeySpec specification = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS);

        try{
            byte[] hash = SecretKeyFactory.getInstance(ALGORITM).generateSecret(specification).getEncoded();

            // Ritorno "salt:hash" in base 64
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new PasswordException("Errore nel calcolo dell'hash della password", e);
        } finally {
            specification.clearPassword();
        }
    }

    public boolean verify(char[] password, String storedHash){
        PBEKeySpec specification = null;
        try {
            // estrazione salt e hash originale
            String[] parts = storedHash.split(":");
            if(parts.length != 2){
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] originalHash = Base64.getDecoder().decode(parts[1]);

            // ricalcolo hash con password fornita
            specification = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS);
            byte[] hash = SecretKeyFactory.getInstance(ALGORITM).generateSecret(specification).getEncoded();

            // confronto
            if(originalHash.length != hash.length) {
                return false;
            }
            int hashCompare = 0;
            for(int i = 0; i < originalHash.length; i++){
                hashCompare |= originalHash[i]^hash[i];
            }

            return hashCompare == 0;
            
        } catch (Exception _) {
            return false;
        } finally {
            if(specification != null){
                specification.clearPassword();
            }
        }
    }
}
