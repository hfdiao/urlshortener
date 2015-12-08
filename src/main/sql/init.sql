set names utf8;
create database urlmapping;
use urlmapping;

CREATE TABLE urlmapping (
    id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    shortenedPath VARCHAR(16) COLLATE latin1_general_cs  NOT NULL,
    reversedURL VARCHAR(1024) COLLATE latin1_general_cs  NOT NULL,
    urlHash BIGINT(20) NOT NULL,
    created BIGINT(20) NOT NULL,
    UNIQUE INDEX idxShortenedPath(shortenedPath),
    INDEX idxURLHash(urlHash)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;