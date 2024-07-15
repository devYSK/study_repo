# AWS ECS 대상그룹 추가방법





AWS Management Console을 사용하여 새로운 대상 그룹을 생성합니다.

1. 로드밸런서 생성 
2. **AWS Management Console**에 로그인합니다.
3. **EC2** 서비스로 이동합니다.
4. 왼쪽 메뉴에서 **Target Groups**를 선택합니다.
5. **Create target group** 버튼을 클릭합니다.
6. **Target type**을 **IP**로 설정합니다.
7. 대상 그룹의 이름을 `xxxx로`으로 지정합니다.
8. VPC, 포트(8090) 및 기타 설정을 완료합니다.
9. **Create** 버튼을 클릭하여 대상 그룹을 생성합니다.

10. 해당 대상그룹을 로드밸런서와 연결한다.
11. 대상 로드 밸런서를 선택합니다.
12. **Listeners** 탭으로 이동합니다.
13. 지정할 포트 (예시8090)에 대한 리스너 규칙을 추가하거나 기존 리스너에 대상 그룹을 추가합니다.
14. 기존 대상 그룹과 새로운 대상 그룹을 포함하여 ECS 서비스 업데이트



예시 1. 기존 대상그룹과 새로운 대상그룹을 포함하는 json 을 생성



vi load-balancers.json

```json
[
    { // 기존 대상그룹 
        "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:123456:targetgroup/기존타겟그룹명/adkfsfdksdkf23",
        "containerName": "컨테이너명",
        "containerPort": 8080
    },
    { // 새로 추가할 대상그룹 
        "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:123456:targetgroup/새로추가할 대상그룹명8090/asdkfsdkf",
        "containerName": "컨테이너명 ",
        "containerPort": 8090
    }
]

```



다음 aws cli 명령어 실행

```sh
LOAD_BALANCERS=$(cat load-balancers.json)

aws ecs update-service \
    --cluster 클러스터명 \
    --service 서비스명 \
    --load-balancers "$LOAD_BALANCERS"
```



성공하면 ecs 서비스 -> 상태 및 지표에 가면 대상 그룹이 늘어나있다. 

* 추가 참고용 : https://blog.kyobodts.co.kr/2023/08/18/hands-on-alb-amazon-ecs-on-fargate-%EB%A9%80%ED%8B%B0-%ED%83%80%EA%B2%9F%EA%B7%B8%EB%A3%B9-%EC%97%B0%EA%B2%B0%EA%B9%8C%EC%A7%80/

## 번외 - 리스너 생성 명령어 

```sh
aws elbv2 create-listener \
    --load-balancer-arn <your-load-balancer-arn> \
    --protocol HTTP \
    --port 8090 \
    --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:ap-northeast-2:123456:targetgroup/대상그룹명/124124 // 대상그룹 arn

```



## 2. 기존 리스너 업데이트

```sh
aws elbv2 modify-listener \
    --listener-arn <your-listener-arn> \
    --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:ap-northeast-2:123456:targetgroup/타겟그룹명/124124 // 대상그룹 arn 

```

