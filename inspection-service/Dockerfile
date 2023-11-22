FROM openjdk:17-slim

WORKDIR /app

COPY build/libs/inspection-0.0.1-SNAPSHOT.jar inspection.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "inspection.jar"]