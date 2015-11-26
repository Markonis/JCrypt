/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import encryption.algorithm.blockMode.BlockMode;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author marko
 */
public class BlockAlgorithm extends StreamAlgorithm {
   
    protected BlockMode blockMode;
    
    public BlockAlgorithm(Configuration configuration){
        super(configuration);

        // Create block mode based on configuration
        if("PCBC".equals(configuration.get("mode"))){
            this.blockMode = new BlockMode(configuration);
        }else{
            this.blockMode = new BlockMode(configuration);
        }
    }
    
   
    @Override
    public void encrypt(InputStream in, OutputStream out) {
        blockMode.reset();
        blockMode.encrypt(in, out);
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) {
        blockMode.reset();
        blockMode.decrypt(in, out);
    }
}
