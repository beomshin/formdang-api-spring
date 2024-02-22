FROM openjdk:8

WORKDIR /app

ARG JAR_FILE_PATH=/web-api/target/*.jar

ENV ACTIVE_PROFILE local

COPY /${JAR_FILE_PATH} ROOT.jar

EXPOSE 12001

ENTRYPOINT ["java", "-jar", "-Xincgc", "-Xmx1024m", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-Duser.timezone=Asia/Seoul", "ROOT.jar"]

