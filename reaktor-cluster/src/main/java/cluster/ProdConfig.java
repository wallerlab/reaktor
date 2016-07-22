package cluster;


import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {
	@Bean
	public File directory(){
		return new File("./tmp1/reactor-cluster/workspace");
	}
}
