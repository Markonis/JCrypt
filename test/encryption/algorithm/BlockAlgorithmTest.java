/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marko
 */
public class BlockAlgorithmTest {
    
    public BlockAlgorithmTest() {
    }
    
    @Test
    public void testImplodeExplodeLong() {
        System.out.println("implode/explode");
        
        Configuration config = new Configuration();
        config.set("blockLength", "8");
        BlockAlgorithm instance = new BlockAlgorithm(config);
        
        int[] bytes = new int[4];
        int imploded, imploded2;
        int[] exploded;
        
        for(int i = 0; i < 1024; i++){
            // Setup random bytes to implode
            for(int j = 0; j < bytes.length; j++)
                bytes[0] = (int) Math.floor(Math.random() * 256);
            
            imploded = instance.implodeInt(bytes);
            exploded = instance.explodeInt(imploded, 4);
            Assert.assertArrayEquals(bytes, exploded);
            
            imploded2 = instance.implodeInt(exploded);
            Assert.assertEquals(imploded, imploded2);
            
        }
    }
}