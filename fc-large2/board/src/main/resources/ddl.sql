CREATE TABLE user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    userId     int                 NOT NULL,
    password   VARCHAR(45)                         NOT NULL,
    nickName   VARCHAR(45),
    isAdmin    BOOLEAN                              NOT NULL DEFAULT FALSE,
    createTime DATETIME                             NOT NULL,
    isWithDraw BOOLEAN                              NOT NULL DEFAULT FALSE,
    status     varchar(255),
    updateTime DATETIME,
    CONSTRAINT chk_user_status CHECK (status IN ('DEFAULT', 'ADMIN', 'DELETED'))
);

CREATE TABLE post
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    isAdmin    BOOLEAN      NOT NULL DEFAULT FALSE,
    contents   varchar(500),
    createTime DATETIME     NOT NULL,
    views      INT          NOT NULL DEFAULT 0,
    categoryId INT,
    userId     INT,
    updateTime DATETIME,
    FOREIGN KEY (categoryId) REFERENCES category (id),
    FOREIGN KEY (userId) REFERENCES user (id)
);

CREATE TABLE comment
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    postId       INT NOT NULL,
    contents     varchar(300),
    subCommentId INT,
    FOREIGN KEY (postId) REFERENCES post (id),
    FOREIGN KEY (subCommentId) REFERENCES comment (id)
);

CREATE TABLE category
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) UNIQUE NOT NULL
);

CREATE TABLE tag
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(45) UNIQUE NOT NULL,
    url    VARCHAR(45)
);

CREATE TABLE postTag
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    postId INT,
    tagId  INT,
    FOREIGN KEY (postId) REFERENCES post (id),
    FOREIGN KEY (tagId) REFERENCES tag (id)
);
