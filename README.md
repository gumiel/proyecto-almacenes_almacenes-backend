# Instalacion 
Programas necesarios.  
- Java 17 instalado en la maquina  
- Postgres 15 o superiores instalado  
Para arrancar el sistema es necesario crear la base de datos "gestion_almacenes" en postgres.  
Desplegar el proyecto y se crearan las tablas necesarias automaticamente

#### Rutas
- Ruta inicial: http://localhost:8081/storehouse/v1  
- Ruta de producción: https://service-storehouse-production.onrender.com/storehouse/v1 (Esperar 45 segundo despues de ingresar a la Url para que se levante el servidor)  
- Documentación API-REST: http://localhost:8081/storehouse/v1/doc/swagger-ui/index.html  
- Documentación API-REST de producción: https://service-storehouse-production.onrender.com/storehouse/v1/doc/swagger-ui/index.html (Esperar 45 segundo despues de ingresar a la Url para que se levante el servidor)  
- git remote add github https://github.com/gumiel/proyecto-almacenes_almacenes-backend.git

#### Estructuras de las rutas  
http://server:port/context-path/v1/controller/action/slug?parameter=150  
http://server:port/context-path/v1/resource/action/slug?parameter=150  

#### Modelo de base de datos  
![Base de datos](https://res.cloudinary.com/daid2fusr/image/upload/fl_preserve_transparency/v1722827888/gestion_almacenes_-_public_lildwy.jpg)
#### Modelo trabajo  
Esquema general de despliegues  
![Esquema_de_despliegue](https://res.cloudinary.com/daid2fusr/image/upload/fl_preserve_transparency/v1722830256/Estructura_de_despliegue_almacenes-Esquema_de_despliegue.drawio_nmsf3x.jpg)

Esquema de trabajo personalizado con Gitflow  
![Esquema_gitflow](https://res.cloudinary.com/daid2fusr/image/upload/fl_preserve_transparency/v1722830259/Estructura_de_despliegue_almacenes-Esquema_gitflow.drawio_hflekf.jpg)  

Esquema del proceso de despliegue  
![Esquema_proceso_despliegue](https://res.cloudinary.com/daid2fusr/image/upload/v1722950138/Estructura_de_despliegue_almacenes-Esquema_del_proceso_de_despliegue.drawio_mplvv4.png)  

#### Aprendizaje
https://www.youtube.com/watch?v=-SzKqwgPTyk  
https://springdoc.org/  
https://www.youtube.com/watch?v=Kln66OZIZME  

#### Herramientas
Template Live: https://github.com/gumiel/gestion_almacenes/blob/master/recursos/settings.zip  
Estilo de codificación: https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml  

#### Atajos de teclado intellij IDEA
Ctrl+alt+o -> Optimiza los Imports de las clases  
Ctrl+alt+l -> Optimiza los el codigo como tabulaciones y otros  
Ctrl+alt+s -> Dirige a las configuraciones que puede usarse para agregar mas Templates  

#### Modularizacion
https://github.com/pwalser75/spring-boot-modular/tree/master  
https://www.baeldung.com/spring-boot-multiple-modules  

https://drive.google.com/file/d/1eJOAwVdztWJ32v1LDdy8nAtcUXKRNO9V/view

# Librerias y tecnologias implementadas
## Sonar
Analisis de codigo fuente para control de calidad  
```bash 
docker pull sonarqube  
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest  
./mvnw clean install  
./mvnw verify sonar:sonar -D sonar.token=sqa_71e14929e5f0442fd2f927f4191c5d6917e9e2e7  
./mvnw clean install -Dkiptest  
```

## flywaydb
Versionador de base de datos. Esta usado para la migracion de datos y procedimientos almacenados.  
Los datos versionados estan en.  
```bash 
/resource/db/migration/*.sql
```

## Pruebas de procesos con Postman y Newman
Los recursos consumidos para las pruebas estan en el directorio  
```bash 
/recursos/Postman-Collection/*.json
```
