#  레거시 코드 리팩토링

---
## 리펙토링 기간

> 기간 2024.09.26 ~ 2024.10.11

## 내용
1. User 엔티티에 Nickname을 추가했습니다.
2. AOP의 동작이 수정되었습니다.
3. 순수 JPA 사용에서 JPQL, QueryDSL 사용으로 수정되었습니다.
4. 할 일 생성시, 해당 유저는 자동으로 manager가 되는 기능이 추가되었습니다.
5. N+1 문제를 해결했습니다.
6. 인증 인가 방식이 Filter에서  Spring Security로 수정되었습니다.
7. Docker Image와 Hub를 통해 AWS EC2로 배포되었습니다.
8. 인덱스를 통해 nickname을 이용한 조회의 속도를 개선했습니다.

---
# 변경 내용 

### 1. User 엔티티에 Nickname을 추가했습니다.
- Nickname 추가를 위해 SignupRequest에서 nickname을 추가로 입력 받도록 수정되었습니다.
- JWT를 통해 Nickname 출력을 위해 JWT에 Nickname 정보가 포함되도록 `Jwts.builder().claim("nickname",nickname)`가 추가되었습니다.
### 2. AOP의 동작이 수정되었습니다.
- AOP의 목적에 맞춰 메소드 실행전 AOP가 동작하도록 @After -> @Before로 수정했습니다.
- 개발 의도에 맞도록 포인트 컷 및 메소드 명을 수정하였습니다.
### 3. 순수 JPA 사용에서 JPQL, QueryDSL 사용으로 수정되었습니다.
- 할 일 검색시 weather 조건 및 날짜 범위를 추가하기 위해 JPQL을 적용했습니다. 
  - 다중 if문 사용을 막기 위해 JPQL문의 Where문에서 OR을 사용했습니다.
- QueryDSL 사용을 위해 의존성을 추가했습니다.
  - QueryDSL 사용을 위해 TodoRepositoryCustom 인터페이스를 생성했습니다.
  - QueryDSL 사용을 위해 TodoRepositoryImpl 클래스를 생성했습니다.
### 4. 할 일 생성시, 해당 유저는 자동으로 manager가 되는 기능이 추가되었습니다.
- 할 일 생성시, 해당 유저가 자동으로 Manager로 생성되도록 `cascade = CascadeType.PERSIST`옵션을 추가했습니다.
### 5. N+1 문제를 해결했습니다. 
- Fetch join과 적절한 batch size를 통해 N+1 문제를 해결했습니다.
### 6. 인증 인가 방식이 Filter에서  Spring Security로 수정되었습니다.
- Filter에서 사용하던 `FilterConfig`, `JwtFilter`, `AuthUserArgumentResolver` 를 각 `SecurityConfig`,`JwtSecurityFilter`,`JwtAuthenticationToken`로 수정했습니다.
- 변경에 따라 사용하지 않게된 `WebConfig`,`PasswordEncoder`는 삭제되었습니다.
### 7. Docker Image와 Hub를 통해 AWS EC2로 배포되었습니다.
- 과금 문제로 인해 배포가 중단되었습니다.
### 8. 인덱스를 통해 nickname을 이용한 조회의 속도를 개선했습니다.
- Nickname을 대상으로한 user 인덱스를 통해 조회 속도를 개선했습니다.

---

# LV3. 12 AWS
## 헬스 체크로 연동 확인
![포스트맨 헬스체크](https://github.com/user-attachments/assets/2d02a8dd-ab41-4d12-98ca-b7b884b66f16)

[참고사이트 (토스 테그블로그)](https://toss.tech/article/how-to-work-health-check-in-spring-boot-actuator)

## EC2
![인스턴스](https://github.com/user-attachments/assets/80d10116-7321-4ddd-9ebf-c2a5fb666d3b)
## DB
![db](https://github.com/user-attachments/assets/0ad3721f-4e14-4f8e-990e-ca6fc575d63d)
## S3로 이미지 업로드, 삭제, 및 버킷 정책

- 업로드
![스크린샷 2024-10-08 오후 1 49 00](https://github.com/user-attachments/assets/081f49e6-7459-4f5b-96de-f5495f809f4e)

- 삭제
![스크린샷 2024-10-08 오후 1 50 57](https://github.com/user-attachments/assets/8633b9d9-1e3a-405f-b63c-085ead0804a3)

- 정책

![스크린샷 2024-10-08 오후 1 58 10](https://github.com/user-attachments/assets/e95aa1ab-b394-4fe4-9788-8ef687efcd64)

---
# LV3. 13 대용량 데이터 조회

## 방법

1. 인덱스 미적용 findByNickname (JPA)
2. 인덱스 미적용 findByNickname (JPQL)
3. 인덱스 적용 findByNickname (JPA)
4. 인덱스 적용 findByNickname (JPQL)

## 결과
인덱스 적용시 응답 속도가 개선 되었다.

### 성능 개선 결과

| 개선 방법   | 인덱스 미적용 조회 속도 (ms) | 인덱스 적용 후 조회 속도 (ms) |
|---------|--------------------|---------------------|
| JPA 조회  | 498                | 156                 |
| JPQL 조회 | 376                | 147                 |


#### 인덱스 미적용 조회
![인덱스 미적용 바로 조회 498](https://github.com/user-attachments/assets/435dfe96-1759-4180-bf94-e04a5af47d0d)

#### 인덱스 미적용 JQPL 조회
![인덱스 미적용 JPQL조회 376](https://github.com/user-attachments/assets/3acc91d1-947f-43c1-a5f1-ab9a89c49574)

#### 인덱스 적용 조회
![인덱스 적용 바로 조회 156](https://github.com/user-attachments/assets/f69b44fb-7768-4207-aa16-10a8a89788fb)

#### 인덱스 적용 JPQL 조회
![인덱스 적용 JPQL 조회 147](https://github.com/user-attachments/assets/057e3d46-fb7d-4c1c-881d-677dfcb29dc7)


---
# 트러블 슈팅
## 1. Spring Security 도입 후 saveTodo 실행시 User가 영속성이 아닌 오류 발생
### 원인 
- User 생성자 수정시 Id를 입력받지 않게 수정해서 생성되는 User의 id가 null이 되어 DB에서 조회가 불가능해 영속성 컨텍스트의 1차 캐시에 저장되지 않음

### 해결 
- 생성자에 id를 추가해 정상적으로 조회가 가능하도록 수정하니 오류 해결됨.

## 2. JAR 파일 실행시 환경변수 인식 실패
### 원인 
- .env 파일이 application.yaml에 적용되지 않았음

### 해결 
-`spring: config: import: optional:file:.env[.properties]`를 통해 정확하게 연결함

## 3. JAR 파일 실행시 DB 연결 실패
### 원인
- 로컬 DB가 연결되어 있는 application-local.yaml을 배포했음
### 해결
- 로컬 DB를 RDS로 변경 후 실행하니 정상 작동함 

## 4. Docker 이미지 생성시 환경 변수 인식 실패
### 원인
- Docker 이미지는 .env 파일을 인식하지 못해 `JWT_SECRET_KEY`과 같은 변수를 파악하지 못함.
### 임시 해결
- 임시 방편으로 `COPY .env .env`를 통해 이미지 자체에 env 파일을 복사했지만, 보안에 문제가 있다고 판단. 