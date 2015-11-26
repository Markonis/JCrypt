/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm.blockMode;

import encryption.Configuration;
import encryption.algorithm.A51;
import encryption.algorithm.blockEncryptor.BlockEncryptor;
import encryption.algorithm.blockEncryptor.Tea;
import encryption.algorithm.blockEncryptor.XTea;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */
public class BlockMode {

    protected int[] blockBuffer;
    protected int blockLength; // Size of block buffer in bytes
    protected BlockEncryptor blockEncryptor;

    public BlockMode(Configuration configuration) {
        blockLength = Integer.parseInt(configuration.get("blockLength"));
        blockBuffer = new int[blockLength];
        
        String encryptor = configuration.get("encryptor");
        if(encryptor.equals("Tea")){
            this.blockEncryptor = new Tea(configuration);
        }else if(encryptor.equals("XTea")){
            this.blockEncryptor = new XTea(configuration);
        }else{
            this.blockEncryptor = new BlockEncryptor(configuration);
        }
    }
    
    public void encrypt(InputStream in, OutputStream out) {
        int inputByte;
        try {
            int i = 0;
            while((inputByte = in.read()) != -1){
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    int[] encryptedBlock = blockEncryptor.encryptBlock(blockBuffer);
                    for(int j = 0; j < blockLength; j++)
                        out.write(encryptedBlock[j]);
                }
            }
            
            if(i > 0){
                // Just copy the rest of bytes
                for(int j = 0; j < i; j++)
                    out.write(blockBuffer[j]);
            }

            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(A51.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void decrypt(InputStream in, OutputStream out) {
        int inputByte;
        try {
            int i = 0;
            while((inputByte = in.read()) != -1){
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    int[] decryptedBlock = blockEncryptor.decryptBlock(blockBuffer);
                    for(int j = 0; j < blockLength; j++)
                        out.write(decryptedBlock[j]);
                }
            }
            
            if(i > 0){
                // Just copy the rest of bytes
                for(int j = 0; j < i; j++)
                    out.write(blockBuffer[j]);
            }

            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(A51.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset() {
        
    }
}
