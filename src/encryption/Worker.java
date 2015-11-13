/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */
public class Worker extends Thread {
    private LinkedTransferQueue<Job> queue;
    private boolean doWork;
    private Project project;
    
    public Worker(Project project) {
        doWork = false;
        this.project = project;
        this.queue = new LinkedTransferQueue<Job>();
    }
    
    public synchronized void addToQueue(Collection<Job> jobs) {
        queue.addAll(jobs);
    }
    
    public void addToQueue(Job job) {
        queue.add(job);
    }
    
    public void addDiff() {
        List<File> diff = project.diffFolders();
        ArrayList<Job> jobs = new ArrayList<Job>();
        
        for(File file : diff)
            jobs.add(new Job(file, null));
        
        addToQueue(jobs);
    }
    
    public synchronized void stopWorking() {
        doWork = false;
    }
    
    public synchronized void startWorking() {
        if(!doWork){
            doWork = true;
            this.start();
        }
    }

    @Override
    public void run() {
        while(doWork){
            try {
                Job job;
                job = queue.take();
                if(job.getOutput() == null){
                    project.encrypt(job.getInput());
                }else{
                    project.decrypt(job.getInput(), job.getOutput());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }   
}