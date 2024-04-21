CREATE TABLE wallets
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT UNIQUE,
    balance    DECIMAL(10, 2),
    version    INT,
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE wallet_transactions
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id       BIGINT,
    amount          DECIMAL(10, 2),
    type            VARCHAR(50),
    reference_type  VARCHAR(50),
    reference_id    BIGINT,
    order_id        VARCHAR(255),
    idempotency_key VARCHAR(255),
    created_at      DATETIME,
    updated_at      DATETIME,
    FOREIGN KEY (wallet_id) REFERENCES wallets (id)
);
