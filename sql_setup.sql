-- create database and table
CREATE DATABASE leaderboard_db;
USE leaderboard_db;

CREATE TABLE players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    score INT NOT NULL
);
