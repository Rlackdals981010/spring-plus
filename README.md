# LV3. 12 AWS

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
