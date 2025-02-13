# 인덱스 추가를 통한 쿼리의 성능개선

## 대상 쿼리 : 인기 상품 5개 조회 쿼리

- accumulated_sold_count를 스케쥴러로 3일 마다 초기화하는 방법으로 구현
- product, product_stock 각각 1000개의 row를 삽입하여 테스트
- product 테이블
    - | id | name   | price | created_date | last_modified_date |
                                                                                                                                                                                                                                                |--------|-------|-------|--------------|-------------------|
      | 1 | 상품명 13 | 52700 | 2025-01-16 15:31:33 | 2025-01-09 07:17:32 |
      | 2 | 상품명 3  | 2700  | 2025-01-16 15:31:33 | 2025-01-09 07:17:32 |
      | 3 | 상품명 56 | 43000 | 2025-01-16 15:31:33 | 2025-01-09 07:17:32 |

- product_stock 테이블
    - | id | product_id | stock_quantity | accumulated_sold_count | created_date | last_modified_date |
                                                                                                                                                                                                                                                    |----|------------|----------------|----------------------|--------------|-------------------|
      | 1 | 1 | 1 | 10 | 2025-01-16 15:31:33 | 2025-01-16 15:31:33 |
      | 2 | 2 | 21 | 1 | 2025-01-16 15:31:33 | 2025-01-09 07:17:32 |
      | 3 | 3 | 3 | 3 | 2025-01-16 15:31:33 | 2025-01-09 07:17:32 |

## 먼저보는 결론

| 방식             | 인덱스                                             | 실행시간    | 스캔 rows | Extra                           |
|----------------|-------------------------------------------------|---------|---------|---------------------------------|
| 초기상태           | product_id만 존재                                  | 6.31ms  | 1000    | Using temporary; Using filesort |
| 단일인덱스          | accumulated_sold_count                          | 5.71ms  | 1000    | Using temporary; Using filesort |
| 복합인덱스 1        | last_modified_date, accumulated_sold_count DESC | 4.58ms  | 1000    | Using temporary; Using filesort |
| 복합인덱스 2        | accumulated_sold_count DESC, product_id         | 4.54ms  | 1000    | Using temporary; Using filesort |
| 서브쿼리           | 없음                                              | 0.511ms | 11      | Using filesort (product_stock만) |
| 서브쿼리 + 복합인덱스 2 | accumulated_sold_count DESC, product_id         | 0.068ms | 11      | Using where                     |

- **초기상태 (6.31ms)**
    - product 전체 테이블 스캔 (1000rows)
    - 각 product마다 product_stock을 찾는 nested loop (1000번)
    - 모든 데이터(1000rows)를 메모리에 올려서 정렬
    - 임시 테이블 생성 필요
- **단일인덱스 accumulated_sold_count (5.71ms)**
    - 정렬할 때 인덱스를 활용할 수 있지만
    - LEFT JOIN 때문에 여전히 product 테이블 스캔 필요
    - 약간의 성능 향상만 있음
- **복합인덱스1,2 (4.5ms)**
    - 정렬과 조인 조건을 모두 커버하려 했지만
    - LEFT JOIN 때문에 여전히 product 테이블 스캔 필요
    - 인덱스가 있어도 전체 결과를 정렬해야 함
- **서브쿼리 방식 (0.511ms)**
    - 순서가 바뀜: 먼저 상위 5개를 선택하고 나서 조인
    - 스캔하는 행이 1000 -> 11개로 감소
    - product 테이블도 5번만 조회
    - 하지만 여전히 filesort 필요
- **서브쿼리 + 복합인덱스 (0.068ms)**
    - accumulated_sold_count DESC로 정렬된 인덱스 바로 사용
    - product_id도 인덱스에 포함되어 조인 최적화
    - filesort 필요 없음 (인덱스가 이미 정렬되어 있음)
    - 총 11개 행만 처리 (TOP 5 + 조인)

결론적으로 서브쿼리 + 복합인덱스 방식이 가장 빠른 이유:
- 서브쿼리에서는 accumulated_sold_count DESC 인덱스를 사용해 상위 5개를 바로 가져옴
- 그 다음 메인쿼리에서 product와 조인할 때는 PRIMARY KEY를 사용

### ✔ 초기 상태 : product_stock에 product_id만 인덱스가 있는 경우

```sql
EXPLAIN
SELECT p.id,
       p.created_date,
       p.last_modified_date,
       p.name,
       p.price,
       ps.id,
       ps.accumulated_sold_count,
       ps.created_date,
       ps.last_modified_date,
       ps.stock_quantity
FROM product p
         LEFT JOIN
     product_stock ps
     ON p.id = ps.product_id
ORDER BY ps.accumulated_sold_count DESC LIMIT 5;
```

| id             | select_type | table | partitions | type   | possible_keys               | key                         | key_len | ref         | rows | filtered | Extra            |
|----------------|-------------|-------|------------|--------|-----------------------------|-----------------------------|---------|-------------|------|----------|------------------|
| 1              | SIMPLE      | p     |            | ALL    |                             |                             |         |             | 1000 | 100      | Using temporary; 
 Using filesort |
