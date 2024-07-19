FROM maven:3-amazoncorretto-21-al2023 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/*.jar"]