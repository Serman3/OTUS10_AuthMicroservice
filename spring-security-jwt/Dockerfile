FROM maven:3.9.11-amazoncorretto-17-debian as builder
WORKDIR app
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM maven:3.9.11-amazoncorretto-17-debian as extracted
WORKDIR app
RUN apt-get update && apt-get install -y curl
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
