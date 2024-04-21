-- Payment Event 테이블 생성
CREATE TABLE payment_events
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id        BIGINT,
    is_payment_done BOOLEAN,
    payment_key     VARCHAR(255) UNIQUE,
    order_id        VARCHAR(255) UNIQUE,
    type            VARCHAR(50),
    order_name      VARCHAR(255),
    method          VARCHAR(50),
    psp_raw_data    text,
    created_at      DATETIME,
    updated_at      DATETIME,
    approved_at     DATETIME
);

-- Payment Order 테이블 생성
CREATE TABLE payment_orders
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_event_id     BIGINT,
    seller_id            BIGINT,
    product_id           BIGINT,
    order_id             VARCHAR(255),
    amount               DECIMAL(10, 2),
    payment_order_status VARCHAR(50),
    ledger_updated       BOOLEAN,
    wallet_updated       BOOLEAN,
    failed_count         TINYINT,
    threshold            TINYINT,
    created_at           DATETIME,
    updated_at           DATETIME,
    FOREIGN KEY (payment_event_id) REFERENCES payment_events (id)
);

-- Payment Order History 테이블 생성
CREATE TABLE payment_order_histories
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_order_id BIGINT,
    previous_status  VARCHAR(50),
    new_status       VARCHAR(50),
    created_at       DATETIME,
    changed_by       VARCHAR(255),
    reason           VARCHAR(255),
    FOREIGN KEY (payment_order_id) REFERENCES payment_orders (id)
);

-- Outboxes 테이블 생성
CREATE TABLE outboxes
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    idempotency_key VARCHAR(255) UNIQUE,
    status          varchar(50),
    type            VARCHAR(40),
    partition_key   INT,
    payload         JSON,
    metadata        JSON,
    created_at           DATETIME,
    updated_at           DATETIME,
);
