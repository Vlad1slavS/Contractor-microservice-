FROM eclipse-temurin:21-jdk-alpine-3.21 as builder

RUN apk add --no-cache maven

WORKDIR /app

COPY . /app/.

RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine-3.20

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/*.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/*.jar"]