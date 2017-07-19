package cluster.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;
import java.io.File;

/**
 * Created by suzanne on 17.05.17.
 */
@SpringBootApplication
@Profile("test")
public class TestConfig {


	@Bean
	public File directory(){
		File file = new File("src/test/Test_Folder/FHS_test");
		return file;
	}

	@Bean
	ConnectionFactory jmsConnectionFactory(){
		ConnectionFactory connectionFactory = new SingleConnectionFactory(new
				ActiveMQConnectionFactory("vm://localhost"));//properties file
		System.out.println("connection factory connected to localhost");
		return connectionFactory;
	}

}
