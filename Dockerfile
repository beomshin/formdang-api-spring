FROM openjdk:8

WORKDIR /app

ARG JAR_FILE_PATH=/web-api/target/*.jar

COPY /${JAR_FILE_PATH} app.jar

EXPOSE 12001

#VOLUME ['/var/log']

ENTRYPOINT ["java", "-jar", "-Xincgc", "-Xmx1024m", "-Dsentry.environment=test", "-Duser.timezone=Asia/Seoul", "app.jar"]

#
#CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

