package reaktor.fileCreator

import reaktor.Reaction;
import reaktor.services.ProductCalculatorService

/**
 * Creates a "define.inp" file to be used by turbomole.
 * 
 * @author suzanne
 *
 */
class DefineFileCreator implements FileCreator {

	/**
	 * Creates turbomole define file in specified path
	 * 
	 */
	@Override
	public void createFile(fileName, filePath) {
		File defineFile = new File(filePath, "define.inp")
		defineFile.createNewFile()
		defineFile.withWriter(){out ->
			[
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine(" a coord"),
				out.writeLine(" sy c1 "),
				out.writeLine(" *"),
				out.writeLine(" no "),
				out.writeLine(" b all def2-SVP            "),
				out.writeLine(" *"),
				out.writeLine(" eht "),
				out.writeLine(" y    "),
				out.writeLine(" 0"),
				out.writeLine(" y    "),
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine("     "),
				out.writeLine(" dft"),
				out.writeLine("on "),
				out.writeLine(" func b-p"),
				out.writeLine(" grid m4"),
				out.writeLine(" q"),
				out.writeLine(" ri "),
				out.writeLine(" on "),
				out.writeLine(" m  "),
				out.writeLine("        1000"),
				out.writeLine(" q"),
				out.writeLine(" scf"),
				out.writeLine(" iter"),
				out.writeLine("        500"),
				out.writeLine(" conv"),
				out.writeLine("           6"),
				out.writeLine("     "),
				out.writeLine(" q")
			]
		}

	}

	@Override
	public void createFilesFromMultipleFiles(Reaction reaction) {
		// only for XyzFileCreator right now
	}

}
