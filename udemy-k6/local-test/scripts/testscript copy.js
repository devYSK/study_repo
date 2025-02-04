import http from 'k6/http';
import { check, sleep, Trend } from 'k6';

export let options = {
  stages: [
    { duration: '10s', target: 50 }, // 10초 동안 사용자 수를 50으로 증가
    { duration: '30s', target: 50 }, // 30초 동안 50명의 사용자 유지
    { duration: '20s', target: 25 }, // 20초 동안 사용자 수를 25로 감소
    { duration: '10s', target: 0 },  // 10초 동안 사용자 수를 0으로 감소
  ],
};

// Trend 객체 정의
const loginTrend = new Trend('Login');
const getUsersTrend = new Trend('GET /users');
const getCurrentDateTrend = new Trend('GET /users/currentDate');
const forceGlobalExceptionTrend = new Trend('GET /users/forceGlobalException');
const forceExceptionTrend = new Trend('GET /users/forceException');
const createUserTrend = new Trend('POST /users/create');
const updateUserTrend = new Trend('PUT /users/:id');
const deleteUserTrend = new Trend('DELETE /users/:id');

// 사용자 로그인 상태를 저장
let loggedIn = false;

// 로그인 함수
function login() {
  const payload = JSON.stringify({ username: 'user', password: 'password' });
  const headers = { 'Content-Type': 'application/json', accept: '*/*' };
  const res = http.post('http://host.docker.internal:8080/login', payload, { headers });

  // Trend에 로그인 응답 시간 기록
  loginTrend.add(res.timings.duration);

  if (res.status === 200 && JSON.parse(res.body).message === 'Login successful') {
    loggedIn = true;
    console.log('로그인 성공');
  } else {
    console.log(`로그인 실패: ${res.body}`);
    loggedIn = false;
  }
}

// 각 작업(task)을 함수로 정의
export default function () {
  // 로그인 시도
  if (!loggedIn) {
    login();
  }

  if (loggedIn) {
    // GET /users
    const getUsersRes = http.get('http://host.docker.internal:8080/users', { headers: { accept: '*/*' } });
    getUsersTrend.add(getUsersRes.timings.duration);
    check(getUsersRes, { 'GET /users 성공': (res) => res.status === 200 });
    console.log('get_users');

    // GET /users/currentDate
    const getCurrentDateRes = http.get('http://host.docker.internal:8080/users/currentDate', { headers: { accept: '*/*' } });
    getCurrentDateTrend.add(getCurrentDateRes.timings.duration);
    check(getCurrentDateRes, { 'GET /users/currentDate 성공': (res) => res.status === 200 });
    console.log('get_current_date');

    // GET /users/forceGlobalException
    const forceGlobalExceptionRes = http.get('http://host.docker.internal:8080/users/forceGlobalException', { headers: { accept: '*/*' } });
    forceGlobalExceptionTrend.add(forceGlobalExceptionRes.timings.duration);
    check(forceGlobalExceptionRes, { 'GET /users/forceGlobalException 성공': (res) => res.status === 500 });
    console.log('force_global_exception');

    // GET /users/forceException
    const forceExceptionRes = http.get('http://host.docker.internal:8080/users/forceException', { headers: { accept: '*/*' } });
    forceExceptionTrend.add(forceExceptionRes.timings.duration);
    check(forceExceptionRes, { 'GET /users/forceException 성공': (res) => res.status === 500 });
    console.log('force_exception');

    // POST /users/create
    const createUserRes = http.post(
      'http://host.docker.internal:8080/users/create?name=John%20Doe&email=john.doe%40example.com',
      null,
      { headers: { accept: '*/*' } }
    );
    createUserTrend.add(createUserRes.timings.duration);
    check(createUserRes, { 'POST /users/create 성공': (res) => res.status === 201 });
    console.log('create_user');

    // PUT /users/1
    const updateUserRes = http.put(
      'http://host.docker.internal:8080/users/1?name=Jane%20Doe1&email=jane.doe%40example.com1',
      null,
      { headers: { accept: '*/*' } }
    );
    updateUserTrend.add(updateUserRes.timings.duration);
    check(updateUserRes, { 'PUT /users/:id 성공': (res) => res.status === 200 });
    console.log('update_user');

    // DELETE /users/{user_id}
    for (let userId = 1; userId <= 10; userId++) {
      const deleteUserRes = http.del(`http://host.docker.internal:8080/users/${userId}`, null, { headers: { accept: '*/*' } });
      deleteUserTrend.add(deleteUserRes.timings.duration);
      check(deleteUserRes, { [`DELETE /users/${userId} 성공`]: (res) => res.status === 200 });
      console.log(`delete_user ${userId}`);
    }
  } else {
    console.log('로그인 실패로 작업을 수행할 수 없습니다.');
  }

  // 작업 간 지연 추가
  sleep(Math.random() * 5 + 1); // 1~5초 대기
}
