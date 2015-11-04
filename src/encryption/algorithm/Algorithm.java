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
public class Algorithm {

    private Configuration configuration;
    private String name;

    public Algorithm(Configuration configuration) {
        this.configuration = configuration;
    }
 
    
    /**
     * Encrypt the given string
     * 
     * @param input
     * @return Encrypted representation of the input according to configuration
     */
    
    public String Encrypt(String input) {
        return input;
    }
    
    /**
     * Decrypt the given string
     * 
     * @param input
     * @return Original string from the encrypted input
     */
    
    public String Decrypt(String input) {
        return input;
    }
    
    
    
    @Override
    public String toString() {
        return name;
    }
}
