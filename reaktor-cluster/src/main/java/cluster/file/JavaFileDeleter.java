package cluster.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.Resource;

import cluster.file.FileDeleter;
import org.springframework.stereotype.Service;

/**
 * Deletes files and folders
 * 
 * @author suzanne
 *
 */
@Service
public class JavaFileDeleter implements FileDeleter {
	
	@Resource
	private File directory;
	
	/**
	 * Recursively deletes folder, taken straight from the java 8 API
	 * 
	 */
	public void deleteFolder(String folderName){
		Path directoryToDelete = Paths.get(directory.getPath()+"/"+folderName);
		try {
			Files.walkFileTree(directoryToDelete, new SimpleFileVisitor<Path>() {
			     @Override
			     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			         throws IOException
			     {
			         Files.delete(file);
			         return FileVisitResult.CONTINUE;
			     }
			     @Override
			     public FileVisitResult postVisitDirectory(Path dir, IOException e)
			         throws IOException
			     {
			         if (e == null) {
			             Files.delete(dir);
			             return FileVisitResult.CONTINUE;
			         } else {
			             // directory iteration failed
			             throw e;
			         }
			     }
			 });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
