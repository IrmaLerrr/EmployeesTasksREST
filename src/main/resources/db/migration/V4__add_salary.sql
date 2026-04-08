-- V4__add_salary.sql

ALTER TABLE employee
ADD COLUMN IF NOT EXISTS salary_gross INT;
