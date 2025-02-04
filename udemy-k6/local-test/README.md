

# granafa, influx 실행
docker-compose up -d influxdb grafana

# 실행 
docker-compose exec k6 run --out influxdb=http://influxdb:8086/k6 /scripts/test.js

# 다른 실행
```
docker run --rm --network=host \
    -v $(pwd)/scripts:/scripts:ro \
    grafana/k6:latest run --out influxdb=http://localhost:8086/k6 /scripts/testscript.js
```
