FROM openjdk:8-alpine

WORKDIR /app

ARG JAR_FILE_PATH=/web-api/target/*.jar

ENV ACTIVE_PROFILE="local" \
    DATASOURCE_KEY = "WelQo35zfvyiSha" \
    DATASOURCE_URL = "ENC(kKYMlEciM6CbjjtiI0rEA7gnanVoVLvS7qKqrSSqzxCcQYTjLMK5xyiYWsGFlDT8BDjgFRvYXmVOK/i2PYlHZF26jDU6/sEjHxugeBQncWCJvjuKy5ST8G72hNyxzQClt4tySQhQTfBGpyMUjCAneCNPpgkTp4gHRtHcnSNe3zjXwUYlOkOV38stUU2a6jhMLC9OvsDWeyDa6zQTS4n+PYInRhy1frvW3ncQSw55YlV6SIKSbAyI2mSTigit+ETAOQ0ALClMXkU5fS/lCYw/YXxaIHr7rHMewgN7OmUu1H8=)" \
    DATASOURCE_USERNAME = "ENC(zkvbxamC0jPW/79MemcxiMgN7cgGKpzudG4OluAzLLrUaQxvvPLNSVmSoM3mY9yF)" \
    DATASOURCE_PASSWORD = "ENC(AI5OUYOhotKwQKmGoYhM+u9SwYFCoGsNDxQ2IfvjQ2sJcdAeikTB6db/GEUJQLQm)"

COPY /${JAR_FILE_PATH} ROOT.jar

EXPOSE 12001

ENTRYPOINT ["java", "-jar", "-Xincgc", "-Xmx1024m",
"-Dspring.profiles.active=${ACTIVE_PROFILE}",
"-Dencryptor.key=${DATASOURCE_KEY}",
"-Dspring.datasource.url=${DATASOURCE_URL}",
"-Dspring.datasource.username=${DATASOURCE_USERNAME}",
"-Dspring.datasource.password=${DATASOURCE_PASSWORD}",
"-Duser.timezone=Asia/Seoul", "ROOT.jar"]

