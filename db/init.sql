
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    accountId INT UNSIGNED NOT NULL AUTO_INCREMENT,
    accountName VARCHAR(30) NOT NULL UNIQUE,
    accountToken VARCHAR(40) NOT NULL UNIQUE,
    accountEsIndexName VARCHAR(40) NOT NULL UNIQUE,
    PRIMARY KEY (accountId)
);

INSERT INTO Account (accountName, accountToken, accountEsIndexName)
VALUES ("root", "YZZXfOLKfTJEMGgKknWaKOpURnvALnRi", "logz-jopnbwmknooanqwzxgpybunufztysazs");
