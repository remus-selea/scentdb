-- Replace hardcoded database, user and password with value from environment variables
-- https://github.com/remus-selea/scentdb/issues/22

CREATE USER scentdb WITH ENCRYPTED PASSWORD 'scentdb';
CREATE DATABASE scentdb;
GRANT ALL PRIVILEGES ON DATABASE scentdb TO scentdb;