FROM openjdk:17

ENV LANG C.UTF-8

WORKDIR /app

COPY target/sleephony-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
