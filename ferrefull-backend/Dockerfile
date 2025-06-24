FROM openjdk:17-oracle
COPY  build/libs/*.jar phoenixapp.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "phoenixapp.jar"]