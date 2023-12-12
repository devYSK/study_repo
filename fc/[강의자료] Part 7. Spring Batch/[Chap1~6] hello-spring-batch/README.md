## PlainText, ResultText 테이블 생성 및 데이터
```sql
CREATE TABLE `house`.`plain_text` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO plain_text values (1, "apple");
INSERT INTO plain_text values (2, "banana");
INSERT INTO plain_text values (3, "carrot");
INSERT INTO plain_text values (4, "dessert");
INSERT INTO plain_text values (5, "egg");
INSERT INTO plain_text values (6, "fish");
INSERT INTO plain_text values (7, "goose");

CREATE TABLE `result_text` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `text` varchar(100) NOT NULL,
    PRIMARY KEY (`id`));
```