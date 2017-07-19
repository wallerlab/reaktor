package cluster.config;

import java.io.File;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@Profile("dev")
public class DevConfig {

	@Bean
	public File directory(){
		return new File("reaktor-cluster/tmp1/reaktor-cluster/workspace");
	}

	@Bean
	public ConnectionFactory jmsConnectionFactory(){
		ConnectionFactory connectionFactory = new SingleConnectionFactory(new ActiveMQConnectionFactory("failover:tcp://localhost:61616"));
		return connectionFactory;
	}
}
