-- V2__create_task_table.sql
CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    author INTEGER NOT NULL,
    assignee INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_task_author FOREIGN KEY (author) REFERENCES employee(id) ON DELETE RESTRICT,
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee) REFERENCES employee(id) ON DELETE SET NULL
);

CREATE INDEX idx_task_author ON task(author);
CREATE INDEX idx_task_assignee ON task(assignee);
CREATE INDEX idx_task_status ON task(status);