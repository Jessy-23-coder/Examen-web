-- Création de la base de données
CREATE DATABASE gestion_vols;
USE gestion_vols;


CREATE Table admin(
  id_admin int AUTO_INCREMENT PRIMARY KEY,
  mail VARCHAR(100),
  passe VARCHAR(100)  
);

insert into admin(mail,passe) VALUES("admin@gmail.com","admin");

CREATE Table client(
  id_client int AUTO_INCREMENT PRIMARY KEY,
  mail VARCHAR(100),
  passe VARCHAR(100)  
);

insert into client(mail,passe) VALUES("client@gmail.com","client");


-- Table CompagnieAerienne
CREATE TABLE CompagnieAerienne (
    id_compagnie INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    code_IATA CHAR(2) NOT NULL,
    logo VARCHAR(255),
    description TEXT,
    date_creation DATE,
    UNIQUE (code_IATA)
);

-- Table Avion
CREATE TABLE Avion (
    id_avion INT AUTO_INCREMENT PRIMARY KEY,
    id_compagnie INT NOT NULL,
    modele VARCHAR(50) NOT NULL,
    capacite INT NOT NULL,
    annee_fabrication YEAR NOT NULL,
    statut VARCHAR(100),
    FOREIGN KEY (id_compagnie) REFERENCES CompagnieAerienne(id_compagnie)
);

-- Table Aeroport
CREATE TABLE Aeroport (
    id_aeroport INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    code_IATA CHAR(3) NOT NULL,
    ville VARCHAR(50) NOT NULL,
    pays VARCHAR(50) NOT NULL,
    terminal VARCHAR(20),
    UNIQUE (code_IATA)
);


-- Table Personnel
CREATE TABLE Personnel (
    id_personnel INT AUTO_INCREMENT PRIMARY KEY,
    id_compagnie INT NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    fonction VARCHAR(100),
    FOREIGN KEY (id_compagnie) REFERENCES CompagnieAerienne(id_compagnie)
);


-- Table AffectationPersonnel
CREATE TABLE Equipage (
    id_equipage INT AUTO_INCREMENT PRIMARY KEY,
    Commandant_de_bord INT NOT NULL,
    Pilote INT NOT NULL,
    Copilote INT NOT NULL,
    Chef_de_cabine INT NOT NULL,
    Hôtesse INT NOT NULL,
    Steward INT NOT NULL,
    FOREIGN KEY (Commandant_de_bord) REFERENCES Personnel(id_personnel),
    FOREIGN KEY (Pilote) REFERENCES Personnel(id_personnel),
    FOREIGN KEY (Copilote) REFERENCES Personnel(id_personnel),
    FOREIGN KEY (Chef_de_cabine) REFERENCES Personnel(id_personnel),
    FOREIGN KEY (Hôtesse) REFERENCES Personnel(id_personnel),
    FOREIGN KEY (Steward) REFERENCES Personnel(id_personnel)
);

CREATE Table classe(
    id_classe INT AUTO_INCREMENT PRIMARY KEY, 
    nom VARCHAR(100)
);


-- Table Vol
CREATE TABLE Vol (
    id_vol INT AUTO_INCREMENT PRIMARY KEY,
    id_avion INT NOT NULL,
    id_aeroport_depart INT NOT NULL,
    id_aeroport_arrivee INT NOT NULL,
    date_heure_depart DATETIME NOT NULL,
    date_heure_arrivee DATETIME NOT NULL,
    numero_vol VARCHAR(10) NOT NULL,
    statut VARCHAR(100),
    porte_embarquement VARCHAR(10),
    equipage INT NOT NULL,
    FOREIGN KEY (id_avion) REFERENCES Avion(id_avion),
    FOREIGN KEY (id_aeroport_depart) REFERENCES Aeroport(id_aeroport),
    FOREIGN KEY (id_aeroport_arrivee) REFERENCES Aeroport(id_aeroport),
    FOREIGN KEY (equipage) REFERENCES Equipage(id_equipage),
    CHECK (date_heure_arrivee > date_heure_depart)
);


CREATE TABLE classvol (
    id_classevol INT AUTO_INCREMENT PRIMARY KEY,
    id_vol INT NOT NULL,
    id_classe INT NOT NULL,
    prix_adulte DOUBLE NOT NULL,
    prix_enfant DOUBLE NOT NULL,
    places_disponibles INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_vol) REFERENCES vol(id_vol) ON DELETE CASCADE,
    FOREIGN KEY (id_classe) REFERENCES classe(id_classe) ON DELETE RESTRICT
);

