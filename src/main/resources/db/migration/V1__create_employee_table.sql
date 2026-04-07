-- V1__create_employee_table.sql
CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    phoneNumber VARCHAR(50),
    position VARCHAR(50) NOT NULL
);
