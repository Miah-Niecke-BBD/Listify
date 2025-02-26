package org.setup.Listify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.setup.Listify")
public class ListifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListifyApplication.class, args);
	}
}
