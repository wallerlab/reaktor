package reaktor.wrapper

import reaktor.services.ListenerService
import javax.annotation.Resource

/**
 * Wrapper around obabel
 * 
 * @author suzanne
 *
 */
class OBabelWrapper implements Wrapper {
	
	@Resource
	File defaultFolder

	/**
	 * Runs obabel converter from smiles to xyz
	 * 
	 */
	@Override
	public void run(Object input, Object output){
		(String) input;
		(String) output
		String newInput
		if (!input.contains(".") || input.contains(".[")){
			newInput = "-:"+input
		} else {
			newInput = input
		}
		Process process = new ProcessBuilder(System.getenv("obabel"), newInput, "-O", output, "--gen3d")
			.directory(defaultFolder).start()
		process.inputStream.eachLine {println it}
		process.waitFor()
		if(process.exitValue()){
			String errorString = getErrorString(process)
			throw new RuntimeException(errorString)
		}
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
