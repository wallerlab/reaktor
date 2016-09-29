package reaktor.fileCreator

import java.nio.file.*
import java.text.DecimalFormat
import java.util.ArrayList;
import java.util.stream.Stream
import reaktor.services.ListenerService
import reaktor.Molecule
import reaktor.Reaction
import reaktor.services.ProductCalculatorService
import javax.annotation.Resource


/**
 * Creates xyz files
 * 
 * @author suzanne
 *
 */
class XyzFileCreator implements FileCreator {
	
	@Resource
	private def defaultFolder
	
	@Resource
	private def mainFolder
	
	private int numFilesToMake = 2 //pyreactor can only handle 2 files right now

	/**
	 * Creates xyz file from given molecule in
	 * productCalculatorService filePath.
	 * 
	 */
	@Override
	public void createFile(molecule, filePath) {
		
		if (molecule instanceof Molecule){
			File xyzFile = new File(filePath, "${molecule.name}.xyz")
			xyzFile.createNewFile()
			DecimalFormat decim = new DecimalFormat("0.00000")
			xyzFile.withWriter(){ out ->
				[
					out.writeLine(molecule.atoms.size().toString()),
					out.writeLine(molecule.name),
					molecule.atoms.each{ atom ->
						def xCoord, yCoord, zCoord
						if (atom.xCoord >= 0 && atom.xCoord < 10){
							xCoord = " ${decim.format(atom.xCoord)}"
						} else xCoord = "${decim.format(atom.xCoord)}"
						if (atom.yCoord >= 0 && atom.yCoord < 10){
							yCoord = " ${decim.format(atom.yCoord)}"
						} else yCoord = "${decim.format(atom.yCoord)}"
						if (atom.zCoord >= 0 && atom.zCoord < 10){
							zCoord = " ${decim.format(atom.zCoord)}"
						} else zCoord = "${decim.format(atom.zCoord)}"
						out.writeLine("${atom.element}     ${xCoord}     ${yCoord}     ${zCoord}")
					}
				]
			}
		}
		
	}

	/**
	 * Merges more than two molecule xyz files into numFilesToMake for calculation.
	 * 
	 * @param number of xyz files to make into numFilesToMake files 
	 */
	@Override
	public void createFilesFromMultipleFiles(Reaction reaction){
		
		for(int j = 0; j < numFilesToMake; j++){
			ArrayList startAndEndNums = assignStartAndEndNums(j, reaction.reactants.size())
			ArrayList molecules = new ArrayList(reaction.reactants)
			ArrayList startMolsJArray = createCombinedFileArray(molecules, defaultFolder, startAndEndNums)
			File startMolsJ = new File(defaultFolder, "startMols${j}.xyz")
			startMolsJ.createNewFile()
			startMolsJ.withWriter(){ out ->
				startMolsJArray.each{ line ->
					out.writeLine(line)
				}
			}
		}
		
	}

	/**
	 * Creates an xyz file with spacing to be used in the jquery tables
	 * 
	 * @param reaction
	 * @return file to be displayed
	 */
	public File createXyzDisplayFile(Reaction reaction){
		
		File folder = new File(mainFolder, "ProductData_${reaction.id.toString()}/input_files")
		File displayFile = new File(folder.parent, "displayFile.xyz")
		if(displayFile.exists()){
			return displayFile
		}
		displayFile.createNewFile()
		ArrayList molecules = new ArrayList(reaction.molecules)
		ArrayList fileArray = createCombinedFileArray(molecules, folder, [0,reaction.molecules.size()])
		displayFile.withWriter(){ out ->
			fileArray.each{ line ->
				out.writeLine(line)
			}
		}
		return displayFile
		
	}
	
	/*
	 * Assigns start and end numbers of files to be merged for
	 * createFilesFromMultipleFiles
	 *
	 */
	private List assignStartAndEndNums(int index, int numXyzFiles){
		
		int dividedXyzFiles = numXyzFiles/numFilesToMake
		int remainder = numXyzFiles%numFilesToMake
		int numFilesToAddTo = numFilesToMake - remainder
		int startNum, endNum
		if (remainder == 0 || index < numFilesToAddTo){
			startNum = dividedXyzFiles*index
			endNum = dividedXyzFiles*(index+1)
		}
		else{
			startNum = (dividedXyzFiles*index)+(index-numFilesToAddTo)
			endNum = dividedXyzFiles*(index+1)+1+(index-numFilesToAddTo)
		}
		return new ArrayList([startNum, endNum])
		
	}
	
	/*
	 * Combines lines from multiple xyz files into a List, with spacing between
	 * molecules
	 *
	 */
	private ArrayList createCombinedFileArray(List molecules, File folder, List startAndEndNums) {
		
		ArrayList fileArray = ["", ""]
		int startNum = startAndEndNums[0]
		int endNum = startAndEndNums[1]
		int moleculeCount = 0
		Double maxXVal = findMaxOrMin(false, new File(folder, "${molecules[0].name}.xyz"), 1)
		for(int k = startNum; k < endNum; k++){
			File moleculeFile = new File(folder, "${molecules[k].name}.xyz")
			Double minXVal = findMaxOrMin(false, moleculeFile, 1)
			Double transVal = maxXVal - minXVal + 0.5
			maxXVal = findMaxOrMin(true, moleculeFile, 1)
			moleculeFile.eachLine {line, lineNumber ->
				if(lineNumber == 1){
					moleculeCount += Integer.parseInt(line.trim())
				}
				else if(lineNumber > 2){
					if(transVal > 0.5){
						DecimalFormat decim = new DecimalFormat("0.00000")
						ArrayList lineSplit = Arrays.asList(line.split("    "))
						lineSplit.removeAll(Arrays.asList("", " "))
						Double newXVal = Double.parseDouble(lineSplit[1]
							.trim()) + transVal
						lineSplit[1] = decim.format(newXVal).toString()
						if(newXVal > maxXVal){
							maxXVal = newXVal
						}
						line = lineSplit.join("    ")
					}
					fileArray << line.trim()
				}
			}
		}
		fileArray[0] = moleculeCount.toString()
		return fileArray
		
	}
	
	/*
	 * Finds either max or min value in an xyz file, depending on whether "max" is true
	 *
	 */
	private Double findMaxOrMin(boolean max, File moleculeFile, int coord){
		
		Double xVal = 0
		Path path = Paths.get(moleculeFile.getPath())
		try{
			Stream<String> lines = Files.lines(path)
			def xVals = lines.skip(2).mapToDouble({line ->
					ArrayList al = Arrays.asList(line.split("    "))
					al.removeAll(Arrays.asList("", " "))
					Double.parseDouble(al[coord].trim())
				})
			if(max){
				xVal = xVals.max().getAsDouble()
			}
			else{
				xVal = xVals.min().getAsDouble()
			}
		}catch(IOException e){
			System.out.println("IOerror");
		}
		return xVal
		
	}
	
}
