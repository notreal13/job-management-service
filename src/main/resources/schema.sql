--JOB table
DROP TABLE IF EXISTS JOB;

CREATE TABLE JOB (
    ID NUMBER PRIMARY KEY NOT NULL,
    PRIORITY NUMBER NOT NULL,
    JOB_TYPE VARCHAR(255) NOT NULL,
    MESSAGE VARCHAR(1000),
    CREATE_TIME TIMESTAMP NOT NULL,
    UPDATE_TIME TIMESTAMP NOT NULL,
    JOB_STATE VARCHAR(20) NOT NULL
);

ALTER TABLE JOB ADD
   CONSTRAINT JOB_JOB_STATE_CHK CHECK (
            JOB_STATE in ('QUEUED', 'RUNNING', 'SUCCESS', 'FAILED'));

DROP SEQUENCE IF EXISTS JOB_SEQ;

CREATE SEQUENCE JOB_SEQ START WITH 100;

--JOB_SCHEDULE table
DROP TABLE IF EXISTS JOB_SCHEDULE;

CREATE TABLE JOB_SCHEDULE (
    ID NUMBER PRIMARY KEY NOT NULL,
    PRIORITY NUMBER NOT NULL,
    JOB_TYPE VARCHAR(255) NOT NULL,
    CRON_EXPRESSION VARCHAR(255) NOT NULL
);

DROP SEQUENCE IF EXISTS JOB_SCHEDULE_SEQ;

CREATE SEQUENCE JOB_SCHEDULE_SEQ START WITH 100;
