-- V5__update_author_can_be_null.sql

ALTER TABLE task ALTER COLUMN author DROP NOT NULL;

ALTER TABLE task DROP CONSTRAINT fk_task_author;

ALTER TABLE task ADD CONSTRAINT fk_task_author FOREIGN KEY (author) REFERENCES employee(id) ON DELETE SET NULL;
