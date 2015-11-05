/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.watcher;

import encryption.Project;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */

public abstract class ProjectFolderWatcher extends Thread{
    private WatchService watchService;
    
    protected Project project;
    private boolean watch;
    
    public ProjectFolderWatcher(Project project) {
        this.project = project;
    }

    protected abstract File watchedFolder();
    protected abstract void handleEvent(String eventKind, String fileName);
    
    
    public void setWatch(boolean watch) {
        this.watch = watch;
    }
    
    
    @Override
    public void run() {
        File folder = watchedFolder();
        Path path;
        
        if(folder != null){
            path = Paths.get(folder.getAbsolutePath());

            try {
                watchService = FileSystems.getDefault().newWatchService();

                path.register(watchService, 
                        java.nio.file.StandardWatchEventKinds.ENTRY_DELETE, 
                        java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY,
                        java.nio.file.StandardWatchEventKinds.ENTRY_CREATE); 

                watch = true;

                // get the first event before looping
                WatchKey key = watchService.take();
                while(watch && key != null) {
                    for (WatchEvent event : key.pollEvents()) {
                        String eventKind = event.kind().toString();
                        String fileName = event.context().toString();

                        handleEvent(eventKind, fileName);

                        System.out.printf("Received %s event for file: %s\n",
                                          event.kind(), event.context() );
                    }

                    key.reset();
                    key = watchService.take();
                }
            } catch (Exception ex) {
                Logger.getLogger(ProjectFolderWatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Stopping folder watcher...");
    }
    
}
