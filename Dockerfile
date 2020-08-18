FROM openjdk:8-jdk-alpine

MAINTAINER Ironelis Herrera & Nicole Espinal

VOLUME /tmp
EXPOSE 8080
COPY /build/libs/dockercompose-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

