package cluster.services.listener;

import cluster.file.FileTraverser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Creates and destroys file listeners
 *
 * Created by suzanne on 28/12/2016.
 */
@Service
public class SimpleDirectoryListenerService implements DirectoryListenerService {
    //TODO: create tests
    @Resource
    FileTransporterService fileTransporterService;

    static Logger log = Logger.getLogger("cluster");

    WatchService watcher;
    private Map<WatchKey,Path> keys;
    private Map<WatchKey,Long> fileSizes;
    private boolean trace = false;

    public SimpleDirectoryListenerService(){

        try{
            keys = new HashMap<>();
            fileSizes = new HashMap<>();
            watcher = FileSystems.getDefault().newWatchService();
            //processEvents();
        } catch(java.io.IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void createDirectoryListener(File directory){

        try{
            register(directory.toPath());
            trace = true;
            processEvents();
        } catch(java.io.IOException e){
            log.error("Unable to register a listener for directory " +
                    directory.toString());
        }

    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
        fileSizes.put(key, new Long(0));
    }

    /**
     * Process all events for keys queued to the watcher
     */
    private void processEvents() {

        while (true) {

            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                log.error("WatchKey not recognized");
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                //TODO: need to handle overflow
                if (kind == OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                String fileName = child.getFileName().toString();

                if(fileName == "output.log"){
                    log.info("output.log has been altered");
                    FileTraverser fileTraverser = new FileTraverser(fileName);
                    boolean calculationEnded = fileTraverser
                            .isCalculationEnded(fileSizes.get(key));
                    if(calculationEnded){
                        log.info("Calculation ended: "+ keys.get(key.toString()));

                        fileTransporterService.sendNewFiles(new File
                                (fileName), fileTraverser.getHasError());
                    }
                    Long newFileSize = fileTraverser.getFileSize();
                    fileSizes.put(key, newFileSize);
                }

            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }

        }

    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

}
