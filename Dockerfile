FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} medicalclinicproxy.jar
ENTRYPOINT ["java","-jar","/medicalclinicproxy.jar"]