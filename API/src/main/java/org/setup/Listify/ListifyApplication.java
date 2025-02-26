package org.setup.Listify;

<<<<<<< HEAD
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.setup.Listify")
=======
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.setup.Listify.Config")
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
public class ListifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListifyApplication.class, args);
	}
<<<<<<< HEAD
}
=======


}
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
