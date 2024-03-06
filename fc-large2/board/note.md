# 게시판
[toc]


# 성능테스트

성능테스트 툴 (https://docs.locust.io/en/stable/index.html)

로커스트 설치. 

```
pip3 install locust
```
성능 테스트 시나리오에 따른 .py 파일 작성
locust -f BoardServer.py 파일 실행
http://localhost:8089/ 접속 후 성능 테스트 진행
사전준비

게시글 카테고리 10개 생성
게시글 10만개 생성
게시글 검색 API 각 시나리오 테스트별 진행
목표: 서버의 지표 CPU, RAM, DISK 와 목표 TPS 확인

시나리오

STRESS 테스트
500명의 동시 사용자가 초당 50번을 호출하여 분당(5분) 사용자를 50씩 늘려 서버의 지표를 확인
Endurance 테스트
100명의 동시 사용자가 초당 100번을 호출하였을때 10분동안 서버의 지표를 확인
PEAK 테스트
100명의 동시 사용자가 초당 50번씩 호출하다 1분에 1000명으로 사용자를 한번에 늘려 서버의 지표를 확인



```python
from locust import HttpUser, task, between
import random


class AddPosts(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/users/sign-in", json={"userId": "topojs9",
                                                 "password": "123"})

    @task
    def add_post(self):
        self.client.post("/posts", json={
            "name": "테스트 게시글" + str(random.randint(1, 100000)),
            "contents": "테스트 컨텐츠" + str(random.randint(1, 100000)),
            "categoryId": random.randint(1, 10),
            "fileId": random.randint(1, 10),
        })
```

```python
import json

from locust import HttpUser, task, between
import random


class BoardServer(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/users/sign-in", json={"userId": "topojs9",
                                                 "password": "123"})

    @task(3)
    def view_item(self):
        sortStatus = random.choice(["CATEGORIES", "NEWEST", "OLDEST", "HIGHPRICE", "LOWPRICE", "GRADE"])
        categoryId = random.randint(1, 10)
        name = '테스트 게시글'.join(str(random.randint(1, 10000)))
        headers = {'Content-Type': 'application/json'}
        data = {"sortStatus": sortStatus,
                "categoryId": categoryId,
                "name": name}
        # print(data)
        self.client.post("/search", json=data, headers=headers)
```