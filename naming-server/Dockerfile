FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/naming-server

COPY ./pom.xml /home/naming-server/pom.xml
COPY ./src/main/java/com/in28minutes/microservices/naming_server/NamingServerApplication.java \
    /home/naming-server/src/main/java/com/in28minutes/microservices/naming_server/NamingServerApplication.java

RUN mvn -f /home/naming-server/pom.xml clean package

COPY . /home/naming-server
RUN mvn -f /home/naming-server/pom.xml clean package

FROM openjdk:18.0.2.1-jdk-slim
VOLUME /tmp
EXPOSE 8761
COPY --from=build /home/naming-server/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]