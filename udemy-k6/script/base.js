import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Trend } from 'k6/metrics';

export const API_URL = __ENV.K6_URL;
export const sessionId = __ENV.K6_SESSION_ID;

export const pollingFailureCounter = new Counter('polling_failure_count');

http.setResponseCallback(http.expectedStatuses({ min: 200, max: 399 }));

const iterations = new Counter('custom_iterations', true);
const iterationDurations = new Trend('custom_iteration_durations', true);

let startTime;
const pollingCount = 20;

export function sendRequest(type, bodyCreator) {
    console.log(`이미지 생성 요청: ${type}`);
    startTime = new Date();
    const serviceUrl = `${API_URL}/api/${type}`;
    const { body, headers } = bodyCreator();

    const tags = {
        type: type,
    };

    const res = http.post(serviceUrl, body, { headers, tags });

    check(res, { '요청 성공': (r) => r.status === 200 }, tags);

    return res.status === 200 ? JSON.parse(res.body).uuid : null;
}

export function pollingMultipleTaskRequests(type, uuid) {
    sleep(2); // 태스크 생성 시간까지 넉넉히 대기
    console.log(`polling 시작: ${type}`);
    let success = false;
    let taskCreated = false;
    const serviceUrl = `${API_URL}/api/${type}`;
    const tags = {
        type: type,
    };

    for (let i = 0; i < pollingCount; i++) {
        const pollingUrl = `${serviceUrl}/polling?id=${uuid}`;
        const pollingRes = http.get(pollingUrl, { tags });

        check(pollingRes, { '폴링 요청 성공': (r) => r.status === 200 }, tags);

        console.log(`Polling request ${i + 1}: Response time for ${uuid}: ${pollingRes.timings.duration}ms`);

        const pollingBody = JSON.parse(pollingRes.body);

        const taskList = pollingBody.list?.[0]?.taskList;

        // 태스크 생성 여부 확인
        if (!taskCreated) {
            if (taskList.length > 0) {
                taskCreated = true;
            }

            check(taskList, { '태스크 생성 성공 유무': (r) => r.length > 0 }, tags);
        }

        // resultUrl 확인
        const resultUrl = taskList?.find(task => task.result?.url)?.result?.url;
        if (resultUrl) {
            console.log(`Success: Result URL is ${resultUrl}`);
            success = true;
            break;
        }

        sleep(2);
    }

    if (!success) {
        pollingFailureCounter.add(1, tags);
        console.log(`Polling failed after ${pollingCount} attempts for ${uuid}`);
    }
}

export function pollingSingleTaskRequests(type, uuid) {
    sleep(2); // 태스크 생성 시간까지 넉넉히 대기
    let success = false;
    let taskCreated = false;
    const serviceUrl = `${API_URL}/api/${type}`;
    const tags = {
        type: type,
    };

    for (let i = 0; i < pollingCount; i++) {
        const pollingUrl = `${serviceUrl}/polling?id=${uuid}`;
        const pollingRes = http.get(pollingUrl, { tags });

        check(pollingRes, { '폴링 요청 성공': (r) => r.status === 200 }, tags);

        console.log(`Polling request ${i + 1}: Response time for ${uuid}: ${pollingRes.timings.duration}ms`);

        const pollingBody = JSON.parse(pollingRes.body);

        const task = pollingBody.list?.[0]?.task;

        // 태스크 생성 여부 확인
        if (!taskCreated) {
            if (task.length > 0) {
                taskCreated = true;
            }

            check(task, { '태스크 생성 성공 유무': (r) => r.length > 0 }, tags);
        }

        // resultUrl 확인
        const resultUrl = task?.result?.url;
        if (resultUrl) {
            console.log(`Success: Result URL is ${resultUrl}`);
            success = true;
            break;
        }

        sleep(2);
    }

    if (!success) {
        pollingFailureCounter.add(1, tags);
        console.log(`Polling failed after ${pollingCount} attempts for ${uuid}`);
    }
}

export function updateIterations(type) {
    const endTime = new Date();
    const duration = (endTime - startTime) / 1000;
    console.log('시나리오 하나 종료');
    const tags = {
        type: type,
    };
    iterations.add(1, tags);
    iterationDurations.add(duration, tags);
}

export function getAuthValue() {
    if (__ENV.K6_SAME_AUTH === 'true') {
        return 'k6-user';
    } else {
        return `k6-${Math.random().toString(36).substring(2, 15)}`;
    }
}