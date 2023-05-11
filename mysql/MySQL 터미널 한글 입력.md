# Docker MySQL 컨테이너 콘솔(터미널) 한글 입력 문제 해결

Docker MySQL Container 콘솔(터미널) 에서 한글 입력이 되지 않는 경우에 해결한 방법에 대해 기록합니다.

결론부터 말씀드리면, 컨테이너의 로케일 설정 때문입니다. 

해당 컨테이너의 리눅스 배포판에 맞춰 로케일 설정을 바꿔주면 됩니다.

## 상황 - 한글 입력 무시

한글 입력이 되지 않는 상황은 다음과 같습니다

```sql
select * from '사원'; 
```

위 명령어 입력시

```mysql
select * from ''; 
```

식으로 한글 입력이 무시되어 입력 되지 않습니다.



버전마다 상이할 수 있지만, 이 글에서는 MySQL 공식 이미지 + 오라클 리눅스 기준으로 해결법을 정리하겠습니다.

## MySQL 컨테이너 정보 확인

### MySQL Charset 확인

MySQL charset 설정은 uf8로 다음과 같이 문제없이 되어있었습니다.

```sh
mysql> show variables like 'c%';
+----------------------------------------------+--------------------------------+
| Variable_name                                | Value                          |
+----------------------------------------------+--------------------------------+
| caching_sha2_password_auto_generate_rsa_keys | ON                             |
| caching_sha2_password_digest_rounds          | 5000                           |
| caching_sha2_password_private_key_path       | private_key.pem                |
| caching_sha2_password_public_key_path        | public_key.pem                 |
| character_set_client                         | utf8mb3                        |
| character_set_connection                     | utf8mb3                        |
| character_set_database                       | utf8mb3                        |
| character_set_filesystem                     | binary                         |
| character_set_results                        | utf8mb3                        |
| character_set_server                         | utf8mb3                        |
| character_set_system                         | utf8mb3                        |
| character_sets_dir                           | /usr/share/mysql-8.0/charsets/ |
| check_proxy_users                            | OFF                            |
| collation_connection                         | utf8mb3_general_ci             |
| collation_database                           | utf8mb3_unicode_ci             |
| collation_server                             | utf8mb3_unicode_ci             |
| completion_type                              | NO_CHAIN                       |
| concurrent_insert                            | AUTO                           |
| connect_timeout                              | 10                             |
| connection_memory_chunk_size                 | 8912                           |
| connection_memory_limit                      | 18446744073709551615           |
| core_file                                    | OFF                            |
| create_admin_listener_thread                 | OFF                            |
| cte_max_recursion_depth                      | 1000                           |
+----------------------------------------------+--------------------------------+
```

### 컨테이너 이미지 정보 확인

해당 컨테이너 이미지 정보는 다음과 같습니다.

```sh
$ docker images
REPOSITORY                       TAG               IMAGE ID       CREATED         SIZE
mysql                            latest            be430b9d80e5   3 months ago    527MB
```

* https://hub.docker.com/_/mysql

해당 MySQL 컨테이너 리눅스 정보는 다음과 같습니다.

* 리눅스 배포판 버전 확인 명령어

```sh
cat /etc/*-release | uniq
```

컨테이너 내부에 들어가서 명령어를 치면 됩니다

```sh
docker exec -it 컨테이너이름 bash (또는 /bin/bash)
```





명령어 및 결과 확인

```sh
$ cat /etc/*-release | uniq
Oracle Linux Server release 8.7
NAME="Oracle Linux Server"
VERSION="8.7"
ID="ol"
ID_LIKE="fedora"
VARIANT="Server"
VARIANT_ID="server"
VERSION_ID="8.7"
PLATFORM_ID="platform:el8"
PRETTY_NAME="Oracle Linux Server 8.7"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:oracle:linux:8:7:server"
HOME_URL="https://linux.oracle.com/"
BUG_REPORT_URL="https://bugzilla.oracle.com/"

ORACLE_BUGZILLA_PRODUCT="Oracle Linux 8"
ORACLE_BUGZILLA_PRODUCT_VERSION=8.7
ORACLE_SUPPORT_PRODUCT="Oracle Linux"
ORACLE_SUPPORT_PRODUCT_VERSION=8.7
Red Hat Enterprise Linux release 8.7 (Ootpa)
Oracle Linux Server release 8.7
```

* 오라클 리눅스 인것을 확인했습니다.
* 혹시라도 패키지 관리자가 궁금하시다면 microdnf  또는 dnf 입니다.

```sh
$ microdnf install 
$ dnf install
```

# 해결 방법

도커 컨테이너의 리눅스 로케일 설정을 바꿔해결할 수 있습니다.

