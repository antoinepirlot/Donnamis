DROP SCHEMA IF EXISTS project_pae CASCADE;
CREATE SCHEMA project_pae;

-------------------CREATE TABLES--------------------------
CREATE TABLE project_pae.members
(
    id_member      SERIAL PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL,
    password       CHAR(60)     NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    first_name     VARCHAR(100) NOT NULL,
    is_admin       BOOLEAN      NOT NULL,
    state          VARCHAR(11)  NOT NULL,
    phone          VARCHAR(15),
    version_member INT          NOT NULL
);

CREATE TABLE project_pae.items_types
(
    id_type            SERIAL PRIMARY KEY,
    item_type          VARCHAR(50) NOT NULL,
    version_items_type INT         NOT NULL
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
    version_address INT          NOT NULL,
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

--CREATE TABLE project_pae.photos
--(
--    id_photo   SERIAL PRIMARY KEY,
--    link_photo VARCHAR(200) NOT NULL
--);

CREATE TABLE project_pae.items
(
    id_item          SERIAL PRIMARY KEY,
    item_description VARCHAR(500) NOT NULL,
    id_type          INTEGER      NOT NULL,
    id_member        INTEGER      NOT NULL,
    photo            VARCHAR(500) NULL,
    title            VARCHAR(50)  NOT NULL,
    offer_status     VARCHAR(10)  NOT NULL,
    last_offer_date  TIMESTAMP NULL,
    version_item     INT          NOT NULL,
    FOREIGN KEY (id_type) REFERENCES project_pae.items_types (id_type),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.ratings
(
    id_rating      SERIAL PRIMARY KEY,
    rating         INTEGER      NOT NULL,
    text           VARCHAR(500) NOT NULL,
    id_member      INTEGER      NOT NULL,
    id_item        INTEGER      NOT NULL,
    version_rating INT          NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.offers
(
    id_offer            SERIAL PRIMARY KEY,
    date                TIMESTAMP   NOT NULL,
    time_slot           VARCHAR(50) NOT NULL,
    id_item             INTEGER     NOT NULL,
    number_of_interests INT         NOT NULL,
    version_offer       INT         NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item)
);

CREATE TABLE project_pae.interests
(
    id_interest      SERIAL PRIMARY KEY,
    call_wanted      BOOLEAN   NOT NULL,
    id_offer         INTEGER   NOT NULL,
    id_member        INTEGER   NOT NULL,
    date             TIMESTAMP NOT NULL,
    version_interest INT       NOT NULL,
    FOREIGN KEY (id_offer) REFERENCES project_pae.offers (id_offer),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);

CREATE TABLE project_pae.refusals
(
    id_refusal      SERIAL PRIMARY KEY,
    text            VARCHAR(500) NOT NULL,
    id_member       INTEGER      NOT NULL,
    version_refusal INT          NOT NULL,
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)
);


CREATE TABLE project_pae.recipients
(
    id_recipient      SERIAL PRIMARY KEY,
    id_item           INTEGER     NOT NULL,
    id_member         INTEGER     NOT NULL,
    received          VARCHAR(15) NOT NULL,
    version_recipient INT         NOT NULL,
    FOREIGN KEY (id_item) REFERENCES project_pae.items (id_item),
    FOREIGN KEY (id_member) REFERENCES project_pae.members (id_member)

);

-------------------INSERT INTO--------------------------
-- MEMBERS TABLE
--Mot de passe : password
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('caro', '$2a$10$tfvcdll/tXK3fZRjmhQUNepc/StPcsWkNPhEhM8lfCsCAAQsDyEH6', 'Line', 'Caroline',
        true, 'confirmed', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('achil', '$2a$10$tfvcdll/tXK3fZRjmhQUNepc/StPcsWkNPhEhM8lfCsCAAQsDyEH6', 'Ile', 'Achille',
        false, 'denied', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('bazz', '$2a$10$tfvcdll/tXK3fZRjmhQUNepc/StPcsWkNPhEhM8lfCsCAAQsDyEH6', 'Ile', 'Basile',
        false, 'confirmed', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('bri', '$2a$10$ZHOKQPhQWXumBaL9n5Lc1.Ib0P4NhRCqtC6gHRPcmpz4p5.Nd1sDq', 'Lehmann',
        'Brigitte', true, 'confirmed', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('theo', '$2a$10$UbT5OFs15x0NMGX9wyLqJ.Stdax1i5Er2vUGPhM4AgEwwDoIrpGkK', 'Ile', 'Théophile',
        false, 'confirmed', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('emi', '$2a$10$a7NZyB3bP7mdDejWrX9odurqJLdPxsOWWLuin3hhonMTvP5evyJsS', 'Ile', 'Emile',
        false, 'denied', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('cora', '$2a$10$dbFNL.1zpe4hZ2jWNv6owOre0sNFlarNaWeRApiYaq/mIzzB2aBkm', 'Line', 'Coralie',
        false, 'denied', NULL, 1);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone,
                                 version_member)
VALUES ('charline', '$2a$10$ecRQNuMsNRxjuNagjEpPvue5ld8QN5T9N6Q8hlzhM9M8nJflmBrw.', 'Line',
        'Charles', false, 'registered', NULL, 1);


-- ADDRESS TABLE
INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue de l''Eglise', '11', 'B1', '4987', 'Stoumont', 1, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue de Renkin', '7', NULL, '4800', 'Verviers', 2, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue Haute Folie', '6', 'A103', '4800', 'Verviers', 3, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Haut-Vinâve', '13', NULL, '4845', 'Jalhay', 4, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue de Rekin', '7', NULL, '4800', 'Verviers', 5, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue de Verviers', '47', NULL, '4000', 'Liège', 6, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue de salpêtré', '789', 'Bis', '1040', 'Bruxelles', 7, 1);

INSERT INTO project_pae.addresses (street, building_number, unit_number, postcode, commune,
                                   id_member, version_address)
VALUES ('Rue des Minières', '45', 'Ter', '4800', 'Verviers', 8, 1);

-- REFUSALS TABLE
INSERT INTO project_pae.refusals (text, id_member, version_refusal)
VALUES ('L''application n''est pas encore ouverte à tous.', 2, 1);

INSERT INTO project_pae.refusals (text, id_member, version_refusal)
VALUES ('L''application n''est pas encore ouverte à tous.', 6, 1);

INSERT INTO project_pae.refusals (text, id_member, version_refusal)
VALUES ('Vous devez encore attendre quelques jours.', 7, 1);

-- ITEMS TYPE TABLE
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Accessoires pour animaux domestiques', 1); -- 1
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Accessoire pour voiture', 1); -- 2
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Décoration', 1); -- 3
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Jouets', 1); -- 4
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Literie', 1); -- 5
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Matériel de cuisine', 1); -- 6
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Matériel de jardinage', 1); -- 7
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Meuble', 1); -- 8
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Plantes', 1); -- 9
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Produits cosmétiques', 1); -- 10
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Vélo trotinette', 1); -- 11
INSERT INTO project_pae.items_types (item_type, version_items_type)
VALUES ('Vêtements', 1);
-- 12

-- ITEMS TABLE
INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Décorations de Noël de couleur rouge', 3, 3, 'christmas-1869533_640.png', 'Déco',
        'cancelled', '21-03-22', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Cadre représentant un chien noir sur un fond noir.', 3, 3, 'dog-4118585_640.jpg', 'Cadre',
        'donated', '25-03-22', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Ancien bureau d’écolier.', 8, 4, 'BureauEcolier-7.JPG', 'Titre', 'donated', '25-03-22', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos', 7, 5,
        'wheelbarrows-4566619_640.jpg', 'Brouette', 'donated', '28-03-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Scie sur perche Gardena', 7, 5, NULL, 'Scie', 'donated', '28-03-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Table jardin et deux chaises en bois', 8, 5, 'Table-jardin.jpg', 'Table de jardin',
        'donated',
        '29-03-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Table bistro', 8, 5, 'table-bistro.jpg', 'Table', 'donated', '20-03-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Table bistro ancienne de couleur bleue', 8, 1, 'table-bistro-carree-bleue.jpg', 'Table',
        'donated', '14-04-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Tableau noir pour enfant', 4, 5, 'Tableau.jpg', 'Tableau', 'assigned', '14-04-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Cadre cottage naïf', 3, 5, 'cadre-cottage-1178704_640.jpg', 'Cadre', 'donated',
        '21-04-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Tasse de couleur claire rose & mauve', 6, 5, 'tasse-garden-5037113_640.jpg', 'Tasse',
        'donated', '21-04-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Pâquerettes dans pots rustiques', 9, 1, 'pots-daisy-181905_640.jpg', 'Pâquerettes',
        'assigned', '21-04-2022', 1);

INSERT INTO project_pae.items (item_description, id_type, id_member, photo, title, offer_status,
                               last_offer_date, version_item)
VALUES ('Pots en grès pour petites plantes', 9, 1, 'pots-plants-6520443_640.jpg', 'Pot', 'donated',
        '21-04-2022', 1);

-- OFFERS TABLE
INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('21-03-22', 'Mardi de 17h à 22h', 1, 0, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('25-03-22', 'Lundi de 18h à 22h', 2, 0, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('25-03-22', 'Tous les jours de 15h à 18h', 3, 2, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('28-03-22', 'Tous les matins avant 11h30', 4, 3, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('29-03-22', 'Tous les matins avant 11h30', 5, 0, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('29-03-22', 'En semaine, de 20h à 21h', 6, 0, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('30-03-22', 'Lundi de 18h à 20h', 7, 0, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('14-04-22', 'Samedi en journée', 8, 2, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('14-04-22', 'Lundi de 18h à 20h', 9, 1, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('21-04-22', 'Lundi de 18h30 à 20h', 10, 3, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('21-04-22', 'En semaine, de 20h à 21h', 11, 2, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('21-04-22', 'Lundi de 16h à 17h', 12, 1, 1);

INSERT INTO project_pae.offers ("date", time_slot, id_item, number_of_interests, version_offer)
VALUES ('21-04-22', 'Lundi de 16h à 17h', 13, 0, 1);

-- INTEREST TABLE
INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 3, 5, '16-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 3, 3, '17-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 4, 4, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 4, 3, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 4, 1, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 8, 5, '14-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 8, 4, '14-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 9, 1, '16-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 10, 4, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 10, 3, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 10, 1, '02-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 11, 3, '16-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 11, 1, '16-05-2022', 1);

INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, version_interest)
VALUES (false, 12, 3, '16-05-2022', 1);

-- RECIPIENTS TABLE
INSERT INTO project_pae.recipients (id_item, id_member, received, version_recipient)
VALUES (9, 1, 'waiting', 1);

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
WHERE i.id_type = it.id_type
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

