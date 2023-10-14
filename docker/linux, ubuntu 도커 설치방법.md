# linux, ubuntu docker 설치방법

1. ubuntu
2. amazon linux2

3. cent os7

# ubuntu 도커 설치

* 버전 Ubuntu 18.04

버전 확인 방법

```
lsb_release -a
```

결과

```
No LSB modules are available.
Distributor ID:	Ubuntu
Description:	Ubuntu 18.04.5 LTS
Release:	18.04
Codename:	bionic
```

- Ubuntu 22.04

**패키지 업데이트**

```bash
sudo apt-get update
```

**패키지 설치**

```bash
sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```

**Docker의 공식 GPG키를 추가**

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

**Docker의 공식 apt 저장소를 추가**

```bash
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

**패키지 업데이트**

```bash
sudo apt-get update
```

**Docker 설치**

```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io
```

**도커 실행상태 확인**

```bash
sudo systemctl status docker
```

**Docker-compose 설치**

```shell
sudo apt-get install docker-compose
```

>  Permission 에러 발생할 시 `sudo usermod -aG docker $USER` 하고, 터미널을 나갔다가 들어와야 합니다.

# Amazon linux2 도커 설치

패키지 업데이트

```shell
sudo yum update -y
```

**yum으로 Docker 설치**

```
sudo yum install docker -y
```

**Docker-compose 설치**

```shell
sudo yum install docker-compose
```

Docker 데몬 소켓 파일 권한 변경

```shell
sudo chmod 666 /var/run/docker.sock
```

**or Docker 그룹에 인스턴스 접속 후 도커 바로 제어할 수 있도록 sudo 추가** 

```shell
sudo usermod -aG docker ec2-user
```

**Docker 서비스 시작**

```shell
sudo service docker start
```

#### 도커 실행상태 확인

```bash
sudo systemctl status docker
```



# CentOs7 도커 설치

**패키지 업데이트**

```shell
sudo yum update -y
```

**필요한 패키지 설치**

```shell
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

**Docker 저장소 설정**

```shell
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

**Docker 설치**

```shell
sudo yum install docker-ce docker-ce-cli containerd.io
```

**docker-compose 설치**

```shell
sudo yum install docker-compose
```



or

```shell
yum update -y

yum install -y yum-utils

yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y
```

