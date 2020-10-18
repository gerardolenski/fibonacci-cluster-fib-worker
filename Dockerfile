FROM maven:3.6.3-jdk-11 AS build-stage
WORKDIR /app

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn --batch-mode clean package


FROM openjdk:11-jre-slim as run-stage
WORKDIR /app
COPY --from=build-stage /app/target/fibworker-*.jar /app/fibworker.jar
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar fibworker.jar"]