FROM fabric8/java-alpine-openjdk8-jdk:1.2.6
MAINTAINER Dmytro Myronenko

ENV JAVA_APP_JAR stocks-api.jar
ENV AB_OFF true

EXPOSE 8080

COPY build/libs/*.jar /deployments/stocks-api.jar