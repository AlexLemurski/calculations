FROM eclipse-temurin:21.0.1_12-jdk-alpine
ENV LANG=ru_RU.UTF-8
ENV LC_ALL=ru_RU.UTF-8
WORKDIR /opt/app
COPY target/calculations-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]