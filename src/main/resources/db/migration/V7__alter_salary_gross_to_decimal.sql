-- V7__alter_salary_gross_to_decimal.sql

ALTER TABLE employee ADD COLUMN IF NOT EXISTS salary_gross_new DECIMAL(10, 2);
UPDATE employee SET salary_gross_new = salary_gross::DECIMAL;
ALTER TABLE employee DROP COLUMN salary_gross;
ALTER TABLE employee RENAME COLUMN salary_gross_new TO salary_gross;