ALTER TABLE classvol
ADD COLUMN prix_initial_adulte DOUBLE,
ADD COLUMN prix_initial_enfant DOUBLE,
ADD COLUMN date_activation DATETIME,
ADD COLUMN delais_jours INT DEFAULT 1,
ADD COLUMN pourcentage_augmentation DOUBLE DEFAULT 100;

ALTER TABLE classvol
ADD COLUMN prix_base_adulte DOUBLE NOT NULL DEFAULT 0,
ADD COLUMN prix_base_enfant DOUBLE NOT NULL DEFAULT 0,
ADD COLUMN date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN delai_jours INT NOT NULL DEFAULT 7;

DELIMITER //
CREATE EVENT IF NOT EXISTS mise_a_jour_prix_minute
ON SCHEDULE EVERY 1 MINUTE
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    -- Double les prix pour tous les enregistrements
    UPDATE classvol 
    SET 
        prix_adulte = prix_adulte * 2,
        prix_enfant = prix_enfant * 2
    WHERE 1=1; -- Condition toujours vraie pour tous les enregistrements
END //
DELIMITER ;

-- DELIMITER //
-- CREATE FUNCTION get_prix_actuel(
--     prix_initial DOUBLE,
--     date_activation DATETIME,
--     delais_jours INT,
--     pourcentage_augmentation DOUBLE
-- ) RETURNS DOUBLE
-- DETERMINISTIC
-- BEGIN
--     DECLARE prix_actuel DOUBLE;
    
--     IF DATEDIFF(NOW(), date_activation) >= delais_jours THEN
--         SET prix_actuel = prix_initial * (1 + pourcentage_augmentation/100);
--     ELSE
--         SET prix_actuel = prix_initial;
--     END IF;
    
--     RETURN ROUND(prix_actuel, 2);
-- END //
-- DELIMITER ;


-- DELIMITER //
-- CREATE TRIGGER before_insert_classvol
-- BEFORE INSERT ON classvol
-- FOR EACH ROW
-- BEGIN
--     SET NEW.prix_adulte = NEW.prix_initial_adulte;
--     SET NEW.prix_enfant = NEW.prix_initial_enfant;
-- END //
-- DELIMITER ;



-- DELIMITER //
-- CREATE EVENT IF NOT EXISTS mise_a_jour_prix_auto
-- ON SCHEDULE EVERY 1 DAY
-- STARTS CURRENT_TIMESTAMP
-- DO
-- BEGIN
--     UPDATE classvol 
--     SET prix_adulte = IF(DATEDIFF(NOW(), date_creation) >= delai_jours, 
--                       prix_base_adulte * 2, 
--                       prix_base_adulte),
--         prix_enfant = IF(DATEDIFf(NOW(), date_creation) >= delai_jours, 
--                       prix_base_enfant * 2, 
--                       prix_base_enfant)
--     WHERE DATEDIFF(NOW(), date_creation) >= delai_jours;
-- END //
-- DELIMITER ;


-- Table Reservation
CREATE TABLE Reservation (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_vol INT NOT NULL,
    date_reservation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_classevol int NOT NULL,
    adulte int,
    enfant int,
    touslespersonnes int,
    statut VARCHAR(100),
    FOREIGN KEY (id_vol) REFERENCES Vol(id_vol),
    FOREIGN KEY (id_classevol) REFERENCES classvol(id_classevol)
);

-- Table Passager
CREATE TABLE Passager (
    id_passager INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    numero_passeport VARCHAR(20) NOT NULL,
    nationalite VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    telephone VARCHAR(20),
    UNIQUE (numero_passeport)
);

-- Table Bagage
CREATE TABLE Bagage (
    id_bagage INT AUTO_INCREMENT PRIMARY KEY,
    id_reservation INT NOT NULL,
    poids DECIMAL(5,2) NOT NULL,
    type ENUM('Cabine', 'Soute') NOT NULL,
    statut ENUM('enregistré', 'chargé', 'perdu', 'livré') DEFAULT 'enregistré',
    FOREIGN KEY (id_reservation) REFERENCES Reservation(id_reservation)
);


-- Création d'un index pour améliorer les performances de recherche
CREATE INDEX idx_vol_dates ON Vol(date_heure_depart, date_heure_arrivee);
CREATE INDEX idx_reservation_vol ON Reservation(id_vol);
CREATE INDEX idx_reservation_passager ON Reservation(id_passager);



