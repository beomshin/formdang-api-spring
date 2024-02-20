FROM maven as builder

WORKDIR /home

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY . .
RUN mvn -Dmaven.test.skip=true package

FROM openjdk:8

WORKDIR /app

COPY --from=builder /home/target/formdang-sp.was.jar /app/ROOT.jar

EXPOSE 12001

CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

