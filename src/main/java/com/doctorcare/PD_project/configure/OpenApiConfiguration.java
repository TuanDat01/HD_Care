package com.doctorcare.PD_project.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8082/api/v1");
        server.setDescription("Development");


        Info information = new Info()
                .title("Doctor Care System API")
                .version("1.0")
                .description("This API exposes endpoints to manage doctor.");
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
