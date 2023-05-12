

# MySQL 도커 터미널 한글 안되는 에러







docker run —name mysql -e MYSQL_ROOT_PASSWORD=root -e LC_ALL=C.UTF-8 -p 3306:3306 -it -d -v /Users/ysk/dev/MySQL/data:/var/lib/mysql mysql_img