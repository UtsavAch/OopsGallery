CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    phone_no    VARCHAR(30),
    password    TEXT         NOT NULL,
    address     TEXT,
    role        VARCHAR(50)  NOT NULL,     -- ROLE_OWNER, ROLE_USER, ROLE_GUEST
    enabled     BOOLEAN      NOT NULL DEFAULT false,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE verification_codes
(
    id          SERIAL PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL,
    user_id     INT          NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,

    CONSTRAINT fk_verification_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE artworks
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    category    VARCHAR(100) NOT NULL,  -- PAINTING, DIGITAL_ART, etc.
    label       VARCHAR(100),
    price       NUMERIC(10, 2),
    img_url     TEXT
);

CREATE TABLE cart
(
    id          SERIAL PRIMARY KEY,
    user_id     INT UNIQUE,
    total_items INT            NOT NULL DEFAULT 0,
    total_price NUMERIC(10, 2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE cart_items
(
    id         SERIAL PRIMARY KEY,
    cart_id    INT NOT NULL,
    artwork_id INT NOT NULL,
    quantity   INT NOT NULL CHECK (quantity >= 1),
    CONSTRAINT fk_cartitem_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_cartitem_artwork
        FOREIGN KEY (artwork_id)
            REFERENCES artworks (id),
    CONSTRAINT uq_cart_artwork UNIQUE (cart_id, artwork_id)
);


CREATE TABLE orders
(
    id          SERIAL PRIMARY KEY,
    user_id     INT               NOT NULL,
    total_price NUMERIC(10, 2),
    address     TEXT,
    status      VARCHAR(50)       NOT NULL,  -- PENDING, CONFIRMED, etc.
    ordered_at  TIMESTAMP,
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);


CREATE TABLE order_items
(
    id                SERIAL PRIMARY KEY,
    order_id          INT            NOT NULL,
    artwork_id        INT            NOT NULL,
    quantity          INT            NOT NULL CHECK (quantity >= 1),
    price_at_purchase NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_orderitem_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_orderitem_artwork
        FOREIGN KEY (artwork_id)
            REFERENCES artworks (id)
);

CREATE TABLE payments
(
    id             SERIAL PRIMARY KEY,
    order_id       INT            NOT NULL,
    user_id        INT            NOT NULL,
    amount         NUMERIC(10, 2) NOT NULL,
    currency       VARCHAR(10),
    method         VARCHAR(50),
    status         VARCHAR(50),  -- PENDING, SUCCESS, FAILED, etc.
    transaction_id VARCHAR(255),
    created_at     TIMESTAMP,
    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id),
    CONSTRAINT fk_payment_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE INDEX idx_artworks_category ON artworks(category);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
