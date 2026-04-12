FROM maven:3.9.11-amazoncorretto-17-alpine AS maven_builder
WORKDIR /app

COPY ./pom.xml .
COPY ./lombok.config .
COPY ./main-service/pom.xml ./main-service/pom.xml
COPY ./main-service/src ./main-service/src
COPY ./chat-service/pom.xml ./chat-service/pom.xml
COPY ./chat-service/src ./chat-service/src
COPY ./counter-service/pom.xml ./counter-service/pom.xml
COPY ./counter-service/src ./counter-service/src
COPY ./libs ./libs

RUN  mvn clean install -DskipTests -pl counter-service -am
RUN mv ./counter-service/target/*.jar ./counter-service/target/counter-service.jar

FROM amazoncorretto:17-alpine
WORKDIR /app
EXPOSE 8080
EXPOSE 5005
COPY --from=maven_builder /app/counter-service/target/counter-service.jar ./
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "counter-service.jar"]