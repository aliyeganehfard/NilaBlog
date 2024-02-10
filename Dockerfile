FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.war
COPY NilaBlog-1.15.0.war nilaBlog.war
ENTRYPOINT ["java","-jar","/nilaBlog.war"]

#FROM openjdk:17-jdk-alpine
#ARG JAR_FILE=target/NilaBlog-1.15.0.war
#COPY ${JAR_FILE} NilaBlog.war
#ENTRYPOINT ["java","-jar","/nilaBlog.war"]