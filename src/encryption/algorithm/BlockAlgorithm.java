/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */
public class BlockAlgorithm extends StreamAlgorithm {
    
    protected int[] blockBuffer;
    protected int blockLength; // Size of block buffer in bytes
    
    public BlockAlgorithm(Configuration configuration){
        super(configuration);
        blockLength = Integer.parseInt(configuration.get("blockLength"));
        blockBuffer = new int[blockLength];
    }
    
    public int[] encryptBlock(int[] in) {
        return in;
    }
    
    public int[] decryptBlock(int[] in) {
        return in;
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) {
        int inputByte;
        try {
            int i = 0;
            while((inputByte = in.read()) != -1){
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    int[] encryptedBlock = encryptBlock(blockBuffer);
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

    @Override
    public void decrypt(InputStream in, OutputStream out) {
        int inputByte;
        try {
            int i = 0;
            while((inputByte = in.read()) != -1){
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    int[] decryptedBlock = decryptBlock(blockBuffer);
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
    
    public int implodeInt(int[] bytes){
        int result = 0;
        for(int i = 0; i < bytes.length; i++){
            int current = bytes[i];
            result += current << (i * 8);
        }
            
        return result;
    }
    
    public int[] explodeInt(int in, int count){
        int[] result = new int[count];
        long mask = 255;
        for(int i = 0; i < count; i++)
            result[i] = (int) ((in & (mask << (i * 8))) >> (i * 8));
        return result;
    }
}
