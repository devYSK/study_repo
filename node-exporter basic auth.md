# nginx를 이용한 Node-exporter basic auth

Node-exporter는 Prometheus 모니터링에 쓰이는 컴포넌트중 하나입니다. 

우리가 서버를 운영하다 보면, 하드웨어및 OS 리소스 등을 모니터링 해야 할 필요가 생깁니다.

이때, 그라파나-프로메테우스로 모니터링 하는 환경이라면 Node-Exporter를 사용하여 시스템 메트릭을 수집할 수 있습니다. .

그런데, VPC등으로 네트워크 보안 등을 제한하지 않은 상태에서 그라파나나 프로메테우스로 메트릭을 수집하게 되면 이 경우, 누구나 접근 가능한 엔드포인트를 통해 중요한 서버 메트릭을 조회할 수 있게 되어, 보안에 취약점이 발생할 수 있습니다.



VPC나 방화벽 등을 사용할 수 없을 때, 다른 네트워크에 있는 Node-Exporter 등과 같은 컴포넌트의 정보를 가져와야 한다면

모든 네트워크에서 접속할 수 있기 때문에, 추가적인 인증 레이어를 구성해서 보안을 향상시킬 수 있습니다.



nginx basic auth로 프록시를 이용하여 NodeExporter의 메트릭 요청에 basic auth를 설정하여 메트릭을 수집할 수 있는 방법입니다.



> Node-exporter와 nginx를 docker-compose로 컨테이너로 같이 실행시킨다는 상황을 가정합니다. 

1. 우선, Nginx를 설치하고, Node-Exporter를 위한 프록시 서버를 설정합니다. 이 과정에서 Nginx에 Basic Auth 인증을 추가하여, 메트릭 데이터에 접근을 제한할 수 있습니다.
2.  `htpasswd` 명령어를 사용하여 인증에 필요한 사용자명과 비밀번호를 담은 파일을 생성하여 nginx basic auth에 이용할 수 있습니다.
3. Nginx 프록시 설정을 통해, Node-Exporter 엔드포인트로의 모든 요청이 Basic Auth 인증을 거치도록 합니다. 이렇게 하면, 인증되지 않은 접근을 차단할 수 있습니다.

이 방법을 통해, VPC나 방화벽을 사용할 수 없는 상황에서도 Node-Exporter와 같은 모니터링 컴포넌트의 보안을 강화할 수 있습니다.

# 1. node-exporter와 nginx docker-compose 작성

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
      - 9101:9100 # 포트 충돌 방지 위해 9101로 설정합니다.
  nginx:
    image: nginx:latest
    container_name: nginx-proxy
    restart: unless-stopped
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/conf.d:/etc/nginx/conf.d:ro
    ports:
      - "80:80"
      - "443:443"
```

* command 명령은 리눅스 시스템 디렉토리를 지정한것이기 때문에 절대 수정하면 안됩니다.  시스템 메트릭을 수집 못할수도 있습니다. 

추가 설정이 필요하다면 아래 문서를 참고해주세요

* https://grafana.com/docs/grafana-cloud/send-data/metrics/metrics-prometheus/prometheus-config-examples/docker-compose-linux/



## 2. prometheus 설정 config.yml

prometheus.config를 다음과 같이 지정합니다

```yaml
global:
  scrape_interval: 15s     # scrap target의 기본 interval을 15초로 변경 / default = 1m
  scrape_timeout: 15s      # scrap request 가 timeout waite/ default = 10s

  external_labels:
    monitor: 'monitor'       # 기본적으로 붙여줄 라벨
  query_log_file: query_log_file.log # prometheus의 쿼리 로그들을 기록. 설정되지않으면 기록하지않는다.

# 매트릭을 수집할 엔드포인드로 여기선 Prometheus 서버 자신을 가리킨다.
scrape_configs:

 # 이 설정에서 수집한 타임시리즈에 `job=<job_name>`으로 잡의 이름을 설정.
 # metrics_path의 기본 경로는 '/metrics'이고 scheme의 기본값은 `http`다

  - job_name: 'my-node-exporter'
    metrics_path: '/metrics'
    static_configs:
      - targets: ['ip:9100']
    basic_auth:
      username: '유저명'
      password: '비밀번호'
```

* 메트릭을 수집하기 위해 node-exporter가 띄워져있는 서버의 주소와 basicauth를 사용할 유저명:패스워드를 지정합니다. 



## 3. nginx 암호 설정을 위한 basic-auth 생성

먼저 httpd-tools를 다운로드 받습니다 

* `htpasswd`는 basic auth를 위한 패스워드 파일을 생성하고 관리하는 데 사용됩니다. 

```
sudo yum install httpd-tools
```

다음으로, .htpasswd 파일을 생성하고 사용자를 추가합니다.

* 이때 basic auth에 사용할 유저명, 비밀번호를 지정합니다

```
sudo htpasswd -c /etc/nginx/.htpasswd '유저명' # '빼도 된다
~이후 비밀번호 지정 
```



# 4. nginx 설정파일 추가

htpasswd로 생성한 아이디 패스워드 파일을  nginx 설정파일을 추가합니다.

```file
server {
    listen 9100; # Basic Auth를 적용할 포트

    location / {
        auth_basic "Protected Node Exporter";
        auth_basic_user_file /etc/nginx/.htpasswd; # 위에서 htpasswd로 생성한 .htpasswd 파일 

        # Node Exporter로 프록시
        proxy_pass http://localhost:9101; # node_exporter가 실행되고 있는 포트 
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

* node-exporter를 9101에 띄워서 nginx를 통해 baiscauth를 진행하고 우회시키기 위한 설정입니다.

nginx 재실행

```
sudo systemctl restart nginx
# 또는
sudo systemctl reload nginx
```

nginx를 재실행하게 되면 지정한 password와 basic auth 적용이 완료됩니다. 



# 5. nginx에 인증 설정 완료 후 프로메테우스 재실행.



```yaml
global:
  scrape_interval:     5s
  evaluation_interval: 5s

scrape_configs:
  - job_name: 'my-node-exporter'
    metrics_path: '/metrics'
    static_configs:
      - targets: ['ip:9100']
    basic_auth:
      username: '유저명'
      password: '비밀번호'
```

* 위 promerthues.config에서 설정하지 않았다면 지금 지정합니다 



prometheus 재시작 

```
docker-compose restart prometheus
```



이후 promethues를 재실행하게되면 9100 포트를 통해 nginx에서 basic auth를 진행하게 되고,

인증이 통과된다면 9101 포트의 node-exporter로 접근하여 메트릭을 가져올 수 있게 됩니다.  
