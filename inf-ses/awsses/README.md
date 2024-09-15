JSON 변환

`https://appdevtools.com/json-escape-unescape`

---

SES 명령어 리스트

```
1. aws ses create-template --cli-input-json file://template.json

2. aws ses delete-template --template-name <이름>

3. aws ses verify-email-address --email-address <이메일 주소>
```

---

IAM 권한

```
 "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ses:*"
            ],
            "Resource": "*"
        }
    ]
```
