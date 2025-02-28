import http from 'k6/http';
import {check, sleep} from 'k6';
import {Counter, Rate, Trend} from 'k6/metrics';
import {SharedArray} from 'k6/data';

const apiResponseTime = new Trend('api_response_time');
const apiRequestsRate = new Rate('api_requests_rate');
const apiErrors = new Counter('api_errors');

const userIds = new SharedArray('users', function () {
    const users = [];
    for (let i = 1; i <= 100; i++) {
        users.push(i);
    }
    return users;
});

// Test configuration
export const options = {
    stages: [
        {duration: '30s', target: 50},
        {duration: '2m', target: 1000},   // 2분 동안 명의 사용자 1000명 유지
        {duration: '30s', target: 50},
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'], // 요청의 95%가 500ms 이내에 완료되어야 함
        'http_req_failed': ['rate<0.01'],   // 요청의 1% 미만이 실패해야 함
    },
};

const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
    const userId = userIds[Math.floor(Math.random() * userIds.length)];

    let postResponse = http.post(`${BASE_URL}/coupon/${userId}?couponId=1`, null, {
        headers: {'Content-Type': 'application/json'},
    });

    check(postResponse, {
        'POST status is 200': (r) => r.status === 200,
    });
    apiResponseTime.add(postResponse.timings.duration);
    apiRequestsRate.add(1);
    if (postResponse.status !== 200) {
        apiErrors.add(1);
    }

    sleep(Math.random() * 3 + 1); // 1-4초 사이의 랜덤 대기 시간
}