delete from JOB;

insert into JOB(ID, PRIORITY, JOB_TYPE, MESSAGE, CREATE_TIME, UPDATE_TIME, JOB_STATE) values (1, 1, 'EMAIL', null, current_timestamp, current_timestamp, 'QUEUED');

delete from JOB_SCHEDULE;

insert into JOB_SCHEDULE(ID, PRIORITY, JOB_TYPE, CRON_EXPRESSION) values(1, 100, 'INDEXING', '0/55 * * * * ?');