FROM openjdk:17-slim

WORKDIR /app

COPY build/libs/appointment-0.0.1-SNAPSHOT.jar appointment.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "appointment.jar"]