# Use uma imagem base do JDK 17 para rodar a aplicação Spring Boot
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado para o diretório de trabalho do container
COPY target/back-1.1.0-SNAPSHOT.jar app.jar

# Expõe a porta 8080 para que a aplicação seja acessível
EXPOSE 8080

# Comando para executar o JAR da aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
