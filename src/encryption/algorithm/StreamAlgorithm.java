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
public class StreamAlgorithm extends Algorithm{

    public StreamAlgorithm(Configuration configuration){
        super(configuration);
    }

    public byte encryptByte(byte in) {
        return in;
    }

    public void encrypt(InputStream in, OutputStream out) {
        int read;
        try {
            while((read = in.read()) != -1){
                byte inputByte = (byte) read;
                out.write((int) encryptByte(inputByte));
            }
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(A51.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decrypt(InputStream in, OutputStream out) {
        encrypt(in, out);
    }


    // Utility methods

    protected byte[] explodeByte(byte b) {
        byte[] result = new byte[8];
        for(int i= 0; i < 8; i++){
            result[i] = (byte) ((b & (1 << i)) >> i);
        }
        return result;
    }

    protected byte implodeByte(byte[] bits) {
        byte result = 0;
        for(int i = 0; i < 8; i++)
            result += bits[i] << i;
        return result;
    }

    public static void printBytes(byte[] bytes){
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
    }
}
