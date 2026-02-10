FROM maven:3.9.11-amazoncorretto-17-debian AS builder
WORKDIR /builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM maven:3.9.11-amazoncorretto-17-debian
WORKDIR /application
RUN rm -f /etc/apt/sources.list.d/corretto.list 2>/dev/null || true && apt-get update && apt-get install -y curl
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ENTRYPOINT ["java", "-jar", "application.jar"]
