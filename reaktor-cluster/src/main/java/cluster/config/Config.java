package cluster.config;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;


@SpringBootConfiguration
@EnableJms
public class Config{

	Logger logger = Logger.getLogger("cluster");

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory jmsConnectionFactory,
			 DefaultJmsListenerContainerFactoryConfigurer configurer){

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, jmsConnectionFactory);
		return factory;
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context){
		return args -> {
			String[] beanNames = context.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for(String beanName: beanNames){
				logger.debug(beanName);
			}
		};
	}
}
