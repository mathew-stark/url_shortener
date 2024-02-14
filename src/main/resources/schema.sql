CREATE TABLE IF NOT EXISTS url_mapping (
     id SERIAL PRIMARY KEY,
     index VARCHAR(10),
     url VARCHAR(255),
     date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS index_name ON url_mapping (index);
