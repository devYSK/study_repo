-- Payment Event 테이블 생성
CREATE TABLE Payment_Event
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id        BIGINT,
    is_payment_done BOOLEAN,
    payment_key     VARCHAR(255) UNIQUE,
    order_id        VARCHAR(255) UNIQUE,
    type            ENUM ('일반 결제', '자동 결제'),
    order_name      VARCHAR(255),
    method          ENUM ('카드 결제', '간편 결제', '휴대폰'),
    psp_raw_data    JSON,
    created_at      DATETIME,
    updated_at      DATETIME,
    approved_at     DATETIME
);

-- Payment Order 테이블 생성
CREATE TABLE Payment_Order
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_event_id     BIGINT,
    seller_id            BIGINT,
    product_id           BIGINT,
    order_id             VARCHAR(255) UNIQUE,
    amount               DECIMAL(10, 2),
    payment_order_status ENUM ('NOT_STARTED', 'EXECUTING', 'SUCCESS'),
    ledger_updated       BOOLEAN,
    wallet_updated       BOOLEAN,
    failed_count         TINYINT,
    threshold            TINYINT,
    created_at           DATETIME,
    updated_at           DATETIME,
    FOREIGN KEY (payment_event_id) REFERENCES Payment_Event (id)
);

-- Payment Order History 테이블 생성
CREATE TABLE Payment_Order_History
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_order_id BIGINT,
    previous_status  ENUM ('NOT_STARTED', 'EXECUTING', 'SUCCESS'),
    new_status       ENUM ('NOT_STARTED', 'EXECUTING', 'SUCCESS'),
    created_at       DATETIME,
    changed_by       VARCHAR(255),
    reason           VARCHAR(255),
    FOREIGN KEY (payment_order_id) REFERENCES Payment_Order (id)
);
