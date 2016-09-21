grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.8
grails.project.source.level = 1.8
grails.project.war.file = "target/${appName}.war"

grails.server.port.http=9090
grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        // runtime 'mysql:mysql-connector-java:5.1.29'
        // runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
		test 'cglib:cglib-nodep:2.2.2'
		compile 'org.apache.activemq:activemq-broker:5.10.0'
		compile 'org.apache.activemq:activemq-client:5.10.0'
		compile 'org.apache.activemq:activemq-kahadb-store:5.10.0'
		compile 'org.apache.activemq:activemq-spring:5.10.0'
		compile 'org.slf4j:slf4j-api:1.7.7'
		compile 'log4j:log4j:1.2.17'
		compile 'geronimo-spec:geronimo-spec-jta:1.0.1B-rc4'
		compile 'geronimo-spec:geronimo-spec-jms:1.1-rc4'
		compile 'geronimo-spec:geronimo-spec-j2ee-management:1.0-rc4'
		compile 'com.google.guava:guava:18.0'
        	compile 'com.atlassian.clover:clover:4.0.2'
        	build 'org.apache.httpcomponents:httpcore:4.3.2'
                build 'org.apache.httpcomponents:httpclient:4.3.2'
                build 'org.apache.httpcomponents:httpmime:4.3.3'
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:8.0.14.1"
        
        // Coveralls plugin
                build(':coveralls:0.1.4', ':rest-client-builder:1.0.3') {
                    export = false
                }
                test(':code-coverage:2.0.3-3') {
                    export = false
                }
                

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ":asset-pipeline:1.9.9"
		compile ":jms:1.3"
		compile ":spring-security-core:2.0-RC4"
		compile ":spring-security-ui:1.0-RC2"
		compile ":mail:1.0.7"
		compile ":uploadr:1.0.0"
		compile ":clover:4.0.2"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.6.1" // or ":hibernate:3.6.10.18"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"

        // Uncomment these to enable additional asset-pipeline capabilities
        //compile ":sass-asset-pipeline:1.9.0"
        //compile ":less-asset-pipeline:1.10.0"
        //compile ":coffee-asset-pipeline:1.8.0"
        //compile ":handlebars-asset-pipeline:1.3.0.3"
    }
}
