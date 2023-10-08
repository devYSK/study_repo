CREATE TABLE IF NOT EXISTS student_score
(
    `student_score_id` BIGINT       NOT NULL COMMENT '인조키' AUTO_INCREMENT,
    `exam`             VARCHAR(255) NOT NULL COMMENT '시험',
    `student_name`     VARCHAR(255) NOT NULL COMMENT '학생 이름',
    `kor_score`        INT          NOT NULL COMMENT '국어 시험 점수',
    `english_score`    INT          NOT NULL COMMENT '영어 시험 점수',
    `math_score`       INT          NOT NULL COMMENT '수학 시험 점수',
    PRIMARY KEY (`student_score_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS student_pass
(
    `student_pass_id` BIGINT       NOT NULL COMMENT '인조키' AUTO_INCREMENT,
    `exam`            VARCHAR(255) NOT NULL COMMENT '시험',
    `student_name`    VARCHAR(255) NOT NULL COMMENT '학생 이름',
    `avg_score`       DOUBLE       NOT NULL COMMENT '평균 시험 점수',
    PRIMARY KEY (`student_pass_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS student_fail
(
    `student_fail_id` BIGINT       NOT NULL COMMENT '인조키' AUTO_INCREMENT,
    `exam`            VARCHAR(255) NOT NULL COMMENT '시험',
    `student_name`    VARCHAR(255) NOT NULL COMMENT '학생 이름',
    `avg_score`       DOUBLE       NOT NULL COMMENT '평균 시험 점수',
    PRIMARY KEY (`student_fail_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;