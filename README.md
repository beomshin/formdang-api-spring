## 프로젝트 구성

- web-api
  - api 모듈

## 프로젝트 실행

- 디비 접근 가능 상태 처리 (AWS 방화벽 설정)

## 빌드 및 배포 방법

- AWS, S3, codeDeploy, EC2 배포 자동화 처리
- CI / CD 
- GITHUB ACTION 통해 처리
- 순서
  - github workflows deploy.yml 실행
  - s3 zip 파일 업로드
  - ec2 jar 파일 배포
  - appspec.yml hooks 실행
  - deploy.sh 쉘 스크립트 실행
  - Dockerfile 실행 후 도커 이미지 등록 및 배포

## 기타

