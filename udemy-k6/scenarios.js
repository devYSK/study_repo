import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

// https://grafana.com/docs/k6/latest/using-k6/metrics/reference/
export const options = {
    // 테스트 시 동시 가상 유저(Virtual Users)의 수를 10명으로 설정
    vus: 10, 
    
    // 테스트의 실행 시간을 10초로 설정
    duration: '10s', 
    
    // 성능 기준(thresholds)을 설정하여, 특정 조건을 만족해야 테스트 성공으로 간주
    thresholds: {
        // HTTP 요청의 95%가 100ms 미만의 응답 시간을 가져야 함
        http_req_duration: ['p(95)<100'], 
        
        // 실패한 HTTP 요청의 비율이 전체 요청의 1% 미만이어야 함
        http_req_failed: ['rate<0.01']
    }
}


export default function () {
    const res = http.get('https://test.k6.io/');
    check(res, {
        'status is 200': (r) => r.status === 200,
        'page is startpage': (r) => r.body.includes('Collection of simple web-pages')
    });
    sleep(2);
}