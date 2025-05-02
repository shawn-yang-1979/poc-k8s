FROM maven:3.8.5-openjdk-17
WORKDIR /workspace
COPY src src
COPY pom.xml pom.xml
RUN mvn clean install
WORKDIR /workspace/target
RUN cp *.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]