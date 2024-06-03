CREATE TABLE `admin`
(
    id           BIGINT AUTO_INCREMENT  NOT NULL,
    email        VARCHAR(255)           NOT NULL,
    nickname     VARCHAR(255)           NOT NULL,
    password     VARCHAR(255)           NOT NULL,
    phone_number VARCHAR(255)           NOT NULL,
    memo         VARCHAR(255)           NULL,
    created_at   datetime DEFAULT NOW() NOT NULL,
    modified_at  datetime DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
);

CREATE TABLE event
(
    id                       BIGINT AUTO_INCREMENT        NOT NULL,
    place_id                 BIGINT                       NOT NULL,
    event_name               VARCHAR(255)                 NOT NULL,
    event_status             VARCHAR(20) DEFAULT 'OPENED' NOT NULL,
    event_start_datetime     datetime                     NOT NULL,
    event_end_datetime       datetime                     NOT NULL,
    current_number_of_people INT         DEFAULT 0        NOT NULL,
    capacity                 INT                          NOT NULL,
    memo                     VARCHAR(255)                 NULL,
    created_at               datetime    DEFAULT NOW()    NOT NULL,
    modified_at              datetime    DEFAULT NOW()    NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE place
(
    id           BIGINT AUTO_INCREMENT        NOT NULL,
    place_type   VARCHAR(20) DEFAULT 'COMMON' NOT NULL,
    place_name   VARCHAR(255)                 NOT NULL,
    address      VARCHAR(255)                 NOT NULL,
    phone_number VARCHAR(255)                 NOT NULL,
    capacity     INT         DEFAULT 0        NOT NULL,
    memo         VARCHAR(255)                 NULL,
    created_at   datetime    DEFAULT NOW()    NOT NULL,
    modified_at  datetime    DEFAULT NOW()    NOT NULL,
    CONSTRAINT pk_place PRIMARY KEY (id)
);

CREATE TABLE admin_place_map
(
    id          BIGINT AUTO_INCREMENT  NOT NULL,
    admin_id    BIGINT                 NOT NULL,
    place_id    BIGINT                 NOT NULL,
    created_at  datetime DEFAULT NOW() NOT NULL,
    modified_at datetime DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_adminplacemap PRIMARY KEY (id)
);


CREATE INDEX idx_3eb99be2c6df3dcd9fb790099 ON place (phone_number);

CREATE INDEX idx_4d2dad1171ab1101220c1bde4 ON place (created_at);

CREATE INDEX idx_51eceaa568455e24788d07974 ON place (modified_at);

CREATE INDEX idx_8937d5dfb2707f816927aa988 ON place (place_name);

CREATE INDEX idx_d5922c3bf14eb0fcb470aade5 ON place (address);

CREATE INDEX idx_1bc18d95691e942c6be0582fc ON event (modified_at);

CREATE INDEX idx_70bb5090e97a9c774fc8145f1 ON event (created_at);

CREATE INDEX idx_92d25cef60c9a3ed64bc291c7 ON event (event_start_datetime);

CREATE INDEX idx_9f2fe388036352060533e460a ON event (event_end_datetime);

CREATE INDEX idx_c94b41f3f084c10b88faa3f52 ON event (event_name);

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_PLACE FOREIGN KEY (place_id) REFERENCES place (id);

ALTER TABLE `admin`
    ADD CONSTRAINT uc_admin_email UNIQUE (email);

ALTER TABLE `admin`
    ADD CONSTRAINT uc_admin_nickname UNIQUE (nickname);

CREATE INDEX idx_356c59a80fbd29aaaa24d7882 ON `admin` (modified_at);

CREATE INDEX idx_927c399ba7ee32f041a096fe5 ON `admin` (created_at);

CREATE INDEX idx_fdf4b3f793ff020a028a46cb1 ON `admin` (phone_number);

CREATE INDEX idx_b7ac127882c206dc0d1c3bc21 ON admin_place_map (modified_at);

CREATE INDEX idx_e095edd8ec09ec05343f1bd06 ON admin_place_map (created_at);

ALTER TABLE admin_place_map
    ADD CONSTRAINT FK_ADMINPLACEMAP_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES `admin` (id);

ALTER TABLE admin_place_map
    ADD CONSTRAINT FK_ADMINPLACEMAP_ON_PLACE FOREIGN KEY (place_id) REFERENCES place (id);

