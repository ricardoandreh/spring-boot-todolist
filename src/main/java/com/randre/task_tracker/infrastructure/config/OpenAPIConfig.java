package com.randre.task_tracker.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${randre.openapi.url}")
    private String url;

    private List<Server> getServers() {
        Server server = new Server();
        server.setUrl(this.url);

        if (this.url.contains("onrender")) {
            server.setDescription("Server URL in Production environment");
        } else {
            server.setDescription("Server URL in Development environment");
        }

        return List.of(server);
    }

    private Info getInfo() {
        Contact contact = new Contact();
        contact.setEmail("ricardo.andre.ifc@gmail.com");
        contact.setName("Ricardo André da Silva");
        contact.setUrl("https://github.com/ricardoandreh/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        return new Info()
                .title("Task Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tasks.")
                .termsOfService("https://github.com/ricardoandreh/")
                .license(mitLicense);
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI myOpenAPI() {
        List<Server> servers = this.getServers();

        Info info = this.getInfo();

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", this.createAPIKeyScheme()))
                .info(info).servers(servers);
    }
}
