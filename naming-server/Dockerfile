FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/naming-server
COPY . /home/naming-server
RUN mvn -f /home/naming-server/pom.xml clean package

FROM openjdk:18.0.2.1-jdk-slim
VOLUME /tmp
EXPOSE 8761
COPY --from=build /home/naming-server/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]