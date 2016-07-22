package cluster;

import java.io.File;

import javax.jms.ConnectionFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.SubscribableChannel;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.RecursiveLeafOnlyDirectoryScanner;
import org.springframework.integration.file.filters.*;


@Configuration
@ComponentScan("cluster")
@EnableIntegration
@EnableJms
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class Config implements ApplicationContextAware{
	
	ApplicationContext appContext;
	
	@Bean
	ConnectionFactory jmsConnectionFactory(){
		ConnectionFactory connectionFactory = new SingleConnectionFactory(new ActiveMQConnectionFactory("failover:tcp://wallerlab.uni-muenster.de:61620"));//properties file
		return connectionFactory;
	}
	
	@Bean
	JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(this.jmsConnectionFactory());
		return template;
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
		DefaultJmsListenerContainerFactory djlcf = new DefaultJmsListenerContainerFactory();
		djlcf.setConnectionFactory(this.jmsConnectionFactory());
		return djlcf;
	}
	
	@Bean
	public FileListFilter<File> fileFilter() {
		CompositeFileListFilter<File> cflf = new CompositeFileListFilter<File>();
		cflf.addFilter(new AcceptOnceFileListFilter<File>());
		cflf.addFilter(new SimplePatternFileListFilter("jobexd*"));
		return cflf;
	} 
	
	@Bean
	@InboundChannelAdapter(value="filePollerChannel", poller = @Poller(fixedDelay = "10000"))
	public MessageSource<File> inboundChannelAdapter(){
		FileReadingMessageSource frms = new FileReadingMessageSource();
		frms.setDirectory((File) appContext.getBean("directory"));
		frms.setScanner(new RecursiveLeafOnlyDirectoryScanner());
		frms.setFilter(this.fileFilter());
		return frms;
	}
	
	@Bean
	public SubscribableChannel filePollerChannel(){
		DirectChannel dc = new DirectChannel();
		return dc;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
	}
}
