# nGridner 도커로 설치





# 도커로 설치

### **Docker로 nGrinder 실행**

아래 링크를 통해 쉽게 Docker로 실행하는 방법과 설명을 알 수 있다.

https://hub.docker.com/r/ngrinder/controller

 

Docker Hub

 

hub.docker.com

https://hub.docker.com/r/ngrinder/agent

 

Docker Hub

 

hub.docker.com

 

 

Controller 설치

```
% docker pull ngrinder/controller
Using default tag: latest
latest: Pulling from ngrinder/controller
6c40cc604d8e: Pull complete
93e89d51b14f: Pull complete
a9db8812b63a: Pull complete
3e0299ece4cc: Pull complete
018e62743d76: Pull complete
316cff05d08b: Pull complete
879040fac2a6: Pull complete
f05be963a0b6: Pull complete
Digest: sha256:bb669e6c63313337d50917554b38ce801e4300911a3ec8be99e1d3c3e64dd417
Status: Downloaded newer image for ngrinder/controller:latest
docker.io/ngrinder/controller:latest
```

Controller 실행

```
% docker run -d -v ~/ngrinder-controller:/opt/ngrinder-controller --name controller -p 80:80 -p 16001:16001 -p 12000-12009:12000-12009 ngrinder/controller
```

- 80 : Controller 웹 UI 포트
- 9010-9019 : Agent를 Controller에 붙도록 해주는 연결 포트
- 12000-12029 : Controller가 스트레스 테스트에 할당하는 포트

 

 

 

Agent 설치

```
% docker pull ngrinder/agent
Using default tag: latest
latest: Pulling from ngrinder/agent
6c40cc604d8e: Already exists
93e89d51b14f: Already exists
a9db8812b63a: Already exists
2c70c2da1dc5: Pull complete
61c897f05bc4: Pull complete
Digest: sha256:d6bf1c1c3eedf19d0946db224d8c19f71bf47aa9426ac8b6e37bbd15a4936baf
Status: Downloaded newer image for ngrinder/agent:latest
docker.io/ngrinder/agent:latest
```

Agent 실행

```
% docker run -d --name agent --link controller:controller ngrinder/agent
```

 

 

 

Docker 실행 컨테이너 확인

```
% docker ps
CONTAINER ID   IMAGE                 COMMAND             CREATED              STATUS              PORTS                                                                                NAMES
4515042cc9bb   ngrinder/agent        "/scripts/run.sh"   35 seconds ago       Up 35 seconds                                                                                            agent
a2f2cbbc8ca3   ngrinder/controller   "/scripts/run.sh"   About a minute ago   Up About a minute   0.0.0.0:80->80/tcp, 0.0.0.0:12000-12009->12000-12009/tcp, 0.0.0.0:16001->16001/tcp   controller
```

 