FROM maven:3.0.0 as builder

#WORKDIR /build
#
#COPY pom.xml .
#COPY web-api/pom.xml ./web-api
#COPY web-core/pom.xml ./web-core
#COPY web-dao/pom.xml ./web-dao
#COPY web-middleware/pom.xml ./web-middleware

RUN mvn -B dependency:go-offline

COPY . .
RUN mvn package

FROM openjdk:8

WORKDIR /app

COPY --from=builder /build/target/formdang-sp.was.jar .

EXPOSE 12001

CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/formdang-sp.was.jar --logging.path=/app/logs --spring.profiles.active=test

