package co.edu.tdea.feedbacksystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI feedbackSystemOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor de desarrollo local");

        Contact contact = new Contact();
        contact.setName("Tecnológico de Antioquia");

        Info info = new Info()
                .title("Feedback System API")
                .version("1.0.0")
                .description("""
                        API de Analítica de Aprendizaje para Detección Temprana y Retroalimentación Personalizada

                        Tecnológico de Antioquia - Trabajo de Grado

                        **Sistema MVP backend independiente**

                        Para referenciar estudiantes se utiliza el campo `referenciaEstudiante` como identificador externo.
                        La integración con Campus TdeA queda como trabajo futuro.
                        """)
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
