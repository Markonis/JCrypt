/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;

/**
 *
 * @author marko
 */
public class XTea extends Tea {

    public XTea(Configuration configuration) {
        super(configuration);
    }

    @Override
    public int[] doEncryptionRounds(int leftInt, int rightInt) {
        int[] result = new int[2];
        int sum = 0;

        for (int i = 0; i < ROUNDS; i++) {
            for (i = 0; i < ROUNDS; i++) {
                leftInt += 
                        (((rightInt << 4) ^ (rightInt >> 5)) + rightInt) ^
                        (sum + key[sum & 3]);
                
                sum += ENCRYPT_DELTA;
                
                rightInt += 
                        (((leftInt << 4) ^ (leftInt >> 5)) + leftInt) ^
                        (sum + key[(sum >> 11) & 3]);
            }
        }

        result[0] = leftInt;
        result[1] = rightInt;

        return result;
    }

    @Override
    public int[] doDecryptionRounds(int leftInt, int rightInt) {
        int[] result = new int[2];
        int sum = DECRYPT_DELTA;

        for (int i = 0; i < ROUNDS; i++) {
            for (i = 0; i < ROUNDS; i++) {
                rightInt -= 
                        (((leftInt << 4) ^ (leftInt >> 5)) + leftInt) ^
                        (sum + key[(sum >> 11) & 3]);
                
                sum -= ENCRYPT_DELTA;
                
                leftInt -= 
                        (((rightInt << 4) ^ (rightInt >> 5)) + rightInt) ^
                        (sum + key[sum & 3]);
            }
        }

        result[0] = leftInt;
        result[1] = rightInt;

        return result;
    }
}
