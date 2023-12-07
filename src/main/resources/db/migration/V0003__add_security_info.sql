ALTER TABLE IF EXISTS users
ADD COLUMN password text;

ALTER TABLE IF EXISTS users
ADD COLUMN active boolean;