CREATE TABLE Enseignant(
    ID_ens INTEGER NOT NULL,
    nom VARCHAR(45) NOT NULL,
    prenom VARCHAR(45) NOT NULL,
    telephone VARCHAR(45) DEFAULT NULL,
    CONSTRAINT PK_ID_ens PRIMARY KEY(ID_ens)
); INSERT INTO Enseignant
VALUES(1, 'Dubois', 'Jean', ' 012345678');
INSERT INTO Enseignant
VALUES(2, 'LEGRAND', 'EMILIE', ' 012345678');
INSERT INTO Enseignant
VALUES(
    10,
    'Nom10',
    'Prenom10',
    ' 012345678'
);
INSERT INTO Enseignant
VALUES(
    11,
    'Nom10',
    'Prenom11',
    ' 012345678'
);
CREATE TABLE Etudiant(
    ID_etudiant INTEGER NOT NULL,
    nom VARCHAR(45) NOT NULL,
    prenom VARCHAR(45) NOT NULL,
    provenance VARCHAR(45) NOT NULL,
    Formation_precedente VARCHAR(45) NOT NULL,
    Pays_Formation_precedente VARCHAR(45) NOT NULL,
    Annee_debut INTEGER NOT NULL,
    Niveau_inscription VARCHAR(45) NOT NULL,
    telephone VARCHAR(45) DEFAULT NULL,
    dateNaissance DATE NOT NULL,
    CONSTRAINT PK_ID PRIMARY KEY(ID_etudiant)
); INSERT INTO Etudiant
VALUES(
    10,
    'Nom10',
    'Prenom10',
    'fr',
    'Licence',
    'fr',
    2008,
    'M1',
    '012345678',
    DATE('1985/03/05')
);
INSERT INTO Etudiant
VALUES(
    11,
    'Nom11',
    'Prenom11',
    'de',
    'M1',
    'fr',
    2009,
    'M2',
    '012345678',
    DATE('1985/09/08')
);
INSERT INTO Etudiant
VALUES(
    12,
    'Nom12',
    'Prenom12',
    'it',
    'Licence',
    'it',
    2008,
    'M1',
    '012345678',
    DATE('1987/07/02')
);
CREATE TABLE Cours(
    NumCours INTEGER NOT NULL,
    libele VARCHAR(45) NOT NULL,
    niveau VARCHAR(45) NOT NULL,
    TYPE VARCHAR(45) NOT NULL,
    CONSTRAINT PK_NumCours PRIMARY KEY(NumCours)
); INSERT INTO Cours
VALUES(1, 'SGBD', 'M1', 'CM');
INSERT INTO Cours
VALUES(2, 'SGBDA', 'M2', 'CM');
INSERT INTO Cours
VALUES(3, 'SGBD', 'M1', 'TD');
INSERT INTO Cours
VALUES(4, 'SGBDA', 'M2', 'TD');
INSERT INTO Cours
VALUES(5, 'ID', 'M2', 'CM');
INSERT INTO Cours
VALUES(6, 'ID', 'M2', 'TD');
CREATE TABLE Enseigne(
    NumEns INTEGER NOT NULL,
    NumCours INTEGER NOT NULL,
    Annee INTEGER NOT NULL,
    CONSTRAINT PK_Ens PRIMARY KEY(NumEns, NumCours, Annee),
    CONSTRAINT FK_Ens FOREIGN KEY(NumEns) REFERENCES Enseignant(ID_ens),
    CONSTRAINT FK_Cours FOREIGN KEY(NumCours) REFERENCES Cours(NumCours)
); INSERT INTO Enseigne
VALUES(1, 1, 2008);
INSERT INTO Enseigne
VALUES(2, 2, 2009);
INSERT INTO Enseigne
VALUES(10, 3, 2008);
INSERT INTO Enseigne
VALUES(11, 4, 2009);
INSERT INTO Enseigne
VALUES(10, 5, 2009);
INSERT INTO Enseigne
VALUES(11, 6, 2009);
CREATE TABLE Inscription(
    NumEt INTEGER NOT NULL,
    NumCours INTEGER NOT NULL,
    Annee INTEGER NOT NULL,
    Note_cours INTEGER NOT NULL,
    CONSTRAINT FK_etudiant FOREIGN KEY(NumEt) REFERENCES Etudiant(ID_etudiant),
    CONSTRAINT FK_cours_ins FOREIGN KEY(NumCours) REFERENCES Cours(NumCours),
    CONSTRAINT PK_LC PRIMARY KEY(NumEt, NumCours, Annee)
); INSERT INTO Inscription
VALUES(10, 1, 2008, 10);
INSERT INTO Inscription
VALUES(11, 2, 2009, 12);
INSERT INTO Inscription
VALUES(12, 1, 2008, 14);
INSERT INTO Inscription
VALUES(10, 3, 2008, 16);
INSERT INTO Inscription
VALUES(11, 4, 2009, 10);
INSERT INTO Inscription
VALUES(12, 3, 2008, 12);
INSERT INTO Inscription
VALUES(11, 5, 2009, 14);
INSERT INTO Inscription
VALUES(11, 6, 2009, 16);