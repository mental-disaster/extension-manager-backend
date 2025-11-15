FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace

COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle gradle
COPY src src
RUN chmod +x gradlew
RUN ./gradlew --no-daemon bootJar && cp build/libs/*.jar app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup --system spring \
    && adduser --system --ingroup spring spring \
    && mkdir -p /app/data \
    && chown -R spring:spring /app

ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/app.db

COPY --from=builder --chown=spring:spring /workspace/app.jar /app/app.jar

EXPOSE 8080

USER spring
ENTRYPOINT ["java","-jar","/app/app.jar"]