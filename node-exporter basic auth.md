# Node-exporter basic auth



1. docker-compose 작성

```yaml
services:
  node-exporter:
    image: prom/node-exporter:latest
    container_name: node-exporter
    restart: unless-stopped
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    command:
      - '--path.procfs=/host/proc'
      - '--path.rootfs=/rootfs'
      - '--path.sysfs=/host/sys'
      - '--collector.filesystem.mount-points-exclude=^/(sys|proc|dev|host|etc)($$|/)'
    ports:
      - 9101:9100 # 9100으로 요청을 받을것이기 때문에 포트 충돌 방지 위해 9100
```

* docker-compose.yml 파일을 아래와 같이 작성하고, conf 폴더 하위에 prometheus.yml 파일을 작성한 후 docker-compose up -d 명령어를 통해 실행한다.

* https://grafana.com/docs/grafana-cloud/send-data/metrics/metrics-prometheus/prometheus-config-examples/docker-compose-linux/

  

2. nginx 암호 설정을 위한 basic-auth 생성

```shell
1. 다운로드
sudo yum install httpd-tools

2. .htpasswd 파일을 생성하고 사용자를 추가

sudo htpasswd -c /etc/nginx/.htpasswd '유저명' # '빼도 된다
이후 비밀번호 지정 
```





3. nginx 설정파일 추가

```file
server {
    listen 9100; # Basic Auth를 적용할 포트

    location / {
        auth_basic "Protected Node Exporter";
        auth_basic_user_file /etc/nginx/.htpasswd;

        # Node Exporter로 프록시
        proxy_pass http://localhost:9101;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```



4. restart

```
sudo systemctl restart nginx
# 또는
sudo systemctl reload nginx
```



basic auth 적용 완료 





5. 프로메테우스 설정 변경

```yaml
global:
  scrape_interval:     5s
  evaluation_interval: 5s

scrape_configs:
  - job_name: node
    metrics_path: /metrics
    static_configs:
      - targets:
        - 10.120.201.2:9100
    basic_auth:
      username: '아이디'
      password: '패스워드'
```



6. docker-compose 재시작

```
docker-compose restart prometheus
```

