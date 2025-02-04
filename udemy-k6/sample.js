import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

// Custom metrics to track HTTP/1.1 and HTTP/2 response times
const http1Duration = new Trend('http1_req_duration');
const http2Duration = new Trend('http2_req_duration');

// 테스트 옵션 설정
export let options = {
    stages: [
        { duration: '5s', target: 50 }, // 30초 동안 VU를 50개로 증가
        { duration: '10s', target: 50 }, // 50개의 VU를 1분 동안 유지
        { duration: '5s', target: 0 }, // 30초 동안 VU를 0으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<200'], // 95%의 요청은 200ms 이하로 완료되어야 함
    },
};

export default function () {
  const url = 'https://dev-api.owwapp.com/api/v1/workouts';

  // HTTP/1.1 Request
  const responseHttp1 = http.get(url, {
      headers: {
          'Connection': 'keep-alive',
      },
      tags: { protocol: 'http1.1' }, // Add a tag for filtering results
  });
  check(responseHttp1, {
      'HTTP/1.1 상태 코드가 200인지 확인': (r) => r.status === 200,
  });
  http1Duration.add(responseHttp1.timings.duration);

  // HTTP/2 Request
  const responseHttp2 = http.get(url, {
      headers: {
          'Connection': 'keep-alive',
      },
      http2: true, // Enable HTTP/2
      tags: { protocol: 'http2' }, // Add a tag for filtering results
  });
  check(responseHttp2, {
      'HTTP/2 상태 코드가 200인지 확인': (r) => r.status === 200,
  });
  http2Duration.add(responseHttp2.timings.duration);

  sleep(1);
}