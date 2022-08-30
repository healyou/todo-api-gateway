FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y curl
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]