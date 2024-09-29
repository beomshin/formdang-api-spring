FROM openjdk:8-alpine

WORKDIR /

ARG JAR_FILE_PATH=/app/web-api/target/web-api-0.0.1.jar

ENV ACTIVE_PROFILE="local"

COPY /${JAR_FILE_PATH} ROOT.jar

EXPOSE 12001

ENTRYPOINT ["java", "-jar", "-Xincgc", "-Xmx256m", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-Duser.timezone=Asia/Seoul", "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=35010", "-Dcom.sun.management.jmxremote.rmi.port=35010", "-Djava.rmi.server.hostname=formdang-api-spring", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "ROOT.jar"]

