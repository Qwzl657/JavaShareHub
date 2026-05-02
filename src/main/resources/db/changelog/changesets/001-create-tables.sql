
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE shared_files (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              original_name VARCHAR(255) NOT NULL,
                              stored_name VARCHAR(255) NOT NULL,
                              file_type VARCHAR(100),
                              file_size BIGINT,
                              status VARCHAR(20) NOT NULL,
                              category VARCHAR(100),
                              download_count INT DEFAULT 0,
                              uploaded_at TIMESTAMP NOT NULL,
                              user_id BIGINT NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE private_links (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               file_id BIGINT NOT NULL,
                               used BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL,
                               FOREIGN KEY (file_id) REFERENCES shared_files(id)
);