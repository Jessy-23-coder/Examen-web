-- CREATE DATABASE tp_flight CHARACTER SET utf8mb4;

-- USE tp_flight;

-- CREATE TABLE etudiant (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     nom VARCHAR(100),
--     prenom VARCHAR(100),
--     email VARCHAR(100),
--     age INT
-- );

CREATE DATABASE ef_financier CHARACTER SET utf8mb4;
USE ef_financier;


-- 1. Table pour les fonds déposés dans l’établissement
CREATE TABLE fonds (
    id INT AUTO_INCREMENT PRIMARY KEY,
    origine VARCHAR(100),           -- ex: Investisseur, Subvention, Client
    montant DOUBLE NOT NULL,
    date_depot DATE NOT NULL
);


-- 2. Table des types de prêts (ex : immobilier, consommation, etc.)
CREATE TABLE TypePret (
    id_type_pret INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL,
    duree INT
);


CREATE TABLE TauxInteret (
    id_taux INT PRIMARY KEY AUTO_INCREMENT,
    id_type_pret INT NOT NULL,
    taux DECIMAL(5,2) NOT NULL, -- taux annuel
    date_application DATE NOT NULL,
    date_fin DATE,
    simple BOOLEAN,
    description VARCHAR(200),
    FOREIGN KEY (id_type_pret) REFERENCES TypePret(id_type_pret)
);



-- 3. Table des clients
CREATE TABLE client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    telephone VARCHAR(30)
);


-- 4. Table des prêts accordés aux clients
CREATE TABLE pret (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_type_pret INT NOT NULL,
    montant DOUBLE NOT NULL,
    date_debut DATE NOT NULL,
    duree_mois INT NOT NULL, -- ex : 12, 24, 36 mois
    FOREIGN KEY (id_client) REFERENCES client(id),
    FOREIGN KEY (id_type_pret) REFERENCES type_pret(id)
);