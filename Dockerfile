FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD /target/api-orcamento-familiar-0.0.1-SNAPSHOT.jar api-orcamento-familiar.jar
ENTRYPOINT ["java", "-jar", "api-orcamento-familiar.jar"]