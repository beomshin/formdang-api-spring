FROM maven as builder

RUN mvn -Dmaven.test.skip=true -B package --file ./pom.xml

FROM openjdk:8

WORKDIR /app

COPY --from=builder /target/formdang-sp.was.jar /app/ROOT.jar

EXPOSE 12001

CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

