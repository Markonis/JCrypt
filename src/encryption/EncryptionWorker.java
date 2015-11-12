/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */
public class EncryptionWorker extends Thread {
    private LinkedTransferQueue<File> queue;
    private boolean doWork;
    private Project project;
    
    public EncryptionWorker(Project project) {
        doWork = true;
        this.project = project;
        this.queue = new LinkedTransferQueue<File>();
    }
    
    public synchronized void addToQueue(Collection<File> files) {
        queue.addAll(files);
    }
    
    public void addToQueue(File file) {
        queue.add(file);
    }
    
    public void addDiff() {
        addToQueue(project.diffFolders());
    }
    
    public synchronized void stopWorking() {
        doWork = false;
    }

    @Override
    public void run() {
        while(doWork){
            File file;
            try {
                file = queue.take();
                project.encrypt(file);
            } catch (InterruptedException ex) {
                Logger.getLogger(EncryptionWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }   
}