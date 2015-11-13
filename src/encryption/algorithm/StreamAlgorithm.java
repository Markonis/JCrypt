/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author marko
 */
public interface StreamAlgorithm {
    
    public void encrypt(InputStream in, OutputStream out);
    public void decrypt(InputStream in, OutputStream out);
}
