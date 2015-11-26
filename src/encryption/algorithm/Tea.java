/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import static encryption.algorithm.StreamAlgorithm.printBytes;

/**
 *
 * @author marko576460725758780550
 */
public class Tea extends BlockAlgorithm {
    
    int[] key;
    final int ENCRYPT_DELTA = 0x9E3779B9;
    final int DECRYPT_DELTA = 0xC6EF3720;
    final int ROUNDS = 32;
    
    public Tea(Configuration configuration) {
        super(configuration);
        this.key = configuration.parseIndexArray("key");
    }
    
    public int[] doEncryptionRounds(int leftInt, int rightInt) {
        int[] result = new int[2];
        int sum = 0;
        
        for(int i = 0; i < ROUNDS; i++){
            sum += ENCRYPT_DELTA;
            leftInt += 
                    ((rightInt << 4) + key[0]) ^
                    (rightInt + sum) ^
                    ((rightInt >>> 5) + key[1]);
            rightInt +=
                    ((leftInt << 4) + key[2]) ^
                    (leftInt + sum) ^
                    ((leftInt >>> 5) + key[3]);
        }
        
        result[0] = leftInt;
        result[1] = rightInt;
        
        return result;
    }
    
    public int[] doDecryptionRounds(int leftInt, int rightInt) {
        int[] result = new int[2];
        int sum = DECRYPT_DELTA;
        
        for(int i = 0; i < ROUNDS; i++){
            rightInt -=
                    ((leftInt << 4) + key[2]) ^
                    (leftInt + sum) ^
                    ((leftInt >>> 5) + key[3]);
            
            leftInt -= 
                    ((rightInt << 4) + key[0]) ^
                    (rightInt + sum) ^
                    ((rightInt >>> 5) + key[1]);

            sum -= ENCRYPT_DELTA;
        }
        
        result[0] = leftInt;
        result[1] = rightInt;
        
        return result;
    }
    
    @Override
    public int[] encryptBlock(int[] in) {
        int[] left = new int[blockLength / 2];
        int[] right = new int[blockLength / 2];
        int[] out = new int[blockLength];;
            
        System.arraycopy(in, 0, left, 0, blockLength / 2);
        System.arraycopy(in, blockLength / 2, right, 0, blockLength / 2);
        
        int[] encrypted = doEncryptionRounds(implodeInt(left), implodeInt(right));
        
        left = explodeInt(encrypted[0], blockLength / 2);
        right = explodeInt(encrypted[1], blockLength / 2);
        
        System.arraycopy(left, 0, out, 0, blockLength / 2);
        System.arraycopy(right, 0, out, blockLength / 2, blockLength / 2);
        
        return out;
    }

    @Override
    public int[] decryptBlock(int[] in) {
        int[] left = new int[blockLength / 2];
        int[] right = new int[blockLength / 2];
        int[] out = new int[blockLength];
        
        System.arraycopy(in, 0, left, 0, blockLength / 2);
        System.arraycopy(in, blockLength / 2, right, 0, blockLength / 2);
        
        int[] decrypted = doDecryptionRounds(implodeInt(left), implodeInt(right));
        
        left = explodeInt(decrypted[0], blockLength / 2);
        right = explodeInt(decrypted[1], blockLength / 2);
        
        System.arraycopy(left, 0, out, 0, blockLength / 2);
        System.arraycopy(right, 0, out, blockLength / 2, blockLength / 2);
        
        return out;
    }
}
