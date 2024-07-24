package com.ecobank.soole.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
    info = @Info(
            title = "Soole API",
            version = "Version 1.2",
            contact = @Contact(
                name = "Innovations", email = "olahammed@ecobank.com", url = "https://olahammed.ecobank.org"
            ),
            license = @License(
                name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
            ),
            termsOfService = "https://ecobank.com/TOS",
            description = "Spring Boot Restful API demo by innovations team with me Olaide Hammed is Onyejeme Emmanuel"
        )
)
public class SwaggerConfig {
    
}
