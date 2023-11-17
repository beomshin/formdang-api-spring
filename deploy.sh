#!/bin/bash

cd /home/sp/deploy/web

# app.jar 파일이 있는지 확인
if [ -e app.jar ]; then
    # app.jar 파일이 있다면 해쉬값 계산
    hash_value=$(md5sum app.jar | awk '{ print $1 }')

    # backup 디렉토리 생성 (이미 있다면 무시)
    mkdir -p backup

    # 백업 파일 경로 설정
    backup_file="backup/app-${hash_value}.jar"

    # 백업 실행
    cp app.jar "$backup_file"

    echo "백업이 완료되었습니다. 백업 파일: $backup_file"

else
    echo "app.jar 파일이 현재 디렉토리에 존재하지 않습니다."
fi


cp /home/sp/source/web/web-api/target/*.jar app.jar

echo "app.jar 생성 완료"