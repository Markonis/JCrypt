/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.algorithm.blockEncryptor.XTea;
import encryption.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marko
 */
public class XTeaTest{
    
    @Test 
    public void testEncryptDecryptBlock() {
        Configuration config = new Configuration();
        config.set("blockLength", "8");
        config.set("key", "111 222 333 444");
        XTea instance = new XTea(config);
        
        for(int i = 0; i < 1024; i++){
            int[] inputBlock = new int[8];
            for(int j = 0; j < 8; j++)
                inputBlock[j] = (int) Math.floor(Math.random() * 256);
            
            int[] encryptedBlock = instance.encryptBlock(inputBlock);
            int[] decryptedBlock = instance.decryptBlock(encryptedBlock);
            
//            System.out.println("***********************");
//            StreamAlgorithm.printBytes(inputBlock);
//            StreamAlgorithm.printBytes(encryptedBlock);
//            StreamAlgorithm.printBytes(decryptedBlock);
//            System.out.println("***********************");
            
            Assert.assertArrayEquals(inputBlock, decryptedBlock);
        }
    }
    
    @Test
    public void testEncryptDecryptStream() {
        Configuration config = new Configuration();
        config.set("blockLength", "8");
        config.set("key", "111 222 333 444");
        config.set("mode", "default");
        config.set("encryptor", "XTea");
        
        BlockAlgorithm instance = new BlockAlgorithm(config);
        
        for(int i = 0; i < 128; i++){
            byte[] inputBytes = generateRandomBytes(1028);
            
            ByteArrayInputStream in =  new ByteArrayInputStream(inputBytes);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            instance.encrypt(in, out);
            byte[] encryptedBytes = out.toByteArray();
            
            in = new ByteArrayInputStream(encryptedBytes);
            out = new ByteArrayOutputStream();
            
            instance.decrypt(in, out);
            byte[] decryptedBytes = out.toByteArray();
            
            Assert.assertArrayEquals(inputBytes, decryptedBytes);
        }
    }
    
    private byte[] generateRandomBytes(int length){
        byte[] result = new byte[length];
        for(int j = 0; j < length; j++)
            result[j] = (byte) Math.floor(Math.random() * 128);
        
        return result;
    }
}