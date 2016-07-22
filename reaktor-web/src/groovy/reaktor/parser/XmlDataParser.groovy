package reaktor.parser

/**
 * Parses XML data from file and creates Molecule and Atom instances
 * 
 * @author suzy
 *
 */
class XmlDataParser implements Parser {

	/**
	 * Parses XML data. Catches if source data isn't a file, then treats it as
	 * a string. Catches malformed files as well and gives them to
	 * xmlStringCreator, which can handle them
	 *
	 */
	@Override
	public Object parse(Object source) {
		
		Node data
		XmlParser parser = new XmlParser()
		boolean sourceIsFile = checkIsFile(source)
		if(sourceIsFile){
			File xmlFile = getFileToParse(source)
			data = parser.parse(xmlFile)
		} else {
			data = parser.parseText(source)
		}
		return data
		
	}

	/*
	 * Checks if source is xml file
	 * 
	 */
	private boolean checkIsFile(source) {
		
		if (source instanceof File || source.endsWith(".xml")){
			return true
		}
		return false
		
	}
	
	/*
	 * Opens file and checks if it's well-written. If it has no <molecules> tag,
	 * adds one so that it can be parsed.
	 * 
	 */
	private File getFileToParse(source){
		
		File xmlFile
		if (source instanceof File){
			xmlFile = source
		}else if (source.endsWith(".xml")){
			xmlFile = new File(source)
		}else{
			//throw new Exception("Incorrect file type")
		}
		def textArray = xmlFile.readLines()
		if (textArray[1].contains("molecule")){
			File parseableXmlFile = File.createTempFile("Tmp", ".xml")
			textArray.add(1,"<molecules>")
			textArray.add("</molecules>")
			textArray.each{line ->
				parseableXmlFile.append(line + "\n")
			}
			xmlFile = parseableXmlFile
		}
		return xmlFile
		
	}
	
}
