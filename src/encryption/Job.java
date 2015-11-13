/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.File;

/**
 *
 * @author marko
 */
public class Job {
    private File input, output;
    
    public Job(File input, File output){
        this.input = input;
        this.output = output;
    }

    public File getInput() {
        return input;
    }

    public File getOutput() {
        return output;
    }
}
