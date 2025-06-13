-- liquibase formatted sql

-- changeset konon:174928601703455-24
ALTER TABLE nodes
    ALTER COLUMN comment TYPE VARCHAR(500);