
package Keyring;

import Exceptions.KeyringException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.LinkedList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    
    
    public static void encrypt(String fileName, String masterKey, LinkedList<Row> tableKeys) throws KeyringException{
        try {
            System.out.print("  -> File encryption in progress...");
            SecretKeySpec secretKey = transformKey(masterKey);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            SealedObject sealedObject = new SealedObject(tableKeys, cipher);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)), cipher);
            ObjectOutputStream outputStream = new ObjectOutputStream(cipherOutputStream);
            outputStream.writeObject(sealedObject);
            outputStream.close();
            System.out.println("Completed. ");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException  ex) {
            throw new KeyringException(ex.getMessage(), "Error", KeyringException.WARNING_MESSAGE);
        }
    }
    
  
    public static LinkedList<Row> dencrypt(String fileName, String masterKey) throws StreamCorruptedException, KeyringException{
        try {
            System.out.print("  -> Decrypting file... ");
            SecretKeySpec secretKey = transformKey(masterKey);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(fileName)), cipher);
            ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
            SealedObject sealedObject = (SealedObject) inputStream.readObject();
            LinkedList<Row> tableKeys = (LinkedList<Row>) sealedObject.getObject(cipher);
            inputStream.close();
            
            System.out.println("Completed. ");
            return tableKeys;
        }catch(StreamCorruptedException ex){
            throw new StreamCorruptedException(ex.getMessage());
        }catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException ex) {
           throw new KeyringException(ex.getMessage(), "Error", KeyringException.WARNING_MESSAGE);
        } 
    }    
    
    private static SecretKeySpec transformKey(String masterKey) throws NoSuchAlgorithmException, InvalidKeySpecException{        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(masterKey.getBytes());
                
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterKey.toCharArray(), md.digest(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
                
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }
}
