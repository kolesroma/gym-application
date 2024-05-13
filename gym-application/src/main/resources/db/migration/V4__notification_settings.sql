CREATE TABLE notification_settings
(
    user_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (user_id)
);
