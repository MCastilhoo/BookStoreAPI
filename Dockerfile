# Use a imagem base oficial do Node.js
FROM maven:3.9.8-amazoncorretto-17

# Crie e defina o diretório de trabalho da aplicação
WORKDIR /usr/src/app


# Copie o restante da aplicação
COPY . .

# Exponha a porta em que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
RUN mvn clean spring-boot:run -DskipTests