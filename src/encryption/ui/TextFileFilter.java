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
public class TextFileFilter implements FileFilter{
    private String extension;
    
    public TextFileFilter(String extension) {
        this.extension = extension;
    }
    
    @Override
    public boolean accept(File file) {
        return file.isFile() && file.canRead() && file.getName().endsWith(extension);
    }
}
