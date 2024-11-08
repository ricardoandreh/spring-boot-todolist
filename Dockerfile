FROM cgr.dev/chainguard/maven:latest AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM cgr.dev/chainguard/jdk:latest

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]
