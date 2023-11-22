FROM openjdk:17-slim

WORKDIR /app

COPY build/libs/auth-0.0.1-SNAPSHOT.jar auth.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "auth.jar"]