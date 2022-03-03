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