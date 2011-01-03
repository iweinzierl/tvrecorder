BEGIN;

CREATE TABLE jobs (
    id      INTEGER PRIMARY KEY,
    name    VARCHAR(64),
    start   TIMESTAMP,
    end     TIMESTAMP,
    channel VARCHAR(32)
);

END;
