FROM openjdk:8-jdk

EXPOSE 8080

# JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정한다.
ARG JAR_FILE=./web-api/target/*.jar

# JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar

ENV USE_PROFILE test
ENV LOG_PATH /home/sp/web/logs

# 시스템 진입점 정의
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}","-jar","/app.jar", "--logging.path=$LOG_PATH"]