CREATE VIEW Vue_Vols AS
SELECT 
    v.id_vol,
    v.numero_vol,
    c.nom AS compagnie_aerienne,
    c.code_IATA AS code_compagnie,
    a.modele AS avion_modele,
    a.capacite,
    dep.nom AS aeroport_depart,
    dep.code_IATA AS code_depart,
    arr.nom AS aeroport_arrivee,
    arr.code_IATA AS code_arrivee,
    v.date_heure_depart,
    v.date_heure_arrivee,
    TIMESTAMPDIFF(MINUTE, v.date_heure_depart, v.date_heure_arrivee) AS duree_vol_minutes,
    v.statut,
    v.porte_embarquement,
    v.equipage,
    CONCAT(dep.ville, ', ', dep.pays) AS ville_depart,
    CONCAT(arr.ville, ', ', arr.pays) AS ville_arrivee
FROM 
    Vol v
JOIN 
    Avion a ON v.id_avion = a.id_avion
JOIN 
    CompagnieAerienne c ON a.id_compagnie = c.id_compagnie
JOIN 
    Aeroport dep ON v.id_aeroport_depart = dep.id_aeroport
JOIN 
    Aeroport arr ON v.id_aeroport_arrivee = arr.id_aeroport;
    

-- INSERT INTO CompagnieAerienne (nom, code_IATA, logo, description, date_creation) VALUES
-- ('Air France', 'AF', 'airfrance.png', 'Compagnie nationale française', '1933-10-07');


-- -- Insertion des compagnies aériennes
INSERT INTO CompagnieAerienne (nom, code_IATA, logo, description, date_creation) VALUES
('Air France', 'AF', 'airfrance.png', 'Compagnie nationale française', '1933-10-07'),
('Emirates', 'EK', 'emirates.png', 'Compagnie des Émirats Arabes Unis', '1985-03-25'),
('Lufthansa', 'LH', 'lufthansa.png', 'Compagnie allemande', '1953-01-06'),
('Delta Air Lines', 'DL', 'delta.png', 'Compagnie américaine', '1928-05-30'),
('Qatar Airways', 'QR', 'qatar.png', 'Compagnie du Qatar', '1993-11-22');

-- Insertion des avions

INSERT INTO Avion (id_compagnie, modele, capacite, annee_fabrication, statut) VALUES
(1, 'Airbus A380', 516, 2015, 'en service'),
(1, 'Boeing 777', 310, 2018, 'en service'),
(2, 'Airbus A350', 369, 2020, 'en service'),
(3, 'Boeing 747', 364, 2017, 'maintenance'),
(4, 'Airbus A330', 293, 2019, 'en service'),
(5, 'Boeing 787', 335, 2021, 'en service');


-- Insertion des aéroports
INSERT INTO Aeroport (nom, code_IATA, ville, pays, terminal) VALUES
('Charles de Gaulle', 'CDG', 'Paris', 'France', '2F'),
('Dubai International', 'DXB', 'Dubai', 'Émirats Arabes Unis', '3'),
('Frankfurt Airport', 'FRA', 'Francfort', 'Allemagne', '1'),
('John F. Kennedy', 'JFK', 'New York', 'États-Unis', '4'),
('Hamad International', 'DOH', 'Doha', 'Qatar', 'A'),
('Orly', 'ORY', 'Paris', 'France', 'Ouest');

-- Insertion du personnel
INSERT INTO Personnel (id_compagnie, nom, prenom, fonction) VALUES
(1, 'Jean', 'Pierre', 'Pilote'),
(1, 'George', 'Paul', 'Pilote'),
(1, 'Goudin', 'Verne', 'Copilote'),
(1, 'Martin', 'Pierre', 'Copilote'),
(1, 'Bernard', 'Sophie', 'Hôtesse'),
(1, 'Bernard', 'Sophie', 'Hôtesse'),
(2, 'Al-Maktoum', 'Ahmed', 'Steward'),
(3, 'Schmidt', 'Klaus', 'Copilote'),
(4, 'Wilson', 'Bent', 'Commandant de bord'),
(5, 'Al-Thani', 'Youssef', 'Technicien'),
(5, 'Al-Mad', 'Gii', 'Chef_de_cabine'),
(5, 'Alad', 'Donne', 'Chef_de_cabine');

-- Insertion des affectations du personnel
INSERT INTO Equipage (Commandant_de_bord,Pilote, Copilote, Chef_de_cabine,Hôtesse,Steward) VALUES
(4, 1, 3, 12, 5, 7),
(4, 2, 4, 12, 5, 7),
(4, 2, 3, 12, 5, 7),
(4, 1, 4, 11, 5, 7),
(4, 1, 3, 11, 5, 7),
(4, 2, 4, 11, 5, 7);

