# AWS ECS 멀티 타겟그룹

https://blog.kyobodts.co.kr/2023/08/18/hands-on-alb-amazon-ecs-on-fargate-%EB%A9%80%ED%8B%B0-%ED%83%80%EA%B2%9F%EA%B7%B8%EB%A3%B9-%EC%97%B0%EA%B2%B0%EA%B9%8C%EC%A7%80/

AWS ECS에서 멀티 타겟 그룹을 추가하려면 서비스 정의를 업데이트하여 여러 타겟 그룹을 지정해야 합니다. 이를 위해서는 다음 단계를 따릅니다:

1. **ECS 서비스 업데이트용 JSON 파일 준비**: 여러 타겟 그룹을 포함한 서비스 업데이트 JSON 파일을 준비합니다.
2. **AWS CLI 명령어 실행**: JSON 파일을 사용하여 ECS 서비스를 업데이트합니다.

아래 예제는 클러스터 `blog-ad`와 서비스 `blog-ad-grow-api`에 대해 두 개의 타겟 그룹을 추가하는 방법을 설명합니다.

### 1. JSON 파일 준비

`blog-grow-config.json` 파일에 여러 타겟 그룹을 지정합니다. 예를 들어:

```
json
코드 복사
{
  "desiredCount": 1,
  "taskDefinition": "blog-ad-grow-api:1",
  "loadBalancers": [
    {
      "containerName": "ecs-test",
      "containerPort": 80,
      "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:123456789012:targetgroup/ecs-test-tg/294d89782947b8a4"
    },
    {
      "containerName": "ecs-test",
      "containerPort": 80,
      "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:123456789012:targetgroup/ecs-multi-test-tg/026fb00974479ae"
    }
  ],
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["subnet-xxxxxxxx", "subnet-yyyyyyyy"],
      "securityGroups": ["sg-xxxxxxxx"],
      "assignPublicIp": "ENABLED"
    }
  },
  "forceNewDeployment": true
}
```

위의 JSON 파일에서는 두 개의 타겟 그룹을 `loadBalancers` 배열에 지정하고 있습니다.

### 2. AWS CLI 명령어 실행

준비한 JSON 파일을 사용하여 ECS 서비스를 업데이트합니다.

```
sh
코드 복사
aws ecs update-service --cluster blog-ad --service blog-ad-grow-api --cli-input-json file://blog-grow-config.json --region ap-northeast-2
```

위 명령어는 `--cluster`, `--service`, `--cli-input-json`, `--region` 옵션을 사용하여 ECS 서비스를 업데이트합니다.

### 주의사항

- **`targetGroupArn`**: 각 타겟 그룹의 ARN을 올바르게 지정해야 합니다.
- **`containerName`**: 컨테이너 이름은 태스크 정의에 지정된 이름과 일치해야 합니다.
- **`containerPort`**: 컨테이너가 사용하는 포트를 지정합니다.
- **`networkConfiguration`**: 서비스가 배포될 서브넷과 보안 그룹을 지정합니다.

이 절차를 따르면 ECS 서비스에 멀티 타겟 그룹을 추가하여 서비스 트래픽을 여러 타겟 그룹으로 분산시킬 수 있습니다.





### VPC, 서브넷, 보안 그룹 정보 확인 방법

#### 1. AWS Management Console을 통해 확인

1. **VPC 정보**:
   - AWS Management Console에 로그인합니다.
   - VPC 대시보드로 이동합니다.
   - 해당 VPC의 ID를 확인합니다.
2. **서브넷 정보**:
   - VPC 대시보드에서 서브넷 메뉴로 이동합니다.
   - 서비스가 배포되는 서브넷의 ID를 확인합니다.
3. **보안 그룹 정보**:
   - VPC 대시보드에서 보안 그룹 메뉴로 이동합니다.
   - ECS 서비스에 연결된 보안 그룹의 ID를 확인합니다.



#### 2. AWS CLI를 통해 확인

ECS 서비스에 대한 현재 네트워크 구성을 확인하려면 다음 명령어를 사용할 수 있습니다:

```
aws ecs describe-services --cluster blog-ad --services blog-ad-grow-api --region ap-northeast-2
```



