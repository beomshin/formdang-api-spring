FROM openjdk:8
#CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=/web-api/target/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
#EXPOSE 12001
#
#CMD java -Xincgc -Xmx1024m -Dserver.port=12001 -Dsentry.environment=test -Duser.timezone=Asia/Seoul -jar /app/ROOT.jar --logging.path=/app/logs --spring.profiles.active=test