-- Insertion des vols
INSERT INTO Vol (id_avion, id_aeroport_depart, id_aeroport_arrivee, date_heure_depart, date_heure_arrivee, numero_vol, statut, porte_embarquement,equipage) VALUES
(1, 1, 2, '2023-12-15 08:00:00', '2023-12-15 16:30:00', 'AF380', 'à l heure', 'A12',1),
(2, 1, 3, '2023-12-15 10:30:00', '2023-12-15 12:00:00', 'AF777', 'retardé', 'B07',2),
(3, 2, 5, '2023-12-15 14:00:00', '2023-12-15 18:30:00', 'EK501', 'à l heure', 'C33',3),
(4, 3, 4, '2023-12-16 09:15:00', '2023-12-16 14:45:00', 'LH400', 'à l heure', 'D15',4),
(5, 4, 1, '2023-12-16 18:00:00', '2023-12-17 07:30:00', 'DL200', 'embarquement', 'E22',5),
(6, 5, 2, '2023-12-17 07:30:00', '2023-12-17 11:45:00', 'QR350', 'à l heure', 'F10',6);


-- Insertion des passagers
INSERT INTO Passager (nom, prenom, date_naissance, numero_passeport, nationalite, email, telephone) VALUES
('Dupont', 'Jean', '1985-07-15', '79DF12345', 'Française', 'jean.dupont@email.com', '+33612345678'),
('Smith', 'Emma', '1990-11-22', 'US1234567', 'Américaine', 'emma.smith@email.com', '+12105551234'),
('Müller', 'Hans', '1978-03-30', 'DE3456789', 'Allemande', 'hans.muller@email.com', '+491701234567'),
('Al-Farsi', 'Layla', '1995-05-18', 'AE7890123', 'Émiratie', 'layla.alfarsi@email.com', '+971501234567'),
('Johnson', 'Michael', '1982-09-10', 'UK4567890', 'Britannique', 'michael.johnson@email.com', '+447912345678'),
('Kim', 'Ji-hoon', '1993-12-05', 'KR5678901', 'Coréenne', 'jihoon.kim@email.com', '+821012345678');

-- Insertion des réservations
INSERT INTO Reservation (id_vol, id_passager, date_reservation, classe, numero_siege, prix, statut) VALUES
(1, 1, '2023-11-20 14:30:00', 'Affaires', '12A', 1200.00, 'confirmé'),
(1, 2, '2023-11-22 10:15:00', 'Première', '1A', 2500.00, 'confirmé'),
(2, 3, '2023-11-25 16:45:00', 'Economique', '28B', 450.00, 'confirmé'),
(3, 4, '2023-11-28 09:30:00', 'Affaires', '8C', 1800.00, 'confirmé'),
(4, 5, '2023-12-01 11:20:00', 'Economique', '32F', 550.00, 'confirmé'),
(5, 6, '2023-12-05 13:10:00', 'Première', '2A', 3000.00, 'confirmé');



-- Insertion des bagages
INSERT INTO Bagage (id_reservation, poids, type, statut) VALUES
(1, 23.5, 'Soute', 'chargé'),
(1, 8.0, 'Cabine', 'enregistré'),
(2, 30.0, 'Soute', 'chargé'),
(3, 18.7, 'Soute', 'enregistré'),
(4, 25.0, 'Soute', 'chargé'),
(5, 12.3, 'Soute', 'enregistré');


-- Insertion des bagages
INSERT INTO classe (nom) VALUES
('Economique'),
('Affaires'),
('Première');









    statut ENUM('en service', 'maintenance', 'hors service') DEFAULT 'en service',
    statut ENUM('à l heure', 'retardé', 'annulé', 'embarquement', 'décollé', 'atterri') DEFAULT 'à l heure',
    classe ENUM('Economique', 'Affaires', 'Première') NOT NULL,
    statut ENUM('confirmé', 'annulé', 'en attente') DEFAULT 'confirmé',
    fonction ENUM('Pilote', 'Copilote', 'Hôtesse', 'Steward', 'Technicien', 'Agent de bord') NOT NULL,
    role ENUM('Commandant de bord', 'Copilote', 'Chef de cabine', 'Hôtesse', 'Steward') NOT NULL,



SELECT * FROM admin WHERE mail = "admin@gmail.com" AND passe = "admin";

