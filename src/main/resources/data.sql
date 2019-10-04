DROP TABLE IF EXISTS user_dto;

CREATE TABLE user_dto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
  -- TODO: (registration) if the user needs anything else stored for auth purposes, it needs to be here
);

INSERT INTO user_dto (username, password) VALUES
  ('user123', '$2a$10$QhPT1UGYxJ7o1mzImaB2cOV2ZVQ/klU6kOK5zgpnkecrZdMlcz3h.');
  -- the "password" is pretty easy to guess ;)
  -- (don't forget to update this line when you add more columns)