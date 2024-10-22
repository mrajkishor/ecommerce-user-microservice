FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/user-service.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service.jar"]
