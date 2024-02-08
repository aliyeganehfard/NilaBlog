insert into users (id, username, email, password)
values ('123e4567-e89b-12d3-a456-426614174000', 'admin', 'admin@gmail.com', '$2a$10$AzgCht/dcZhuvbeFtOaZgOGAWQZ//09Nps7HAh4J4KAvAnHbZtlvG');

insert into users (id, username, email, password)
values ('440cd9c2-b215-405d-bc00-8e83ed5cf1dc', 'author', 'author@gmail.com', '$2a$10$ncvnCfGkRjDttaUUvxzcquGAlimuS/61lF3cgP30UpF5EI.XHNO7y');

insert into blog_post (id,title, content, created_at, keywords, category, author_id)
values (10,'Sample Blog Post', 'This is the content of the blog post.', current_timestamp, 'sample, blog, post', 'FOOD', '440cd9c2-b215-405d-bc00-8e83ed5cf1dc');

insert into comments (id,texts, created_at, user_id, post_id)
values (10,'This is a comment', current_timestamp , '123e4567-e89b-12d3-a456-426614174000', 10);