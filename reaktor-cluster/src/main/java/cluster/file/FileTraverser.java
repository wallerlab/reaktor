package cluster.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * Looks through files
 *
 * Created by suzanne on 28/12/2016.
 */
public class FileTraverser {

    @Value("${cluster.ft.success}")
    private String successString;

    @Value("${cluster.ft.error}")
    private String errorString;

    private Logger log = Logger.getLogger("cluster");
    private RandomAccessFile raf;
    private boolean hasError;

    /**
     * Opens file with name fileName
     *
     * @param fileName name of file to be opened
     */
    public FileTraverser(String fileName){

        try{
            raf = new RandomAccessFile(fileName, "r");
        } catch(FileNotFoundException e){
            log.error("File " + fileName + " not found");
        }

    }

    /**
     * Looks at file from the last point checked (oldFileSize) to determine
     * if the calculation has ended yet
     *
     * @param oldFileSize last point in file checked
     * @return whether calculation has ended
     */
    public boolean isCalculationEnded(Long oldFileSize){

        try{
            raf.seek(oldFileSize);
            String line = raf.readLine();
            while(line != null){
                if(line.contains(successString)){
                    hasError = false;
                    return true;
                }else if(line.contains(errorString)) {
                    hasError = true;
                    return true;
                }
                line = raf.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Gets the size of the file in bytes
     *
     * @return Long of file size in bytes
     */
    public Long getFileSize(){

        try{
            return raf.length();
        }catch(IOException e){
            log.error("File " + raf.toString() + ": length not found");
        }
        return new Long(-1);

    }

    /**
     * Returns true or false depending on whether the output log indicates
     * whether there was an error with the calculation
     *
     * @return if the calculation ended with an error
     */
    public boolean getHasError(){
        return hasError;
    }
}