```
{
    "services": [
        {
            "serviceArn": "arn:aws:ecs:ap-northeast-2:435033618084:service/blog-ad/blog-ad-grow-api",
            "serviceName": "blog-ad-grow-api",
            "clusterArn": "arn:aws:ecs:ap-northeast-2:435033618084:cluster/blog-ad",
            "loadBalancers": [
                {
                    "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:435033618084:targetgroup/blog-grow-api/a2227a7219b0fb87",
                    "containerName": "blog-ad-grow-api",
                    "containerPort": 8080
                }
            ],
            "serviceRegistries": [],
            "status": "ACTIVE",
            "desiredCount": 1,
            "runningCount": 1,
            "pendingCount": 0,
            "capacityProviderStrategy": [
                {
                    "capacityProvider": "FARGATE",
                    "weight": 1,
                    "base": 0
                }
            ],
            "platformVersion": "1.4.0",
            "platformFamily": "Linux",
            "taskDefinition": "arn:aws:ecs:ap-northeast-2:435033618084:task-definition/blog-ad-grow:13",
            "deploymentConfiguration": {
                "deploymentCircuitBreaker": {
                    "enable": false,
                    "rollback": false
                },
                "maximumPercent": 200,
                "minimumHealthyPercent": 100
            },
            "deployments": [
                {
                    "id": "ecs-svc/9190381245649392497",
                    "status": "PRIMARY",
                    "taskDefinition": "arn:aws:ecs:ap-northeast-2:435033618084:task-definition/blog-ad-grow:13",
                    "desiredCount": 1,
                    "pendingCount": 0,
                    "runningCount": 1,
                    "failedTasks": 0,
                    "createdAt": "2024-06-07T00:30:33.468000+09:00",
                    "updatedAt": "2024-06-07T00:34:23.645000+09:00",
                    "capacityProviderStrategy": [
                        {
                            "capacityProvider": "FARGATE",
                            "weight": 1,
                            "base": 0
                        }
                    ],
                    "platformVersion": "1.4.0",
                    "platformFamily": "Linux",
                    "networkConfiguration": {
                        "awsvpcConfiguration": {
                            "subnets": [
                                "subnet-dd4bf4a6",
                                "subnet-4b2fed20",
                                "subnet-acd0e6e0"
                            ],
                            "securityGroups": [
                                "sg-3d739c5e"
                            ],
                            "assignPublicIp": "ENABLED"
                        }
                    },
                    "rolloutState": "COMPLETED",
                    "rolloutStateReason": "ECS deployment ecs-svc/9190381245649392497 completed."
                }

자 현재정보야
```





현재 ECS 서비스의 정보를 기반으로, 멀티 타겟 그룹을 추가하는 JSON 파일을 작성하고 서비스를 업데이트하는 절차는 다음과 같습니다.

### blog-grow-config.json

기존 정보를 활용하여 두 개의 타겟 그룹을 추가한 JSON 파일을 작성합니다:

```
json
코드 복사
{
  "desiredCount": 1,
  "taskDefinition": "arn:aws:ecs:ap-northeast-2:435033618084:task-definition/blog-ad-grow:13",
  "loadBalancers": [
    {
      "containerName": "blog-ad-grow-api",
      "containerPort": 8080,
      "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:435033618084:targetgroup/blog-grow-api/a2227a7219b0fb87"
    },
    {
      "containerName": "blog-ad-grow-api",
      "containerPort": 8090,
      "targetGroupArn": "arn:aws:elasticloadbalancing:ap-northeast-2:435033618084:targetgroup/blog-grow-api-8090/2fa4e35b924cb0de"
    }
  ],
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": [
        "subnet-dd4bf4a6",
        "subnet-4b2fed20",
        "subnet-acd0e6e0"
      ],
      "securityGroups": [
        "sg-3d739c5e"
      ],
      "assignPublicIp": "ENABLED"
    }
  },
  "forceNewDeployment": true
}
```

이 JSON 파일은 다음을 포함합니다:

- 두 개의 타겟 그룹 (`targetGroupArn`)이 포함된 `loadBalancers` 설정.
- 네트워크 구성 (`networkConfiguration`)에서 서브넷과 보안 그룹.

### AWS CLI 명령어 실행

위 JSON 파일을 사용하여 ECS 서비스를 업데이트합니다:

```
aws ecs update-service --cluster blog-ad --service blog-ad-grow-api --cli-input-json file://blog-grow-config.json --region ap-northeast-2
```

이 명령어는 ECS 서비스에 두 개의 타겟 그룹을 추가하고, 네트워크 설정을 적용합니다. `--force-new-deployment` 옵션을 사용하여 새로운 배포를 강제합니다.