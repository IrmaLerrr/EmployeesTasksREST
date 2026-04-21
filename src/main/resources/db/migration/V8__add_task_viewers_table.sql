--V8__add_task_viewers_table.sql

CREATE TABLE task_viewers (
    task_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, employee_id),

    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

