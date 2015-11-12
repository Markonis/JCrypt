/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.watcher;

import encryption.EncryptionWorker;
import encryption.Project;
import java.io.File;

/**
 *
 * @author marko
 */
public class InputFolderWatcher extends ProjectFolderWatcher{
    
    private EncryptionWorker worker;
    
    public InputFolderWatcher(Project project, EncryptionWorker worker) {
        super(project);
        this.worker = worker;
        worker.addToQueue(project.getInputFiles());
    }

    @Override
    protected File watchedFolder() {
        return project.getInputFolder();
    }

    @Override
    protected void handleEvent(String eventKind, String fileName) {
        if("ENTRY_CREATE".equals(eventKind) || "ENTRY_MODIFY".equals(eventKind)){
            File inputFile = new File(project.getInputFolder(), fileName);
            if(inputFile.exists()){
                worker.addToQueue(inputFile);
            }
        }
        project.refreshInputFiles();
    }
}
