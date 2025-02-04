import http from 'k6/http';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

export let options = {
  stages: [
    { duration: '10s', target: 50 },
    { duration: '30s', target: 50 },
    { duration: '20s', target: 25 },
    { duration: '10s', target: 0 },
  ],
};

const loginTrend = new Trend('Login');
const getUsersTrend = new Trend('GET_users');
const getCurrentDateTrend = new Trend('GET_users_currentDate');
const forceGlobalExceptionTrend = new Trend('GET_users_forceGlobalException');
const forceExceptionTrend = new Trend('GET_users_forceException');
const createUserTrend = new Trend('POST_users_create');
const updateUserTrend = new Trend('PUT_users_id');
const deleteUserTrend = new Trend('DELETE_users_id');

let loggedIn = false;

function login() {
  const payload = JSON.stringify({ username: 'user', password: 'password' });
  const headers = { 'Content-Type': 'application/json', accept: '*/*' };
  const res = http.post('http://host.docker.internal:8080/login', payload, { headers });

  loginTrend.add(res.timings.duration);

  if (res.status === 200) {
    try {
      const body = JSON.parse(res.body || '{}');
      if (body.message === 'Login successful') {
        loggedIn = true;
        console.log('로그인 성공');
      } else {
        console.log(`로그인 실패: ${res.body}`);
      }
    } catch (e) {
      console.error(`JSON 파싱 오류: ${e.message}`);
    }
  } else {
    console.error(`로그인 요청 실패, 상태 코드: ${res.status}`);
  }
}

export default function () {
  if (!loggedIn) {
    login();
  }

  if (loggedIn) {
    const headers = { accept: '*/*' };

    // GET /users
    const getUsersRes = http.get('http://host.docker.internal:8080/users', { headers });
    getUsersTrend.add(getUsersRes.timings.duration);
    check(getUsersRes, { 'GET_users 성공': (res) => res.status === 200 });
    console.log('get_users');

    // GET /users/currentDate
    const getCurrentDateRes = http.get('http://host.docker.internal:8080/users/currentDate', { headers });
    getCurrentDateTrend.add(getCurrentDateRes.timings.duration);
    check(getCurrentDateRes, { 'GET_users_currentDate 성공': (res) => res.status === 200 });
    console.log('get_current_date');

    // GET /users/forceGlobalException
    const forceGlobalExceptionRes = http.get('http://host.docker.internal:8080/users/forceGlobalException', { headers });
    forceGlobalExceptionTrend.add(forceGlobalExceptionRes.timings.duration);
    check(forceGlobalExceptionRes, { 'GET_users_forceGlobalException 성공': (res) => res.status === 500 });
    console.log('force_global_exception');

    // GET /users/forceException
    const forceExceptionRes = http.get('http://host.docker.internal:8080/users/forceException', { headers });
    forceExceptionTrend.add(forceExceptionRes.timings.duration);
    check(forceExceptionRes, { 'GET_users_forceException 성공': (res) => res.status === 500 });
    console.log('force_exception');

    // POST /users/create
    const createUserRes = http.post(
      'http://host.docker.internal:8080/users/create?name=John%20Doe&email=john.doe%40example.com',
      null,
      { headers }
    );
    createUserTrend.add(createUserRes.timings.duration);
    check(createUserRes, { 'POST_users_create 성공': (res) => res.status === 201 });
    console.log('create_user');

    // PUT /users/1
    const updateUserRes = http.put(
      'http://host.docker.internal:8080/users/1?name=Jane%20Doe1&email=jane.doe%40example.com1',
      null,
      { headers }
    );
    updateUserTrend.add(updateUserRes.timings.duration);
    check(updateUserRes, { 'PUT_users_id 성공': (res) => res.status === 200 });
    console.log('update_user');

    // DELETE /users/{user_id}
    for (let userId = 1; userId <= 10; userId++) {
      const deleteUserRes = http.del(`http://host.docker.internal:8080/users/${userId}`, null, { headers });
      deleteUserTrend.add(deleteUserRes.timings.duration);
      check(deleteUserRes, { [`DELETE_users_${userId} 성공`]: (res) => res.status === 200 });
      console.log(`delete_user ${userId}`);
    }
  } else {
    console.log('로그인 실패로 작업을 수행할 수 없습니다.');
  }
}
