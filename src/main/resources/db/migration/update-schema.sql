CREATE TABLE account
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE address
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    street      VARCHAR(255)                             NOT NULL,
    city        VARCHAR(255)                             NOT NULL,
    province    VARCHAR(255)                             NOT NULL,
    country     VARCHAR(255)                             NOT NULL,
    postal_code VARCHAR(255)                             NOT NULL,
    customer_id INTEGER                                  NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE cart_map
(
    customer_id INTEGER NOT NULL,
    game_id     INTEGER NOT NULL,
    CONSTRAINT pk_cart_map PRIMARY KEY (customer_id, game_id)
);

CREATE TABLE category
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE category_map
(
    category_id INTEGER NOT NULL,
    game_id     INTEGER NOT NULL,
    CONSTRAINT pk_category_map PRIMARY KEY (category_id, game_id)
);

CREATE TABLE credit_card
(
    id                 INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    card_number        INTEGER                                  NOT NULL,
    cvv                INTEGER                                  NOT NULL,
    expiry_date        date                                     NOT NULL,
    customer_id        INTEGER                                  NOT NULL,
    billing_address_id INTEGER                                  NOT NULL,
    CONSTRAINT pk_creditcard PRIMARY KEY (id)
);

CREATE TABLE customer
(
    id           INTEGER NOT NULL,
    email        VARCHAR(255),
    phone_number VARCHAR(255),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE employee
(
    id        INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE game
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255),
    description   VARCHAR(255),
    cover_picture VARCHAR(255),
    price         FLOAT                                    NOT NULL,
    is_active     BOOLEAN                                  NOT NULL,
    stock         INTEGER                                  NOT NULL,
    CONSTRAINT pk_game PRIMARY KEY (id)
);

CREATE TABLE game_request
(
    id              INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    external_review VARCHAR(255),
    status          VARCHAR(255),
    requestor_id    INTEGER                                  NOT NULL,
    game_id         INTEGER                                  NOT NULL,
    CONSTRAINT pk_gamerequest PRIMARY KEY (id)
);

CREATE TABLE like_map
(
    customer_id INTEGER NOT NULL,
    review_id   INTEGER NOT NULL,
    CONSTRAINT pk_like_map PRIMARY KEY (customer_id, review_id)
);

CREATE TABLE manager
(
    id INTEGER NOT NULL,
    CONSTRAINT pk_manager PRIMARY KEY (id)
);

CREATE TABLE policy
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255)                             NOT NULL,
    CONSTRAINT pk_policy PRIMARY KEY (id)
);

CREATE TABLE promotion
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    discount VARCHAR(255)                             NOT NULL,
    start_date date,
    end_date date
    CONSTRAINT pk_promotion PRIMARY KEY (id)
);

CREATE TABLE promotion_map
(
    game_id      INTEGER NOT NULL,
    promotion_id INTEGER NOT NULL,
    CONSTRAINT pk_promotion_map PRIMARY KEY (game_id, promotion_id)
);

CREATE TABLE purchase
(
    id                  INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    purchase_date       date,
    purchase_price      FLOAT                                    NOT NULL,
    game_purchased_id   INTEGER                                  NOT NULL,
    customer_id         INTEGER                                  NOT NULL,
    delivery_address_id INTEGER                                  NOT NULL,
    payment_method_id   INTEGER                                  NOT NULL,
    CONSTRAINT pk_purchase PRIMARY KEY (id)
);

CREATE TABLE refund_request
(
    purchase_id INTEGER      NOT NULL,
    status      VARCHAR(255),
    reason      VARCHAR(255) NOT NULL,
    reviewer_id INTEGER,
    CONSTRAINT pk_refundrequest PRIMARY KEY (purchase_id)
);

CREATE TABLE reply
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(255)                             NOT NULL,
    review_id INTEGER                                  NOT NULL,
    CONSTRAINT pk_reply PRIMARY KEY (id)
);

CREATE TABLE review
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    rating      INTEGER                                  NOT NULL,
    text        VARCHAR(255)                             NOT NULL,
    purchase_id INTEGER                                  NOT NULL,
    CONSTRAINT pk_review PRIMARY KEY (id)
);

CREATE TABLE wishlist_map
(
    customer_id INTEGER NOT NULL,
    game_id     INTEGER NOT NULL,
    CONSTRAINT pk_wishlist_map PRIMARY KEY (customer_id, game_id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE credit_card
    ADD CONSTRAINT FK_CREDITCARD_ON_BILLING_ADDRESS FOREIGN KEY (billing_address_id) REFERENCES address (id);

ALTER TABLE credit_card
    ADD CONSTRAINT FK_CREDITCARD_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_ID FOREIGN KEY (id) REFERENCES account (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ID FOREIGN KEY (id) REFERENCES account (id);

ALTER TABLE game_request
    ADD CONSTRAINT FK_GAMEREQUEST_ON_GAME FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE game_request
    ADD CONSTRAINT FK_GAMEREQUEST_ON_REQUESTOR FOREIGN KEY (requestor_id) REFERENCES employee (id);

ALTER TABLE manager
    ADD CONSTRAINT FK_MANAGER_ON_ID FOREIGN KEY (id) REFERENCES account (id);

ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_ON_DELIVERY_ADDRESS FOREIGN KEY (delivery_address_id) REFERENCES address (id);

ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_ON_GAME_PURCHASED FOREIGN KEY (game_purchased_id) REFERENCES game (id);

ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_ON_PAYMENT_METHOD FOREIGN KEY (payment_method_id) REFERENCES credit_card (id);

ALTER TABLE refund_request
    ADD CONSTRAINT FK_REFUNDREQUEST_ON_PURCHASE FOREIGN KEY (purchase_id) REFERENCES purchase (id);

ALTER TABLE refund_request
    ADD CONSTRAINT FK_REFUNDREQUEST_ON_REVIEWER FOREIGN KEY (reviewer_id) REFERENCES employee (id);

ALTER TABLE reply
    ADD CONSTRAINT FK_REPLY_ON_REVIEW FOREIGN KEY (review_id) REFERENCES review (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_PURCHASE FOREIGN KEY (purchase_id) REFERENCES purchase (id);

ALTER TABLE cart_map
    ADD CONSTRAINT fk_cart_map_on_customer FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE cart_map
    ADD CONSTRAINT fk_cart_map_on_game FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE category_map
    ADD CONSTRAINT fk_catmap_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE category_map
    ADD CONSTRAINT fk_catmap_on_game FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE like_map
    ADD CONSTRAINT fk_like_map_on_customer FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE like_map
    ADD CONSTRAINT fk_like_map_on_review FOREIGN KEY (review_id) REFERENCES review (id);

ALTER TABLE promotion_map
    ADD CONSTRAINT fk_promap_on_game FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE promotion_map
    ADD CONSTRAINT fk_promap_on_promotion FOREIGN KEY (promotion_id) REFERENCES promotion (id);

ALTER TABLE wishlist_map
    ADD CONSTRAINT fk_wismap_on_customer FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE wishlist_map
    ADD CONSTRAINT fk_wismap_on_game FOREIGN KEY (game_id) REFERENCES game (id);