/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.watcher;

import encryption.Project;
import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author marko
 */
public class OutputFolderWatcher extends ProjectFolderWatcher{
    
    public OutputFolderWatcher(Project project){
       super(project);
    }

    @Override
    public void registerEvents(Path path) throws Exception {
        path.register(watchService, 
                        java.nio.file.StandardWatchEventKinds.ENTRY_DELETE, 
                        java.nio.file.StandardWatchEventKinds.ENTRY_CREATE); 
    }

    
    
    @Override
    protected File watchedFolder() {
        return project.getOutputFolder();
    }

    @Override
    protected void handleEvent(String eventKind, String fileName) {
        project.refreshOutputFiles();
    }
}
