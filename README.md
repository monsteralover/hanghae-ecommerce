캐시 전략 분석 및 구현 보고서

1. 캐시의 본질
   캐시는 자주 사용되는 데이터를 빠르게 접근할 수 있는 임시 저장소입니다.
   데이터가 자주 변경되지 않는 경우에 사용하는 것이 좋기 때문에 쿠폰 수량은 캐시로 관리하기에 적합합니다.
   Redis를 캐시로 활용하여 쿠폰의 남은 수량을 관리하면 데이터베이스 접근을 줄이고 성능을 향상시킬 수 있습니다.

2. 쿠폰 시스템의 캐시 전략 분석
   현재 코드에서는 Read-Through 패턴을 사용합니다.

Read-Through 패턴

수량 조회 시 캐시 미스가 발생하면 자동으로 DB에서 데이터를 로드하여 캐시에 저장
캐시와 DB의 일관성을 유지하기 쉬움
첫 요청 시 지연시간이 발생할 수 있음

Write-Through 패턴

수량 감소 시 캐시를 먼저 업데이트하고 동기적으로 DB 반영
데이터 일관성이 매우 중요한 시스템에 적합
모든 쓰기 작업이 DB까지 도달해야 하므로 지연 시간 발생

Cache-Aside 패턴

애플리케이션이 직접 캐시를 관리하며 간단한 조회에 적합
구현이 단순하고 유연함
캐시 미스 시 직접 DB 조회 필요

Read-Through 사용 이유

Write-Through는 매 쓰기 작업마다 DB 동기화가 필요해 성능 저하 발생
Write-Behind는 장애 상황에서 데이터 유실 위험이 있음
쿠폰 수량은 정확성이 중요하므로, 읽기 작업에서만 캐시를 활용

Write-Behind 패턴

캐시 업데이트를 모아서 배치로 DB에 반영, 대량 처리에 효과적
성능은 좋지만 데이터 유실 가능성 있음

3. 캐시 스탬피드 (Cache Stampede)
   캐시 스탬피드는 캐시가 동시에 만료되어 다수의 요청이 동시에 DB에 접근하는 현상입니다.
   예를 들어:

인기 쿠폰의 캐시가 만료됨
수천 명의 사용자가 동시에 쿠폰을 조회
모든 요청이 DB에 접근하여 서버에 과부하 발생

방지 전략: 분산 락 사용

public CouponIssueResponse issueCouponForUser(Long userId, Long couponId) {
String cacheKey = CACHE_KEY_PREFIX + couponId;

    // Redisson을 통한 분산 락으로 동시 접근 제어
    RLock lock = redissonClient.getLock(LOCK_PREFIX + couponId);

    
    // Redis의 원자적 연산으로 경쟁 상태 방지
    Long newQuantity = redisTemplate.opsForValue().decrement(cacheKey);
}

4. 성능 개선 포인트

쿠폰 수량 조회의 캐시화

높은 조회 빈도에 대응
데이터 일관성 요구사항이 상대적으로 낮음
Redis의 원자적 연산을 활용한 안전한 수량 관리

