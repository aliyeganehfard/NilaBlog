CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_pictures BYTEA
);

CREATE TABLE IF NOT EXISTS blog_post (
    id serial PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    keywords VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    author_id UUID NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS "comment" (
    id serial PRIMARY KEY,
    "text" VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES blog_post (id)
);