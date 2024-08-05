package com.gestion.almacenes;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AlmacenesApplication {

  public static void main(String[] args) {
    SpringApplication.run(AlmacenesApplication.class, args);
    System.out.println("--------------------------------------");
    System.out.println("----------- Desplegado en: -----------");
    System.out.println("--------------------------------------");
    System.out.println("http://localhost:8081/storehouse/v1/doc/swagger-ui/index.html");
    System.out.println("https://service-storehouse-production.onrender.com/storehouse/v1/doc/swagger-ui/index.html");
  }

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Documentación de API-REST de la GESTION DE ALMACENES")
                .version("1.0")
                .description(
                    "Esta es una pequeña documentacion para los endpoints del programa de Gestion de almacenes")
        );
  }

}
