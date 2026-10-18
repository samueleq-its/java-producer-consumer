FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS builder
WORKDIR /consumer

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:resolve-plugins dependency:resolve

COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu
WORKDIR /consumer

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /consumer/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
