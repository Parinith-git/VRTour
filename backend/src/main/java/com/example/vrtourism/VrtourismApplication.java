package com.example.vrtourism;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class VrtourismApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		Map<String, Object> envProps = new HashMap<>();
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
			envProps.put(entry.getKey(), entry.getValue());
		});
		
		SpringApplication app = new SpringApplication(VrtourismApplication.class);
		
		if (System.getProperty("MONGODB_URI") != null) {
			envProps.put("spring.data.mongodb.uri", System.getProperty("MONGODB_URI"));
		}
		
		app.setDefaultProperties(envProps);
		app.run(args);
	}

}
