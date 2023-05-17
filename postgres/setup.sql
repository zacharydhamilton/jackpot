CREATE SCHEMA games;
SET search_path TO games;

CREATE EXTENSION postgis;

-- # create and populate products data table
CREATE TABLE pools (
	jackpotPool VARCHAR(24) PRIMARY KEY,
	region VARCHAR(6)
);

COPY pools(jackpotPool, region)
FROM '/data/jackpots.csv'
DELIMITER ','
CSV HEADER;