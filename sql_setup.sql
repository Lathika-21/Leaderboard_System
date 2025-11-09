CREATE DATABASE leaderboarddb;
USE leaderboarddb;

CREATE TABLE players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    score INT
);
