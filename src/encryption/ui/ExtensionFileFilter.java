/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.ui;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author marko
 */
public class ExtensionFileFilter implements FileFilter{
    private String goodExtension, badExtension;
    
    public ExtensionFileFilter(String acceptedExtension, String badExtension) {
        this.goodExtension = acceptedExtension;
        this.badExtension = badExtension;
    }
    
    @Override
    public boolean accept(File file) {
        boolean isAccessible = file.isFile() && file.canRead();
        if(isAccessible){
            return 
                (goodExtension == null || file.getName().endsWith(goodExtension)) &&
                (badExtension == null || !file.getName().endsWith(badExtension));
        }
        
        return false;
    }
}
