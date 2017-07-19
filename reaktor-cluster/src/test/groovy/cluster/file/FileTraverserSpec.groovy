package cluster.file

import org.apache.log4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by suzanne on 29/12/2016.
 */
class FileTraverserSpec extends Specification {
    static String directoryName = "src/test/Test_Folder/logs"
    FileTraverser ft = new FileTraverser(directoryName+"/output.log")

    def setup(){
        ft.log = Mock(Logger)
    }

    @Unroll
    def "test that isCalculationEnded returns whether calculation is ended"() {
        when:
        FileTraverser ft = new FileTraverser(a)
        ft.successString = ">>>>TIME:"
        ft.errorString = "Traceback (most recent call last):"
        Long oldFileSize = new Long(b)
        boolean calcEnded = ft.isCalculationEnded(oldFileSize)

        then:
        calcEnded == c
        ft.hasError == d

        where:
        a                                           |   b           |   c       |   d
        "${directoryName}/output_unfinished.log"    |   0           |   false   |   false
        "${directoryName}/output.log"               |   2373100     |   true    |   false
        "${directoryName}/output_unf_error.log"     |   0           |   false   |   false
        "${directoryName}/output_error.log"         |   220100      |   true    |   true

    }

    def "test getFileSize"() {
        when:
        Long fileSize = ft.getFileSize()

        then:
        fileSize == new Long(2373229)

    }
}
