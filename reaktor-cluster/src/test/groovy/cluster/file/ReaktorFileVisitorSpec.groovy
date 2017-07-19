package cluster.file

import spock.lang.Specification
import spock.lang.Unroll

import com.google.common.io.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

/**
 * Created by suzanne on 13/01/2017.
 */
class ReaktorFileVisitorSpec extends Specification {
    File testFolder = new File("src/test/Test_Folder")
    Path testFolderPath = testFolder.toPath()
    ReaktorFileVisitor reaktorFileVisitor = new ReaktorFileVisitor(testFolderPath, "reaction")

    void setupSpec() {
        createFolderForTest()
    }

    void cleanupSpec() {
        File largeTestFolder = new File("src/test/Test_Folder/10")
        largeTestFolder.deleteDir()
    }

    def "test preVisitDirectory"() {
        setup:
        File testFile = new File(testFolder, "10/aggregate_0")
        Path testPath = testFile.toPath()
        BasicFileAttributes bfa = java.nio.file.Files.readAttributes(testPath,
                BasicFileAttributes.class)

        when:
        reaktorFileVisitor.preVisitDirectory(testPath, bfa)

        then:
        reaktorFileVisitor.getFileString() == "aggregate_0;"
        reaktorFileVisitor.folderPrefix == "#"
        reaktorFileVisitor.aggFileNum == 0
    }

