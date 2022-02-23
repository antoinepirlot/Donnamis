DROP SCHEMA IF EXISTS project_pae CASCADE;
CREATE SCHEMA project_pae;

-------------------CREATE TABLES--------------------------
CREATE TABLE project_pae.members
(
    id_member SERIAL PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL,
    password  CHAR(60)     NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    is_admin BOOLEAN NOT NULL,
    state VARCHAR(10) NOT NULL,
    phone VARCHAR(15)
);

-------------------INSERT INTO--------------------------
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('nikesakou', 'K72FdDqcLMZwq9dq37NQ6ysyvADtKZbWZuUMDeEcGNqrrnau8JKMbLBdSSkQ', 'Dimitriadis',
        'Nicolas', true, 'confirmed', NULL);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('lepirelot', 'K72FdDqcLMZwq9dq37NQ6ysyvADtKZbWZuUMDeEcGNqrrnau8JKMbLBdSSkQ', 'Pirlot',
        'Antoine', true, 'registered', NULL);

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('Navial', 'K72FdDqcLMZwq9dq37NQ6ysyvADtKZbWZuUMDeEcGNqrrnau8JKMbLBdSSkQ', 'Denis', 'Victor',
        false, 'confirmed', '0479489137');
INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('Swapiz', 'K72FdDqcLMZwq9dq37NQ6ysyvADtKZbWZuUMDeEcGNqrrnau8JKMbLBdSSkQ', 'Hernaut', 'Loic',
        false, 'confirmed', '0478249544');

INSERT INTO project_pae.members (username, password, last_name, first_name, is_admin, state, phone)
VALUES ('username', 'K72FdDqcLMZwq9dq37NQ6ysyvADtKZbWZuUMDeEcGNqrrnau8JKMbLBdSSkQ', 'Lastname',
        'Firstname', false, 'denied', NULL);