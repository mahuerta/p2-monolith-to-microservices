FROM openjdk:11-jre-slim
USER root
WORKDIR /
COPY target/user-ms.jar /opt/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/opt/user-ms.jar"]
