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
    
    protected byte[] blockBuffer;
    protected int blockLength; // Size of block buffer in bytes
    
    public BlockAlgorithm(Configuration configuration){
        super(configuration);
        blockLength = Integer.parseInt(configuration.get("blockLength"));
        blockBuffer = new byte[blockLength];
    }
    
    public byte[] encryptBlock(byte[] in) {
        return in;
    }
    
    public byte[] decryptBlock(byte[] in) {
        return in;
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) {
        int read;
        try {
            int i = 0;
            
            while((read = in.read()) != -1){
                byte inputByte = (byte) read;
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    out.write(encryptBlock(blockBuffer));
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
        int read;
        try {
            int i = 0;
            
            while((read = in.read()) != -1){
                byte inputByte = (byte) read;
                blockBuffer[i++] = inputByte;
                if(i == blockLength){
                    i = 0;
                    out.write(decryptBlock(blockBuffer));
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
}
