package org.setup.Listify;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.setup.Listify.Config")
public class ListifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListifyApplication.class, args);
	}

	@Bean
	public PhysicalNamingStrategy physicalNamingStrategy() {
		return new org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl();
	}
}