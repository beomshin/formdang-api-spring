FROM maven as builder

WORKDIR /build

COPY /pom.xml .
RUN mvn -Dmaven.test.skip=true -B package

FROM openjdk:8

WORKDIR /app

COPY --from=builder /build/formdang-sp.was.jar .

CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/app.jar --logging.path=/app/logs --spring.profiles.active=test

