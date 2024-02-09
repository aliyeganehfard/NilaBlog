FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.war
COPY ./target/NilaBlog-1.14.0.war nilaBlog.war
ENTRYPOINT ["java","-jar","/nilaBlog.war"]