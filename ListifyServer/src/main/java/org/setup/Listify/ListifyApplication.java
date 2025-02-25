package org.setup.Listify;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ListifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListifyApplication.class, args);
	}


}