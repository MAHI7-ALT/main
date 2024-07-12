CREATE DATABASE resource;
USE resource;

-- Employees table 
CREATE TABLE employees (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F', 'O')),
    email VARCHAR(80) NOT NULL,
    designation VARCHAR(30) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    reports_to_id INT,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (email),
    FOREIGN KEY (reports_to_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Divisions table 
CREATE TABLE divisions (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    parent_id INT,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (name),
    FOREIGN KEY (parent_id) REFERENCES divisions(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Employee-Divisions relationship table
CREATE TABLE employee_divisions (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    employee_id INT NOT NULL,
    division_id INT NOT NULL,
	primary_division BOOLEAN NOT NULL DEFAULT FALSE,
    can_approve_timesheets BOOLEAN NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (employee_id, division_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (division_id) REFERENCES divisions(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Projects table 
CREATE TABLE projects (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    division_id INT NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (name),
    FOREIGN KEY (division_id) REFERENCES divisions(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Employee-Projects relationship table
CREATE TABLE employee_projects (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    employee_id INT NOT NULL,
    project_id INT NOT NULL,
    can_approve_timesheets BOOLEAN NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Tasks table 
CREATE TABLE tasks (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(200) NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (name),
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Project-Tasks relationship table
CREATE TABLE project_tasks (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    task_id INT NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    UNIQUE KEY (project_id, task_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Timesheet status table 
CREATE TABLE timesheet_status (
    id TINYINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Timesheets table 
CREATE TABLE timesheets (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    employee_division_id INT NOT NULL,
    employee_project_id INT NOT NULL,
    project_task_id INT NOT NULL,
    description VARCHAR(100),
    hours_worked TINYINT NOT NULL,
    submitted_by INT NOT NULL,
    submitted_on DATETIME NOT NULL,
    status TINYINT NOT NULL,
    approved_by INT,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    FOREIGN KEY (employee_division_id) REFERENCES employee_divisions(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_project_id) REFERENCES employee_projects(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (project_task_id) REFERENCES project_tasks(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (submitted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (status) REFERENCES timesheet_status(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Users table 
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    password VARCHAR(12) NOT NULL,
    reset_token VARCHAR(10) NOT NULL,
    reset_token_expires_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by INT,
    modified_on DATETIME,
    deleted_by INT,
    deleted_on DATETIME,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (modified_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (deleted_by) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE SET NULL
);