**로케일(Locale)**: 언어, 지역, 코드셋

로케일은 좁은 의미에서 이해하자면 사용자가 사용하는 언어로 정의할 수 있지만, 실제로는 조금 차이가 있습니다. 일반적으로 `language_territory.codeset` 형식으로 표현 됩니다. 언어(`language`)는 ISO 639-1, 지역(`territory`)은 ISO 3166-1을 따르며, 코드셋(`codeset`)에는 UTF-8나 EUC-KR과 같은 값이 올 수 있습니다. 이 값들의 조합으로 하나의 로케일이 정의됩니다.

![image-20230512020016187](./images//image-20230512020016187.png)

`locale` 명령어를 사용해 현재 MySQL 컨테이너의 로케일 설정을 확인할 수 있습니다.

```sh
$ locale
```

로케일 정보 확인

```
$ locale
LANG=
LC_CTYPE="POSIX"
LC_NUMERIC="POSIX"
LC_TIME="POSIX"
LC_COLLATE="POSIX"
LC_MONETARY="POSIX"
LC_MESSAGES="POSIX"
LC_PAPER="POSIX"
LC_NAME="POSIX"
LC_ADDRESS="POSIX"
LC_TELEPHONE="POSIX"
LC_MEASUREMENT="POSIX"
LC_IDENTIFICATION="POSIX"
LC_ALL=
```

로케일이 POSIX로 되어있었습니다. 

> POSIX?
>
> POSIX 로케일은 "Portable Operating System Interface"의 약자로, UNIX 및 UNIX-like 운영 체제에서 텍스트 및 문자열 처리를 위한 표준 인터페이스를 정의하는 표준입니다. POSIX 로케일은 지역화와 관련하여 텍스트의 형식화, 숫자 및 날짜 형식, 통화 표시 등과 같은 다양한 언어 및 문화 관련 설정을 처리하는 방법을 규정합니다.

* 한글은 C.UTF-8 이나 ko_KR.UTF-8 을 이용해야 합니다.

`해당 Locale을 POSIX에서 C.UTF-8로 바꿔주면 됩니다.`



현재 사용가능한 로케일 확인 명령어

```sh
$ locale -a
C
C.utf8
POSIX
```

### ko_KR.UTF-8 Locale을 사용하고 싶다면? -  다운로드

```sh
#ko_KR 로케일 설치
$ dnf install -y langpacks-ko
~~~ 설치

# 로케일 설치 확인 
$ locale -a
C
C.utf8
POSIX
ko_KR.euckr
ko_KR.utf8
```

## 1. Docker Locale 설정

현재 사용 가능한 로케일은 C.UTF-8 이니 이 값을 컨테이너 실행 시 옵션으로 설정해주면 되는데,

다음과 같이 컨테이너 시작시 환경변수 값에 넘겨주면 됩니다.

```sh
$ docker run -it -e LC_ALL=C.UTF-8 mysql
```

**저는 다음과 같은 방법으로 실행하였습니다.**

```sh
$ docker run --name mysql -e MYSQL_ROOT_PASSWORD=???? 
-e LC_ALL=C.UTF-8 -p 3306:3306 -it -d -v /Users/ysk/dev/MySQL/data:/var/lib/mysql mysql
```

하고나면 한글입력이 문제없이 잘 됩니다.

* 해당 컨테이너를 도커이미지로 만들어서 사용하면 더 편리하게 사용할 수  있습니다.

## 2. 리눅스 명령어로 Locale 설정 변경

다음과 같은 명령어를 입력합니다.

```sh
$ export LC_ALL="C.UTF-8"
$ export LANG="C.UTF-8"
```



다음과 같이 확인할 수 있습니다.

```sh
$ export LC_ALL="C.UTF-8"
$ export LANG="C.UTF-8"

$ locale
LANG=C.UTF-8
LC_CTYPE="C.UTF-8"
LC_NUMERIC="C.UTF-8"
LC_TIME="C.UTF-8"
LC_COLLATE="C.UTF-8"
LC_MONETARY="C.UTF-8"
LC_MESSAGES="C.UTF-8"
LC_PAPER="C.UTF-8"
LC_NAME="C.UTF-8"
LC_ADDRESS="C.UTF-8"
LC_TELEPHONE="C.UTF-8"
LC_MEASUREMENT="C.UTF-8"
LC_IDENTIFICATION="C.UTF-8"
LC_ALL=C.UTF-8
```

 하지만 이 방법은 이미 실행된 컨테이너라면 가끔씩 안먹히거나, restart시 초기화 되는 일도 있었습니다.



### 참조

* https://www.44bits.io/ko/post/setup_linux_locale_on_ubuntu_and_debian_container

* SRE - youngc