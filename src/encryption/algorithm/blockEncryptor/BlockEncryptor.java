/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm.blockEncryptor;

import encryption.Configuration;

/**
 *
 * @author marko
 */
public class BlockEncryptor {

    protected int blockLength;
    public BlockEncryptor(Configuration configuration) {
        blockLength = Integer.parseInt(configuration.get("blockLength"));
    }
    
    public int[] encryptBlock(int[] in) {
        return in;
    }
    
    public int[] decryptBlock(int[] in) {
        return in;
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
