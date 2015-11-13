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

    protected Configuration configuration;
    private String name;

    public Algorithm(Configuration configuration) {
        this.configuration = configuration;
    }
 
    
    /**
     * encrypt the given string
     * 
     * @param input
     * @return Encrypted representation of the input according to configuration
     */
    
    public String encrypt(String input) {
        return input;
    }
    
    /**
     * decrypt the given string
     * 
     * @param input
     * @return Original string from the encrypted input
     */
    
    public String decrypt(String input) {
        return input;
    }
    
    
    
    @Override
    public String toString() {
        return name;
    }
}
