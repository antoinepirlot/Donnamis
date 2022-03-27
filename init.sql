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
    unit_number     VARCHAR(10) NULL,
    postcode        VARCHAR(10)  NOT NULL,
    commune         VARCHAR(100) NOT NULL,
    id_member       INTEGER      NOT NULL,
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.items
(
    id_item          SERIAL PRIMARY KEY,
    item_description VARCHAR(500) NOT NULL,
    id_type     INTEGER      NOT NULL,
    id_member        INTEGER      NOT NULL,
    photo            VARCHAR(500) NULL,
    title            VARCHAR(50)  NOT NULL,
    offer_status     VARCHAR(10)  NOT NULL,
    FOREIGN KEY (id_type) REFERENCES project_pae.items_types (id_type),
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
-- MEMBERS TABLE
--Mot de passe : password
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('caro', '$2a$10$xakKOBvypQxUzlYFc9Nhz.fJ6voiJsnGzKpmelMZnqJWB5GLTibGq', 'Line',
        'Caroline', false, 'denied', NULL);
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('achil', '$2a$10$HNgw3caDAFIA4vr4U6sW7OOSZORCLLf./yQOnYxYvbWir88ZTDzZ6', 'Ile',
        'Achille', false, 'registered', NULL);
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('bazz', '$2a$10$1biHuxC0nR6Vvjyj8NW9XedY3LCCcwbQyAkCUG8IJmfte/OU4Kxfm', 'Ile', 'Basile',
        false, 'confirmed', NULL);
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('bri', '$2a$10$gafrRJ.P4GA8TkHkcCgPjuB.U5okgAOn3jXeA/HPAe7xqE89PYuhy', 'Lehmann',
        'Brigitte', true, 'confirmed', NULL);

-- ADDRESS TABLE
INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member)
VALUES ('Rue de l’Eglise', '11', 'B1', '4987', 'Stroumont', 1);
INSERT INTO project_pae.addresses (street, building_number, postcode, commune, id_member)
VALUES ('Rue de Renkin', '7', '4800', 'Verviers', 2);
INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member)
VALUES ('Rue Haute Folie', '6', 'A103', '4800', 'Verviers', 3);
INSERT INTO project_pae.addresses (street, building_number, postcode, commune, id_member)
VALUES ('Haut-Vinâve', '13', '4845', 'Jalhay', 4);

-- REFUSALS TABLE
INSERT INTO project_pae.refusals (text, id_member)
VALUES ('Il faudra patienter un jour ou deux.', 1);

-- ITEMS TYPE TABLE
INSERT INTO project_pae.items_types (item_type)
VALUES ('Accessoires pour animaux domestiques'); -- 1
INSERT INTO project_pae.items_types (item_type)
VALUES ('Accessoire pour voiture'); -- 2
INSERT INTO project_pae.items_types (item_type)
VALUES ('Décoration'); -- 3
INSERT INTO project_pae.items_types (item_type)
VALUES ('Jouets'); -- 4
INSERT INTO project_pae.items_types (item_type)
VALUES ('Literie'); -- 5
INSERT INTO project_pae.items_types (item_type)
VALUES ('Matériel de cuisine'); -- 6
INSERT INTO project_pae.items_types (item_type)
VALUES ('Matériel de jardinage'); -- 7
INSERT INTO project_pae.items_types (item_type)
VALUES ('Meuble'); -- 8
INSERT INTO project_pae.items_types (item_type)
VALUES ('Plantes'); -- 9
INSERT INTO project_pae.items_types (item_type)
VALUES ('Produits cosmétiques'); -- 10
INSERT INTO project_pae.items_types (item_type)
VALUES ('Vélo trotinette'); -- 11
INSERT INTO project_pae.items_types (item_type)
VALUES ('Vêtements');
-- 12

-- ITEMS TABLE
INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title,
                               offer_status)
VALUES ('Décorations de Noël de couleur rouge', 3, 3, 'christmas-1869533_640', 'Titre', 'donated');
INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title,
                               offer_status)
VALUES ('Cadre représentant un chien noir sur un fond noir.', 3, 3, 'dog-4118585_640', 'Titre',
        'donated');
INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title,
                               offer_status)
VALUES ('Ancien bureau d’écolier.', 8, 4, 'BureauEcolier-7', 'Titre', 'donated');

-- OFFERS TABLE
INSERT INTO project_pae.offers ("date", time_slot, id_item)
VALUES ('21-03-22', 'Mardi de 17h à 22h', 1);
INSERT INTO project_pae.offers ("date", time_slot, id_item)
VALUES ('25-03-22', 'Lundi de 18h à 22h', 2);
INSERT INTO project_pae.offers ("date", time_slot, id_item)
VALUES ('25-03-22', 'Tous les jours de 15h à 18h', 3);

-- SELECT
SELECT m.id_member,
       m.username,
       m.is_admin,
       m.state,
       m.phone,
       r.text
FROM project_pae.members m,
     project_pae.refusals r
WHERE m.id_member = r.id_member
ORDER BY m.state;

SELECT i.id_item,
       i.item_description,
       it.item_type,
       i.offer_status,
       min(o.date) AS "Date de la première offre"
FROM project_pae.items i,
     project_pae.items_types it,
     project_pae.offers o
WHERE i.id_item_type = it.id_type
  AND i.id_item = o.id_item
GROUP BY i.id_item,
         i.item_description,
         it.item_type,
         i.offer_status
ORDER BY 5 DESC;

SELECT m.last_name,
       i.item_description
FROM project_pae.items i,
     project_pae.members m
WHERE i.id_member = m.id_member
GROUP BY m.last_name,
         i.item_description
ORDER BY m.last_name,
         i.item_description;