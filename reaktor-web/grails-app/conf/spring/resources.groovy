import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.jms.connection.SingleConnectionFactory
import reaktor.fileCreator.*
import reaktor.populator.XyzDatabasePopulator
import reaktor.parser.MessageParser
import reaktor.wrapper.OBabelWrapper
import grails.util.Environment

// Place your Spring DSL code here
beans = {
	xmlns aop: "http://www.springframework.org/schema/aop"
	loggingAspect(reaktor.logging.LoggingAspect)
	aop.config("proxy-target-class": true){
		aspect(id:"loggingAspectService",ref: "loggingAspect")
		
	}
	switch(Environment.current){
		case Environment.PRODUCTION:
			jmsConnectionFactory(SingleConnectionFactory){
				targetConnectionFactory = {ActiveMQConnectionFactory cf ->
					brokerURL = 'failover:tcp://wallerlab.uni-muenster.de:61620'
				}
			}
			noMsgConvertConnectionFactory(SingleConnectionFactory){
				targetConnectionFactory = {ActiveMQConnectionFactory cf ->
					brokerURL = 'failover:tcp://wallerlab.uni-muenster.de:61620'
				}
			}
		default:
			jmsConnectionFactory(SingleConnectionFactory){
				targetConnectionFactory = {ActiveMQConnectionFactory cf ->
					brokerURL = 'vm://localhost'
					//brokerURL = 'failover:tcp://localhost:61616'
				}
			}
			noMsgConvertConnectionFactory(SingleConnectionFactory){
				targetConnectionFactory = {ActiveMQConnectionFactory cf ->
					brokerURL = 'vm://localhost'
					//brokerURL = 'failover:tcp://localhost:61616'
				}
			}
			
	}
	mainFolder(File, "reaktor-workspace"){bean ->
		bean.autowire = "byName"
	}
	incomingFolder(File, ref("mainFolder"), "IncomingFiles"){bean ->
		bean.autowire = "byName"
	}
	xyzFileCreator(XyzFileCreator){
	}
	defineFileCreator(DefineFileCreator){
	}
	xyzDatabasePopulator(XyzDatabasePopulator){
	}
	messageParser(MessageParser){
	}
	obabel(OBabelWrapper){
	}
}
