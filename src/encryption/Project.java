/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import encryption.algorithm.Algorithm;
import encryption.ui.TextFileFilter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */

public class Project implements Serializable {
    
    private Algorithm algorithm;
    
    
    public Project() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    private File outputFolder;
    public static final String PROP_OUTPUTFOLDER = "outputFolder";

    /**
     * Get the value of outputFolder
     *
     * @return the value of outputFolder
     */
    public File getOutputFolder() {
        return outputFolder;
    }

    /**
     * Set the value of outputFolder
     *
     * @param outputFolder new value of outputFolder
     */
    public void setOutputFolder(File outputFolder) {
        File oldOutputFolder = this.outputFolder;
        this.outputFolder = outputFolder;
        //configuration.set("outputFolder", outputFolder.getAbsolutePath());
        propertyChangeSupport.firePropertyChange(PROP_OUTPUTFOLDER, oldOutputFolder, outputFolder);
        refreshFiles();
    }
    
    public synchronized void refreshOutputFiles(){
        if(outputFolder != null){
            File[] filesInFolder = outputFolder.listFiles(new TextFileFilter(".jcrypt"));
            setOutputFiles(Arrays.asList(filesInFolder));
        }
    }
    
    private File inputFolder;
    public static final String PROP_INPUTFOLDER = "inputFolder";

    /**
     * Get the value of inputFolder
     *
     * @return the value of inputFolder
     */
    public File getInputFolder() {
        return inputFolder;
    }

    /**
     * Set the value of inputFolder
     *
     * @param inputFolder new value of inputFolder
     */
    public void setInputFolder(File inputFolder) {
        File oldInputFolder = this.inputFolder;
        this.inputFolder = inputFolder;
        //configuration.set("inputFolder", inputFolder.getAbsolutePath());
        propertyChangeSupport.firePropertyChange(PROP_INPUTFOLDER, oldInputFolder, inputFolder);
        refreshFiles();
    }
    
    public synchronized void refreshInputFiles(){
        if(inputFolder != null){
            File[] filesInFolder = inputFolder.listFiles(new TextFileFilter(".txt"));
            //configuration.set("inputFiles", createFilesListString(filesInFolder));
            setInputFiles(Arrays.asList(filesInFolder));
        }
    }
    
    private List inputFiles;
    public static final String PROP_INPUTFILES = "inputFiles";

    /**
     * Get the value of inputFiles
     *
     * @return the value of inputFiles
     */
    public List getInputFiles() {
        return inputFiles;
    }

    /**
     * Set the value of inputFiles
     *
     * @param inputFiles new value of inputFiles
     */
    public void setInputFiles(List inputFiles) {
        List oldInputFiles = this.inputFiles;
        this.inputFiles = inputFiles;
        propertyChangeSupport.firePropertyChange(PROP_INPUTFILES, oldInputFiles, inputFiles);
    }
    
    
    private List outputFiles;
    public static final String PROP_OUTPUTFILES = "outputFiles";

    /**
     * Get the value of outputFiles
     *
     * @return the value of outputFiles
     */
    public List getOutputFiles() {
        return outputFiles;
    }

    /**
     * Set the value of outputFiles
     *
     * @param outputFiles new value of outputFiles
     */
    public void setOutputFiles(List outputFiles) {
        List oldOutputFiles = this.outputFiles;
        this.outputFiles = outputFiles;
        propertyChangeSupport.firePropertyChange(PROP_OUTPUTFILES, oldOutputFiles, outputFiles);
    }
    

    /**
     * Set the value of algorithm
     *
     * @param algorithm new value of algorithm
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        cleanOutputFolder();
        refreshFiles();
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
    
    public void encrypt(File file) {
        if(algorithm != null){
            File output = new File(outputFolder, file.getName() + "jcrypt");
            try {
                FileWriter fr = new FileWriter(output);
                fr.write(algorithm.encrypt(readFile(file)));
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String decrypt(File encryptedFile, File folder) {
        if(algorithm != null){
            String originalName = originalName(encryptedFile.getName());
            File outputFile = new File(folder, originalName);
            try {
                FileWriter fr = new FileWriter(outputFile);
                fr.write(algorithm.decrypt(readFile(encryptedFile)));
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    
    public List<File> diffFolders() {
        List<File> diff = new ArrayList<File>();
        if(inputFolder != null && outputFolder != null){
            refreshFiles();
            
            for(int i = 0; i < inputFiles.size(); i++){
                File inputFile = (File) inputFiles.get(i);
                
                File outputFile = new File(outputFolder, inputFile.getName() + ".jcrypt");
                
                if(!outputFiles.contains(outputFile)){
                    diff.add(inputFile);
                }
            }
        }
        return diff;
    }
    
    private void cleanOutputFolder() {
        refreshOutputFiles();
        for(int i = 0; i < outputFiles.size(); i++){
            File file = (File) outputFiles.get(i);
            try {
                Files.delete(file.toPath());
            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String originalName(String name){
        return name.substring(0, name.length() - 6);
    }
    
    public String readFile(File file){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            
        }
        
        return sb.toString();
    }
    
    public void writeFile(File file, String content){
        
    }
    
    private void refreshFiles() {
        refreshInputFiles();
        refreshOutputFiles();
    }
    
    private transient final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
