CREATE DATABASE STREAM_DB;
USE STREAM_DB;
CREATE TABLE user_details (
    id BIGINT PRIMARY KEY,
    authority VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    gmail VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE episodes (
    id BIGINT PRIMARY KEY,
	unique_poster_id VARCHAR(255) NOT NULL,
    belongs_to_movie BIGINT,
    belongs_to_series BIGINT
);
CREATE TABLE user_movie_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL
);
CREATE TABLE user_series_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL
);
CREATE TABLE media_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unique_id VARCHAR(255) NOT NULL UNIQUE,
	file_path VARCHAR(255) NOT NULL UNIQUE
);