    @Unroll
    def "test visitFile"() {
        when:
        ReaktorFileVisitor rfv = new ReaktorFileVisitor(testFolderPath,a)
        File testFile = new File(testFolder, b)
        Path testPath = testFile.toPath()
        BasicFileAttributes bfa = java.nio.file.Files.readAttributes(testPath,
                BasicFileAttributes.class)
        rfv.aggFileNum = c
        rfv.fileStringBuilder = new StringBuilder()
        rfv.filePrefix = ""
        String folderName = b.split("/")[1]
        rfv.fileStringBuilder.append(folderName + ";")
        rfv.visitFile(testPath, bfa)

        then:
        rfv.getFileString() == d
        rfv.filePrefix == ","

        where:
        a               |   b                                   |   c   |   d
        "reaction"      |   "8/product_geom/molecule0.xyz"      |   0   |   "product_geom;molecule0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"
        "reaction"      |   "8/product_geom/product_25.xyz"     |   0   |   "product_geom;product_25.xyz:12\nbenzene example\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  N       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"
        "aggregation"   |   "10/aggregate_0/result_0_0_234.xyz" |   0   |   "aggregate_0;"
        "aggregation"   |   "10/aggregate_0/start_0_0_108.xyz"  |   0   |   "aggregate_0;"
        "aggregation"   |   "10/aggregate_1/result_1_0_51.xyz"  |   1   |   "aggregate_1;"
        "aggregation"   |   "10/aggregate_1/start_0_0_26.xyz"   |   1   |   "aggregate_1;start_0_0_26.xyz:12\nGeometry from arc file\n C \t 0.59839890 \t -0.53910693 \t -0.13414855 \n C \t -0.69823840 \t -0.29243359 \t 0.02742261 \n H \t 1.00620884 \t -1.52531577 \t -0.29564661 \n H \t 1.35636771 \t 0.23121849 \t -0.11728704 \n H \t -1.46114378 \t -1.05570791 \t 0.01187852 \n H \t -1.09872284 \t 0.69876212 \t 0.18867303 \n C \t -0.13800951 \t 3.30895923 \t 0.35832330 \n C \t 1.16837544 \t 3.10612006 \t 0.21373813 \n H \t -0.64097683 \t 3.30931653 \t 1.31401684 \n H \t -0.80936320 \t 3.49335037 \t -0.46732993 \n H \t 1.84023995 \t 2.92371192 \t 1.03938908 \n H \t 1.67146112 \t 3.10767853 \t -0.74190792 \n"
        "aggregation"   |   "10/aggregate_1/start_1_0_20.xyz"   |   1   |   "aggregate_1;"
        "aggregation"   |   "10/aggregate_18/result_18_3_7.xyz" |   18  |   "aggregate_18;"
        "aggregation"   |   "10/aggregate_18/start_18_2_243.xyz"|   18  |   "aggregate_18;"
        "aggregation"   |   "10/aggregate_18/start_17_1_154.xyz"|   18  |   "aggregate_18;start_17_1_154.xyz:114\nGeometry from arc file\n C \t 0.29632736 \t 0.02601656 \t 1.12424965 \n C \t -0.43998222 \t 0.05697422 \t 0.50645195 \n H \t 2.04871600 \t -1.36270695 \t 1.48945329 \n H \t 0.74933537 \t 1.50886411 \t 2.90233560 \n H \t -0.49184714 \t -0.13748606 \t -0.80121861 \n H \t -1.63495709 \t 0.38390332 \t 0.66164004 \n C \t 0.81079303 \t 4.51062850 \t 1.02005024 \n C \t 1.16716285 \t 4.31631803 \t 2.47191175 \n H \t 0.89593726 \t 5.52391888 \t 0.60177809 \n H \t -0.23704316 \t 2.64619055 \t 0.59630086 \n H \t 1.50321061 \t 5.16206268 \t 3.07074129 \n H \t 0.56778351 \t 3.64679616 \t 3.08730836 \n C \t -0.35611908 \t 3.75009685 \t 0.42601175 \n C \t -2.27218539 \t 1.77814354 \t -2.38911634 \n H \t 0.20314183 \t -6.05948364 \t 0.25296558 \n H \t 0.11541846 \t -6.10500009 \t 0.18997743 \n H \t -1.28519002 \t 1.54492453 \t -1.98744360 \n H \t -2.36506873 \t 2.84864933 \t -2.54254942 \n C \t -3.46944734 \t 0.97488920 \t -1.93592946 \n C \t -2.93895438 \t 0.81679726 \t -3.34561062 \n H \t -3.33814917 \t 0.17029083 \t -1.21698961 \n H \t -4.42208412 \t 1.46955154 \t -1.77764467 \n H \t -2.43125028 \t -0.10290358 \t -3.62525819 \n H \t -3.51227693 \t 1.19941067 \t -4.17866387 \n C \t 0.34680316 \t -0.46693373 \t 3.84198362 \n C \t 0.23334539 \t 0.41543864 \t 2.70625361 \n H \t 0.51787015 \t -1.60445504 \t 3.64033400 \n H \t 0.99571011 \t -0.27010287 \t 4.76811944 \n H \t 2.91506544 \t 1.85937006 \t 4.46396085 \n H \t 2.78153826 \t 2.32826814 \t 4.20090398 \n C \t -2.80195963 \t 3.45833244 \t 0.85218616 \n C \t -1.79546800 \t 4.10281194 \t 1.05088862 \n H \t -1.76433202 \t 5.32981176 \t 1.01352958 \n H \t -0.35775851 \t 3.85661248 \t -0.68661123 \n H \t -3.88666449 \t 3.91212814 \t 0.53988373 \n H \t -2.85749596 \t 2.46709910 \t 0.14187539 \n C \t 2.06487872 \t 3.77756902 \t 1.39773474 \n C \t 1.23647057 \t 2.66033198 \t -3.03896053 \n H \t 2.13328189 \t 2.69969805 \t 1.22709007 \n H \t 3.04158862 \t 4.23437446 \t 1.23862453 \n H \t 0.21917019 \t 3.12095328 \t -3.01488789 \n H \t 1.44048893 \t 2.40768106 \t -4.10794465 \n C \t -4.30328074 \t 0.34639826 \t 1.48866882 \n C \t -4.04214055 \t -1.14477980 \t 1.13179670 \n H \t -3.70860926 \t 0.72823872 \t 2.18817716 \n H \t -4.92259365 \t 0.86810832 \t 0.97668076 \n H \t -3.41578907 \t -1.67250785 \t 1.64657268 \n H \t -4.63530941 \t -1.52457712 \t 0.43654144 \n C \t 2.28303546 \t 3.11618154 \t -2.17401004 \n C \t 1.70321167 \t 1.82431971 \t -1.97714264 \n H \t 3.32370331 \t 3.24443604 \t -2.55166470 \n H \t 2.10924321 \t 3.93929727 \t -1.43621791 \n H \t 2.27513245 \t 0.88516117 \t -2.18496746 \n H \t 1.06519711 \t 1.60646219 \t -1.07967512 \n C \t -0.81532204 \t -2.27346882 \t -2.36840453 \n C \t 1.70384349 \t -0.40021508 \t 0.98942419 \n H \t 2.57931115 \t 0.30043083 \t 1.20061163 \n H \t -0.80309433 \t -2.64322983 \t -3.42949856 \n H \t 1.90179648 \t -0.65668559 \t -0.12280219 \n H \t -0.70862686 \t -1.14126037 \t -2.45856766 \n C \t 4.50214230 \t 1.70508301 \t 0.44386496 \n C \t 5.15882342 \t 1.07738870 \t 1.22821703 \n H \t 5.70002865 \t 0.62305876 \t 2.05669642 \n H \t 4.00855287 \t 2.09811074 \t -0.32847618 \n H \t 4.32630318 \t -1.16672743 \t 3.84792819 \n H \t 3.62603054 \t -1.01272868 \t 3.81044128 \n C \t -0.84208485 \t 0.14337031 \t 3.59798975 \n C \t -2.32526141 \t 3.48333793 \t 1.96435733 \n H \t -1.33123409 \t 0.90400343 \t 4.29384745 \n H \t -1.75830415 \t -0.47564121 \t 3.17850738 \n H \t -1.82308606 \t 2.52885642 \t 2.52055438 \n H \t -2.84216284 \t 3.97231307 \t 2.93931374 \n C \t 0.31358506 \t -2.81408054 \t -1.61349490 \n C \t -2.21762854 \t -2.58521339 \t -1.75633542 \n H \t 0.45728157 \t -2.35496957 \t -0.58288312 \n H \t 1.31437411 \t -2.65209005 \t -2.14176875 \n H \t -3.00218962 \t -2.00036066 \t -2.36336507 \n H \t 0.26333799 \t -3.92574854 \t -1.42753409 \n C \t 2.68993505 \t -3.78848833 \t 2.60272873 \n C \t 2.94446317 \t -3.94061808 \t 1.60087978 \n H \t 3.01730701 \t -3.47229041 \t 3.41892718 \n H \t 1.23795773 \t -4.08102341 \t 2.91500568 \n H \t 4.03000348 \t -3.73940880 \t 1.23006920 \n H \t 2.49817954 \t -4.26106276 \t 0.60640903 \n C \t -0.25569316 \t 0.18591007 \t -5.34698409 \n C \t 0.81158903 \t -0.61718825 \t -4.66766600 \n H \t -0.63792357 \t -0.01358924 \t -6.27843384 \n H \t -0.57149427 \t 1.15205666 \t -4.78594072 \n H \t 1.13695857 \t -1.58154959 \t -5.22300868 \n H \t 1.20438773 \t -0.41092750 \t -3.73470127 \n C \t -1.85512105 \t -3.62999612 \t 3.10469196 \n C \t -0.98121664 \t -3.10517409 \t 1.80311068 \n H \t -2.05767366 \t -4.46490357 \t 3.28870260 \n H \t -1.69506267 \t -2.84765822 \t 3.87420222 \n H \t -1.17444227 \t -3.89970846 \t 1.03951063 \n H \t -0.80146737 \t -2.27683715 \t 1.63152587 \n C \t 3.87314102 \t -1.36028209 \t -1.50334123 \n C \t 3.71800019 \t -1.34622538 \t -2.94181580 \n H \t 4.33992111 \t -0.55815780 \t -1.02097318 \n H \t 3.61544385 \t -2.15315975 \t -0.94258867 \n H \t 3.98036633 \t -0.56414221 \t -3.49606273 \n H \t 3.25981288 \t -2.14419650 \t -3.41819856 \n C \t -3.48834043 \t -4.53116099 \t -0.65129752 \n C \t -2.50931267 \t -3.91620068 \t -1.75619837 \n H \t -3.80177620 \t -5.48479290 \t -0.66087789 \n H \t -3.69154522 \t -3.81029733 \t 0.14325801 \n H \t -2.29645009 \t -4.62018615 \t -2.49464330 \n H \t -2.25588293 \t -2.09059073 \t -0.72226135 \n C \t 5.02161191 \t -5.02691383 \t 5.21238232 \n C \t 4.68592320 \t -4.18611449 \t 6.09104452 \n H \t 4.98589799 \t -5.38951400 \t 4.44575912 \n H \t 5.91612718 \t -5.73372445 \t 5.78106528 \n H \t 3.84279260 \t -3.32068190 \t 6.04562496 \n H \t 5.21463581 \t -3.71260293 \t 6.99200626 \n"
    }

