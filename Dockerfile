FROM openjdk:17-jdk-slim
LABEL maintainer="rlackdals <ksjchm4@gmail.com>"
ADD /build/libs/*.jar app.jar
COPY .env .env

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" ,"-jar", "app.jar"]