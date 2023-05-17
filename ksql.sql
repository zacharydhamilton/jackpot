CREATE STREAM wagers (
    jackpotPoolId STRING,
    wager INT
) WITH (
    KAFKA_TOPIC='wagers',
    VALUE_FORMAT='AVRO'
);

CREATE STREAM pool_changes (
    pk STRUCT<jackpotPool VARCHAR> KEY,
    jackpotPool VARCHAR, 
    region VARCHAR
) WITH (
    KAFKA_TOPIC='jackpot.games.pools',
    KEY_FORMAT='JSON',
    VALUE_FORMAT='AVRO'
);

CREATE TABLE jackpotPools WITH (
        KAFKA_TOPIC='pools',
        KEY_FORMAT='KAFKA',
        VALUE_FORMAT='AVRO'
    ) AS SELECT
        jackpotpool pool,
        LATEST_BY_OFFSET(region) AS region
    FROM pool_changes
    GROUP BY jackpotpool
EMIT CHANGES;

CREATE TABLE jackpots WITH (
        KAFKA_TOPIC='jackpots',
        KEY_FORMAT='KAFKA',
        VALUE_FORMAT='AVRO'
    ) AS SELECT 
        p.pool AS jackpotPoolId,
        LATEST_BY_OFFSET(p.region) region,
        SUM(w.wager) AS jackpot
    FROM wagers w
        JOIN jackpotPools p ON p.pool = w.jackpotPoolId
    GROUP BY p.pool
EMIT CHANGES;

INSERT INTO wagers (jackpotPoolId, wager) VALUES ('4a41a567', 0);
INSERT INTO wagers (jackpotPoolId, wager) VALUES ('26531c2c', 0);
INSERT INTO wagers (jackpotPoolId, wager) VALUES ('9876a556', 0);
INSERT INTO wagers (jackpotPoolId, wager) VALUES ('8d4c0a19', 0);
INSERT INTO wagers (jackpotPoolId, wager) VALUES ('fd5c17a6', 0);
INSERT INTO wagers (jackpotPoolId, wager) VALUES ('5b535f6c', 0);