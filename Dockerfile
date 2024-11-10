FROM docker.io/maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM gcr.io/distroless/java17-debian12:nonroot

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

USER nonroot:nonroot

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]
CMD ["$JAVA_OPTS"]
