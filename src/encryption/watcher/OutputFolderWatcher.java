/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.watcher;

import encryption.Project;
import encryption.watcher.ProjectFolderWatcher;
import java.io.File;

/**
 *
 * @author marko
 */
public class OutputFolderWatcher extends ProjectFolderWatcher{
    
    public OutputFolderWatcher(Project project){
       super(project);
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
