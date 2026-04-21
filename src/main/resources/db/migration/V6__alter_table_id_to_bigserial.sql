--V6__alter_table_id_to_bigserial.sql

ALTER TABLE employee ALTER COLUMN id DROP DEFAULT;
ALTER TABLE employee ALTER COLUMN id TYPE BIGINT;

DROP SEQUENCE IF EXISTS employee_id_seq;
CREATE SEQUENCE employee_id_seq AS BIGINT START 1 OWNED BY employee.id;
SELECT setval('employee_id_seq', COALESCE((SELECT MAX(id) FROM employee), 0));

ALTER TABLE employee ALTER COLUMN id SET DEFAULT nextval('employee_id_seq');


ALTER TABLE task ALTER COLUMN author TYPE BIGINT;
ALTER TABLE task ALTER COLUMN assignee TYPE BIGINT;

ALTER TABLE task ALTER COLUMN id DROP DEFAULT;
ALTER TABLE task ALTER COLUMN id TYPE BIGINT;

DROP SEQUENCE IF EXISTS task_id_seq;
CREATE SEQUENCE task_id_seq AS BIGINT START 1 OWNED BY task.id;
SELECT setval('task_id_seq', COALESCE((SELECT MAX(id) FROM task), 0));

ALTER TABLE task ALTER COLUMN id SET DEFAULT nextval('task_id_seq');
