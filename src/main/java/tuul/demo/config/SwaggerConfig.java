package tuul.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development server");

        Contact contact = new Contact();
        contact.setName("Timofey Babisashvili");
        contact.setEmail("timofey.test@gmail.com");
        contact.setUrl("https://timofey-tigertimofeys-projects.vercel.app");

        Info info = new Info()
                .title("Tuul Vehicle Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for managing shared vehicles.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
