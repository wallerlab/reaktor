package cluster.config;


import java.io.File;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;

@SpringBootConfiguration
@Profile("prod")
public class ProdConfig {

	@Bean
	public File directory(){
		System.out.println("creating directory bean");
		return new File("./tmp1/reaktor-cluster/workspace");
	}

	@Bean
	public ConnectionFactory jmsConnectionFactory(){
		ConnectionFactory connectionFactory = new SingleConnectionFactory(new ActiveMQConnectionFactory("failover:tcp://wallerlab.uni-muenster.de:61620"));
		System.out.println("connection factory is local apache");
		return connectionFactory;
	}

}
