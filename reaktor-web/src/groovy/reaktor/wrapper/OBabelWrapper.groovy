package reaktor.wrapper

import org.apache.log4j.Logger
import reaktor.error.NoObabelException

import javax.annotation.Resource

/**
 * Wrapper around obabel
 * 
 * @author suzanne
 *
 */
class OBabelWrapper implements Wrapper {

	Logger logger = Logger.getLogger("reaktor-web")

	@Resource
	File incomingFolder

	/**
	 * Runs obabel converter from smiles to outFile file type. Smiles strings
	 * containing more than one molecule will be written to only one file
	 * 
	 */
	@Override
	public File run(String smilesString, String outFile){
		String newInput
		newInput = "-:"+smilesString
		Process process = new ProcessBuilder(System.getenv("obabel"), newInput, "-O", outFile, "--gen3d")
			.directory(incomingFolder).start()
		process.inputStream.eachLine {println it}
		process.waitFor()
		if(process.exitValue()){
			String errorString = getErrorString(process)
			throw new RuntimeException(errorString)
		}
		return new File(incomingFolder, outFile)
	}

	/**
	 * Runs obabel converter from xyz file to smiles
	 *
	 * @param fileToConvert
	 * @return
	 */
	@Override
	public String run(File fileToConvert) throws NoObabelException{
		String smilesString
		String xyzName = fileToConvert.readLines()[1]?: "m1"
		logger.info("xyzName is: " + xyzName)
		Process process;
		try{
			String obabel = System.getenv("obabel")
			process = new ProcessBuilder(obabel,
					"-i${fileToConvert.name.tokenize('.')[-1]}", fileToConvert.name, "-osmi")
					.directory(incomingFolder).start()
		}catch(NullPointerException){
			throw new NoObabelException()
		}
		process.inputStream.eachLine{
			line -> println line
				if((line.contains(fileToConvert.name) && !line.contains("Cannot open"))
						|| (xyzName != "" && line.contains(xyzName))){
					smilesString = line.tokenize()[0]
					logger.info(smilesString)
				}
		}
		process.waitFor()
		if(process.exitValue()){
			String errorString = getErrorString(process)
			throw new RuntimeException(errorString)
		}
		return smilesString
	}

	/*
	 * Gets error text
	 * 
	 */
	private static String getErrorString(Process process) {
		BufferedInputStream error = new BufferedInputStream(process.getErrorStream())
		StringBuilder errorString = new StringBuilder()
		error.eachLine{errorString.append(it)}
		return (String) errorString
	}
}
