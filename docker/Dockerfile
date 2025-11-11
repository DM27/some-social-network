FROM maven:3.9.11-amazoncorretto-17-alpine AS maven_builder
WORKDIR /app
COPY ./pom.xml .
COPY ./src ./src
RUN mvn clean install -DskipTests
RUN mv target/*.jar target/application.jar

FROM amazoncorretto:17-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=maven_builder /app/target/application.jar ./
ENTRYPOINT ["java", "-jar", "application.jar"]
#ENTRYPOINT ["tail", "-f", "/dev/null"]