| 1              | SIMPLE      | ps    |            | eq_ref | UKhj4kvinsv4h5gi8xi09xbdl46 | UKhj4kvinsv4h5gi8xi09xbdl46 | 9       | hhplus.p.id | 1    | 100      |                  |

- EXPLAIN ANALYZE
    - > Limit: 5 row(s) (actual time=6.31..6.31 rows=5 loops=1)
      -> Sort: ps.accumulated_sold_count DESC, limit input to 5 row(s) per chunk (actual time=6.31..6.31 rows=5 loops=1)
      -> Stream results (cost=452 rows=1000) (actual time=0.108..5.94 rows=1000 loops=1)
      -> Nested loop left join (cost=452 rows=1000) (actual time=0.0868..4.76 rows=1000 loops=1)
      -> Table scan on p (cost=102 rows=1000) (actual time=0.0643..0.742 rows=1000 loops=1)
      -> Single-row index lookup on ps using UKhj4kvinsv4h5gi8xi09xbdl46 (product_id=[p.id](http://p.id/)) (cost=0.25
      rows=1) (actual time=0.00382..0.00386 rows=0.998 loops=1000)


- type
    - ALL : product 테이블에 대해 전체 테이블 스캔을 수행
    - eq_ref : product_stock 테이블에서 unique/primary key를 사용해 조인
- possible_keys & key
    - product 테이블: <null> - 사용 가능한 인덱스가 없음
    - product_stock: UIOj4kv1nsvsh5gl8x109xbd146 - product_id 인덱스 사용
- key_len: product_stock 테이블에서 9바이트 길이의 인덱스 사용
- rows: 1000개의 행을 검사할 것으로 예상 (product 전체 rows)
- filtered: 100% - 조건에 의해 필터링되는 행이 없음
- Extra: Using temporary, Using filesort : 정렬을 위한 임시 테이블 생성과 파일 정렬 사용, 개선 필요

### ✔ accumulated_sold_count 인덱스 추가

- CREATE INDEX idx_acc_sold_count ON product_stock (accumulated_sold_count);

- EXPLAIN 지표는 동일
- EXPLAIN ANALYZE : 6.31 -> 5.71ms 로 빨라짐
    - > Limit: 5 row(s) (actual time=5.71..5.71 rows=5 loops=1)
      -> Sort: ps.accumulated_sold_count DESC, limit input to 5 row(s) per chunk (actual time=5.71..5.71 rows=5 loops=1)
      -> Stream results (cost=452 rows=1000) (actual time=0.102..5.19 rows=1000 loops=1)
      -> Nested loop left join (cost=452 rows=1000) (actual time=0.0948..4.04 rows=1000 loops=1)
      -> Table scan on p (cost=102 rows=1000) (actual time=0.0683..0.695 rows=1000 loops=1)
      -> Single-row index lookup on ps using UKhj4kvinsv4h5gi8xi09xbdl46 (product_id=[p.id](http://p.id/)) (cost=0.25
      rows=1) (actual time=0.00316..0.00318 rows=0.998 loops=1000)

### ✔ last_modified_date, accumulated_sold_count 복합 인덱스 추가

- CREATE INDEX idx_stock_date_sold ON product_stock (last_modified_date, accumulated_sold_count DESC);

- product_stock rows 1개 행 검사, 수행 시간: 4.58ms
- EXPLAIN
- | id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
                                                    | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
  | 1 | SIMPLE | p |  | ALL |  |  |  |  | 1000 | 100 | Using temporary;
  Using filesort |
  | 1 | SIMPLE | ps |  | eq_ref | UKhj4kvinsv4h5gi8xi09xbdl46 | UKhj4kvinsv4h5gi8xi09xbdl46 | 9 | hhplus.p.id | 1 | 100 |  |

- EXPLAIN ANALYZE
    - > -> Limit: 5 row(s)  (actual time=4.58..4.58 rows=5 loops=1)
      -> Sort: ps.accumulated_sold_count DESC, limit input to 5 row(s) per chunk  (actual time=4.57..4.58 rows=5
      loops=1)
      -> Stream results  (cost=452 rows=1000) (actual time=0.0832..4.3 rows=1000 loops=1)
      -> Nested loop left join  (cost=452 rows=1000) (actual time=0.0767..3.55 rows=1000 loops=1)
      -> Table scan on p  (cost=102 rows=1000) (actual time=0.0537..0.467 rows=1000 loops=1)
      -> Single-row index lookup on ps using UKhj4kvinsv4h5gi8xi09xbdl46 (product_id=p.id)  (cost=0.25 rows=1) (actual
      time=0.00294..0.00296 rows=0.998 loops=1000)

### ✔ accumulated_sold_count DESC, product_id 복합 인덱스 추가

- CREATE INDEX idx_accumulated_sold ON product_stock (accumulated_sold_count DESC, product_id);

- product_stock rows 1개 행 검사, 수행 시간: 4.54ms
- EXPLAIN : last_modified_date, accumulated_sold_count 복합 인덱스 추가의 경우와 동일
- EXPLAIN ANALYZE
    - > Limit: 5 row(s)  (actual time=4.54..4.54 rows=5 loops=1)
      -> Sort: ps.accumulated_sold_count DESC, limit input to 5 row(s) per chunk  (actual time=4.54..4.54 rows=5
      loops=1)
      -> Stream results  (cost=452 rows=1000) (actual time=0.102..4.23 rows=1000 loops=1)
      -> Nested loop left join  (cost=452 rows=1000) (actual time=0.095..3.43 rows=1000 loops=1)
      -> Table scan on p  (cost=102 rows=1000) (actual time=0.05..0.542 rows=1000 loops=1)
      -> Single-row index lookup on ps using UKhj4kvinsv4h5gi8xi09xbdl46 (product_id=p.id)  (cost=0.25 rows=1) (actual
      time=0.00271..0.00273 rows=0.998 loops=1000)

### ✔ 서브쿼리로 변경

```sql
SELECT p.id,
       p.created_date,
       p.last_modified_date,
       p.name,
       p.price,
       ps.id,
       ps.accumulated_sold_count,
       ps.created_date,
       ps.last_modified_date,
       ps.stock_quantity
FROM (SELECT *
      FROM product_stock
      ORDER BY accumulated_sold_count DESC LIMIT 5) ps
         JOIN product p
              ON p.id = ps.product_id;
```

- product의 Extra 사라짐, product_stock using filesort 생성
- 수행 속도 : 0.511ms
- EXPLAIN
    - | id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
                                                                            | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
      | 1 | PRIMARY | <derived2> |  | ALL |  |  |  |  | 5 | 100 | Using where |
      | 1 | PRIMARY | p |  | eq_ref | PRIMARY | PRIMARY | 8 | ps.product_id | 1 | 100 |  |
      | 2 | DERIVED | product_stock |  | ALL |  |  |  |  | 1000 | 100 | Using filesort |
- EXPLAIN ANALYZE
    - > Nested loop inner join  (cost=4.81 rows=5) (actual time=0.511..0.546 rows=5 loops=1)
      -> Filter: (ps.product_id is not null)  (cost=82.2..3.06 rows=5) (actual time=0.499..0.5 rows=5 loops=1)
      -> Table scan on ps  (cost=103..105 rows=5) (actual time=0.497..0.498 rows=5 loops=1)
      -> Materialize  (cost=102..102 rows=5) (actual time=0.496..0.496 rows=5 loops=1)
      -> Limit: 5 row(s)  (cost=102 rows=5) (actual time=0.483..0.484 rows=5 loops=1)
      -> Sort: product_stock.accumulated_sold_count DESC, limit input to 5 row(s) per chunk  (cost=102 rows=1000) (
      actual
      time=0.483..0.483 rows=5 loops=1)
      -> Table scan on product_stock  (cost=102 rows=1000) (actual time=0.0744..0.355 rows=1000 loops=1)
      -> Single-row index lookup on p using PRIMARY (id=ps.product_id)  (cost=0.27 rows=1) (actual time=0.00866..0.00869
      rows=1 loops=5)

### ✔ 서브쿼리로 변경 + accumulated_sold_count DESC, product_id 복합 인덱스 추가

- CREATE INDEX idx_accumulated_sold ON product_stock (accumulated_sold_count DESC, product_id);
- product, product_stock extra 사라짐, 임시 테이블에 using where
- 스캔하는 row 전체 11로 감소
- 수행 속도 : 0.511ms
- EXPLAIN
    - | id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
                                                          | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
      | 1 | PRIMARY | <derived2> |  | ALL |  |  |  |  | 5 | 100 | Using where |
      | 1 | PRIMARY | p |  | eq_ref | PRIMARY | PRIMARY | 8 | ps.product_id | 1 | 100 |  |
      | 2 | DERIVED | product_stock |  | index |  | idx_accumulated_sold | 18 |  | 5 | 100 |  |
- EXPLAIN ANALYZE
- > Nested loop inner join  (cost=4.81 rows=5) (actual time=0.0682..0.078 rows=5 loops=1)
  -> Filter: (ps.product_id is not null)  (cost=1.02..3.06 rows=5) (actual time=0.0576..0.0586 rows=5 loops=1)
  -> Table scan on ps  (cost=1.02..3.07 rows=5) (actual time=0.0566..0.0573 rows=5 loops=1)
  -> Materialize  (cost=0.51..0.51 rows=5) (actual time=0.0548..0.0548 rows=5 loops=1)
  -> Limit: 5 row(s)  (cost=0.01 rows=5) (actual time=0.0395..0.0411 rows=5 loops=1)
  -> Index scan on product_stock using idx_accumulated_sold  (cost=0.01 rows=5) (actual time=0.0389..0.0402 rows=5
  loops=1)
  -> Single-row index lookup on p using PRIMARY (id=ps.product_id)  (cost=0.27 rows=1) (actual time=0.00334..0.00337
  rows=1 loops=5)

