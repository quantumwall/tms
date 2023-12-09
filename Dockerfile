FROM eclipse-temurin:17-jre-alpine
COPY target/*.jar tms.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/tms.jar", "--spring.profiles.active=prod"]
