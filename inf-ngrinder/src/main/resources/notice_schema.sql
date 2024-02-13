CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(30) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `who` varchar(30) DEFAULT NULL,
  `createDate` timestamp NOT NULL,
  `updateDate` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notice_createDate` (`createDate`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
