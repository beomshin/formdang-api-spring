FROM maven:3.8.6-jdk-11 as MAVEN_BUILD

WORKDIR /build

COPY pom.xml .

COPY web-api ./web-api
COPY web-core ./web-core
COPY web-dao ./web-dao
COPY web-middleware ./web-middleware

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:8

WORKDIR /app

ARG JAR_FILE_PATH=/web-api/target/*.jar

COPY --from=MAVEN_BUILD /build/target/${JAR_FILE_PATH} app.jar

EXPOSE 12001

ENTRYPOINT ["java", "-jar", "app.jar"]

#
#CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

