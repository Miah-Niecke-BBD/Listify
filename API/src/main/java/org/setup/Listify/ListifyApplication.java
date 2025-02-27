package org.setup.Listify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ListifyAPI", version = "1.0", description = "API for Listify productivity tool"))
public class ListifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListifyApplication.class, args);
    }
}