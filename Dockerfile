FROM maven as builder

WORKDIR /build

COPY pom.xml .
RUN mvn -Dmaven.test.skip=true -B package

FROM openjdk:1.8

WORKDIR /app

COPY --form=builder /build/app.jar .
ENTRYPOINT["java", "-jar", "/app/app.jar"]
