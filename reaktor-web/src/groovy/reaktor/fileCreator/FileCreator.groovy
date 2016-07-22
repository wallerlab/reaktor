package reaktor.fileCreator;

import reaktor.Reaction;

public interface FileCreator {
	
	public void createFile(fileName, filePath);
	
	public void createFilesFromMultipleFiles(Reaction reaction);

}
