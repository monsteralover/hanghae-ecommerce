# 부하 테스트 시나리오 및 결과 분석

## 부하 테스트 시나리오

- 30초 간 사용자 50명이 쿠폰 발급 요청
- 2분 간 1000명 요청 (고부하 상태 유지)
- 마지막 30초 간 50명이 쿠폰 발급 요청

### 테스트 환경

- 테스트를 위해 극단적으로 낮게 설정
- CPU: 0.2
- 메모리: 128MB
- hikari maximum-pool-size : 3
- tomcat max-threads : 200 (기본값)
- tomcat accept-count : 100 (기본값)

## 테스트 결과 분석

![load-test-metric-dashboard-before.png](docs%2Fload-test-image%2Fload-test-metric-dashboard-before.png)
![load-test-http-dashboard-before.png](docs%2Fload-test-image%2Fload-test-http-dashboard-before.png)

### HTTP 요청 응답 시간

- 평균: 24.45초
- 최대: 1.00분
- 중앙값: 23.91초
- p90: 47.09초
- p95: 55.18초

### 가상 사용자

- 최대 약 800-1000명까지 증가

### 초당 요청 수

- 피크 부하 시 약 20-40 RPS
- 부하가 증가함에 따라 성능 저하가 뚜렷함

### HTTP 요청 차단 시간

- 평균: 3.20ms
- 최대: 155.98ms (connection 지연 발생)

### 컨테이너 모니터링

- CPU 사용률이 08:44:00경 약 100%까지 상승
- 네트워크 트래픽이 높음
- 메모리 사용량은 특정 컨테이너(influxdb, grafana)에서 안정적이지만 높음

## 서버 실패 원인

1. **응답 시간 저하:**
    - 평균 응답 시간이 24.45초로, 목표 임계값 500ms를 크게 초과
    - p95 응답 시간은 55.18초로 임계값의 100배 이상
2. **연결 병목 현상:**
    - HTTP 요청 차단 시간 증가는 connectionPool 포화 상태를 나타냄
    - 서버가 연결 요청 속도를 처리할 수 없음을 의미
3. **리소스 제약:**
    - CPU 사용률이 100%까지 급증하여 CPU 병목 현상 발생

# 장애 보고서 (병목 해결 과정)

# 변경된 테스트 환경  V1

- ConnectionPool 증가
- CPU: 0.2
- 메모리: 128MB
- hikari maximum-pool-size : 3 ⇒ 20
- tomcat max-threads : 200 ⇒ 400
- tomcat accept-count : 100 ⇒200

![load-test-http-dashboard-v1.png](docs%2Fload-test-image%2Fload-test-http-dashboard-v1.png)
![load-test-metric-dashboard-v1.png](docs%2Fload-test-image%2Fload-test-metric-dashboard-v1.png)

## HTTP 요청 응답 시간

- 평균: 24.72초
- 최대: 1.00분
- 중앙값: 21.36초
- p90: 56.08초
- p95: 60.00초

## 초당 요청 수

- 피크 부하 시 약 15-30 RPS
- 이전 테스트보다 약간 낮은 RPS 보임 (평균 14.3 RPS)

## HTTP 요청 차단 시간

- 평균: 3.17ms
- 최대: 159.68ms (connection 지연 발생)

## 컨테이너 모니터링

- CPU 사용률이 최대 약 40%까지만 상승
- 네트워크 트래픽이 일정하게 유지됨

## 서버 개선 결과 분석

1. **리소스 사용 개선:**
    - CPU 사용률이 100%에서 40%로 크게 감소하여 CPU 병목 현상이 해소됨
    - 설정한 Tomcat max-threads 증가(400)가 CPU 리소스를 더 효율적으로 활용하게 함
2. **응답 시간 상태:**
    - 평균 응답 시간은 24.72초로 여전히 높음
    - p95 응답 시간은 60.00초로 약간 증가 (이전 55.18초)
3. **연결 관리 개선:**
    - HTTP 요청 차단 시간이 평균 3.17ms로 약간 개선됨 (이전 3.20ms)
    - 증가된 connection pool과 accept-count가 연결 관리를 개선했지만 여전히 높은 지연 발생

# 변경된 테스트 환경 V2

- ConnectionPool 증가
- CPU 증설 1
- CPU: 0.2
- 메모리: 128MB
- hikari maximum-pool-size : 3 ⇒ 20
- tomcat max-threads : 200 ⇒ 400
- tomcat accept-count : 100 ⇒200

![load-test-http-dashboard-v2.png](docs%2Fload-test-image%2Fload-test-http-dashboard-v2.png)
![load-test-metric-dashboard-v2.png](docs%2Fload-test-image%2Fload-test-metric-dashboard-v2.png)

## HTTP 요청 응답 시간

- 평균: 3.13초
- 최대: 20.23초
- 중앙값: 2.10초
- p90: 7.47초
- p95: 8.84초

## 초당 요청 수

- 피크 부하 시 약 125-175 RPS
- 평균 74.5 RPS로 이전 테스트보다 크게 증가 (이전 14.3 RPS)

## HTTP 요청 차단 시간

- 평균: 9.02ms
- 최대: 1.07초
- p95: 21.80ms

## 컨테이너 모니터링

- CPU 사용률이 최대 약 60-70%까지 상승
- 네트워크 트래픽이 상당히 증가함 (최대 약 300 kB/s 수신, 800 kB/s 송신)
- 메모리 사용량은 안정적으로 유지됨

## 서버 개선 결과 분석

1. **응답 시간 대폭 개선:**
   - 평균 응답 시간이 24.72초에서 3.13초로 약 87% 감소
   - p95 응답 시간도 60.00초에서 8.84초로 약 85% 감소
2. **처리량 대폭 증가:**
   - 초당 요청 수가 평균 14.3 RPS에서 74.5 RPS로 약 5배 증가
   - 피크 RPS도 30에서 175까지 약 5.8배 증가
3. **리소스 활용 최적화:**
   - CPU 사용률이 적절하게 분산됨(최대 60-70%)
   - 네트워크 처리량이 크게 증가하여 시스템 성능이 효율적으로 활용됨