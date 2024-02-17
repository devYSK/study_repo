# Hello Search API Sample Project

# 사전 준비
- [Local] Elasticsearch 8.6.2
- [Local] Kibana 8.6.0 
- Elasticseearch 연결 정보
  - Finger Print
  - User Account / Password

# 참고사항
Search API 만들 때 필요한 추가 정보

## Finger Print 생성 방법
```
$ openssl x509 -fingerprint -sha256 -noout -in /path/to/http_ca.crt
```
