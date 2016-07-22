package cluster;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

	@Bean
	public File directory(){
		File file = new File("src/test/Test_Folder/FHS_test");
		return file;
	}
}
