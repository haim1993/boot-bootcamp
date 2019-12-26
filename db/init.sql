
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    accountNo INT UNSIGNED NOT NULL AUTO_INCREMENT,
    accountName VARCHAR(30) NOT NULL UNIQUE,
    accountToken VARCHAR(40) NOT NULL UNIQUE,
    accountEsIndexName VARCHAR(40) NOT NULL UNIQUE,
    PRIMARY KEY (accountNo)
);

INSERT INTO Account (accountName, accountToken, accountEsIndexName)
VALUES ("root", "root", "root");

INSERT INTO Account (accountName, accountToken, accountEsIndexName)
VALUES ("admin", "admin", "admin");