    def "test getFileString"() {

    }

    private createFolderForTest(){

        File testFolder = new File("src/test/Test_Folder")
        File folder10 = new File(testFolder,"10")
        folder10.mkdir()
        for(int i=0;i<21;i++){
            File subdir = new File(folder10, "aggregate_" + i)
            subdir.mkdir()
        }
        folder10.eachDir { dir ->
            for(int i = 0; i < 300; i++){
                int maxJ = 1
                if(!dir.name.endsWith("_0")){
                    maxJ = 4
                }
                for(int j = 0; j < maxJ; j++){
                    String number = dir.name.split("_")[1]
                    File resFile = new File(dir, "result_${number}_${j}_${i}.xyz")
                    File startArc = new File(dir, "start_${number}_${j}_${i}.arc")
                    File startLog = new File(dir, "start_${number}_${j}_${i}.log")
                    File startOut = new File(dir, "start_${number}_${j}_${i}.out")
                    File startXyz = new File(dir, "start_${number}_${j}_${i}.xyz")
                    resFile.createNewFile()
                    startArc.createNewFile()
                    startLog.createNewFile()
                    startOut.createNewFile()
                    startXyz.createNewFile()
                }
                File aggFolderStartFiles = new File(testFolder,
                        "aggregate_start_files")
                if(dir.name.endsWith("_1")){
                    aggFolderStartFiles.eachFile{ file ->
                        if(file.name.startsWith("start_0")){
                            File fileCopy = new File(dir, file.name.split("/")[-1])
                            Files.copy(file, fileCopy)
                        }
                    }

                } else if(dir.name.endsWith("_18")){
                    aggFolderStartFiles.eachFile{ file ->
                        if(file.name.startsWith("start_17")){
                            File fileCopy = new File(dir, file.name.split("/")[-1])
                            Files.copy(file, fileCopy)
                        }
                    }
                }
            }
        }

        println "done"
    }

}
