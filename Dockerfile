FROM maven as builder

WORKDIR /build

COPY pom.xml .
COPY /web-api/pom.xml ./web-api/
COPY /web-core/pom.xml ./web-core/
COPY /web-dao/pom.xml ./web-dao/
COPY /web-middleware/pom.xml ./web-middleware/

RUN mvn -Dmaven.test.skip=true -B package

FROM openjdk:8

WORKDIR /app

COPY --from=builder /build/formdang-sp.was.jar /app/ROOT.jar

EXPOSE 12001

CMD java -Xincgc -Xmx1024m -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

