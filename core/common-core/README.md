# common-core

--- 

## 모듈 소개

- WEB 서비스에 필요한 코어 모듈을 모아둔다
- 주로 웹 서비스 관련된 의존성을 가지고 있다.

## 모듈 구성

### 설정 클래스

- 필터 설정
- REST Template 설정
- 웹 config 설정 (cors 설정 적용 예정)

### 필터 클래스

- 로깅 필터 클래스
- MDC 필터 클래스

### REST client 클래스

- app 인증 서버 요청 client (connection timeout: 5초, read timeout: 10초) [최대 커넥션 수 50, IP 기준 50개 최대, 3번 재시도]
- 카카오 로그인 client (connection timeout: 5초, read timeout: 15초) [최대 커넥션 수 50, IP 기준 25개 최대, 3번 재시도]
- 구글 로그인 client (connection timeout: 5초, read timeout: 15초) [최대 커넥션 수 50, IP 기준 25개 최대, 3번 재시도]

### wrapper 클래스

- 로깅을 위해 io stream write 후 재사용을 위해 사용

### properties 클래스

- 카카오, 구글 로그인 필요한 properties 관리 클래스 

## 사용방법

- 독립적으로 사용하지 못하며 어플리케이션 모듈에 붙여서 사용한다.

## 주의사항

- jasypt 모듈을 통해서 카카오, 구글 로그인 키 정보를 암호화하여 properties에서 관리중으로 사용하는 어플리케이션 모듈에서는 jasypt Bean 등록을 진행해야한다. (알고리즘, 키, salt, encoding 등의 정보 필요)
