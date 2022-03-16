DROP SCHEMA IF EXISTS project_pae CASCADE;
CREATE SCHEMA project_pae;

-------------------CREATE TABLES--------------------------
CREATE TABLE project_pae.members
(
    id_member  SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    password   CHAR(60)     NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    is_admin   BOOLEAN      NOT NULL,
    state      VARCHAR(10)  NOT NULL,
    phone      VARCHAR(15)
);

CREATE TABLE project_pae.items_types
(
    id_type   SERIAL PRIMARY KEY,
    item_type VARCHAR(50) NOT NULL
);

CREATE TABLE project_pae.addresses
(
    id_address      SERIAL PRIMARY KEY,
    street          VARCHAR(150) NOT NULL,
    building_number VARCHAR(20)  NOT NULL,
    unit_number     VARCHAR(10)  NULL,
    postcode        VARCHAR(10)  NOT NULL,
    commune         VARCHAR(100) NOT NULL,
    id_member       INTEGER      NOT NULL,
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.items
(
    id_item          SERIAL PRIMARY KEY,
    item_description VARCHAR(500) NOT NULL,
    id_item_type     INTEGER      NOT NULL,
    id_member        INTEGER      NOT NULL,
    photo            VARCHAR(500) NULL,
    title            VARCHAR(50)  NOT NULL,
    offer_status     VARCHAR(10)  NOT NULL,
    FOREIGN KEY (id_item_type) REFERENCES project_pae.items_types (id_type),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.ratings
(
    id_item   SERIAL PRIMARY KEY,
    rating    INTEGER      NOT NULL,
    text      VARCHAR(500) NOT NULL,
    id_member INTEGER      NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.offers
(
    id_offer  SERIAL PRIMARY KEY,
    date      DATE        NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    id_item   INTEGER     NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item)
);

CREATE TABLE project_pae.interests
(
    id_interest SERIAL PRIMARY KEY,
    call_wanted BOOLEAN NOT NULL,
    id_offer    INTEGER NOT NULL,
    id_member   INTEGER NOT NULL,
    date        DATE    NOT NULL,
    FOREIGN KEY (id_offer) REFERENCES project_pae.offers (id_offer),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.refusals
(
    id_refusal SERIAL PRIMARY KEY,
    text       VARCHAR(500) NOT NULL,
    id_member  INTEGER      NOT NULL,
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);


CREATE TABLE project_pae.recipients
(
    id_recipient SERIAL PRIMARY KEY,
    id_item      INTEGER     NOT NULL,
    id_member    INTEGER     NOT NULL,
    received     VARCHAR(15) NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)

);
-------------------INSERT INTO--------------------------
--Mot de passe : password
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('nikesakou', '$2a$10$vD5FXSmaNv4DkfpFfKfDsOjaJ192x2RdWyjIWr28lj5r1X9uvB9yC', 'Dimitriadis',
        'Nicolas', true, 'confirmed', NULL);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('lepirelot', '$2a$10$S6YG0YYAwZ.MLLsCOlhUiuy6HYl.U.r3dG60HnopHfY4RIpPK8jrS', 'Pirlot',
        'Antoine', true, 'registered', NULL);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('Navial', '$2a$10$Psvm5269Z5F.eR1LRz92feZ/s1Euxsi15yBkLRFWJkX0PGhidAFP6', 'Denis', 'Victor',
        false, 'confirmed', '0479489137');
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('Swapiz', '$2a$10$YUc0XQ56mZr9p2RQOPko/OuszOaUJaypZ.viL4FSFwG4oeKkoQNZS', 'Hernaut', 'Loic',
        false, 'confirmed', '0478249544');

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('username', '$2a$10$EOyq/phHFeZu4dnCJIpS2e4GBwOrhMOkY3Oz5zIQWqjN23ziS1Wra', 'Lastname',
        'Firstname', false, 'denied', NULL);

INSERT INTO project_pae.items_types (item_type)
VALUES ('Décoration');

INSERT INTO project_pae.items (item_description, id_item_type, id_member, photo, title,
                               offer_status)
VALUES ('Cadre de la mort qui tue', 1, 1, 'photo', 'Cadre', 'donated');
