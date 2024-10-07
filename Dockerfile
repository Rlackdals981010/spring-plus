FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/expert-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springplus.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "docker-springplus.jar"]
