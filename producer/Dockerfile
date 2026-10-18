FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS builder
WORKDIR /producer

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:resolve-plugins dependency:resolve

COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests

# opzioni più leggere
# eclipse-temurin:21-jre-alpine or [mcr.microsoft.com/openjdk/jdk:21-distroless](https://mcr.microsoft.com/openjdk/jdk:21-distroless)
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu
WORKDIR /producer

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /producer/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]