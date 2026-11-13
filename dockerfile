   FROM eclipse-temurin:21-jre-alpine
   WORKDIR /app
   COPY target/demo-ci-*.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]