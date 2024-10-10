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




# LV3. 13 대용량 데이터 조회

## 방법

1. 인덱스 미적용 findByNickname
2. 인덱스 적용 findByNickname

## 결과

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
