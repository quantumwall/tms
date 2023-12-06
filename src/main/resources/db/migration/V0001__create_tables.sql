create table if not exists users 
(
	id bigint generated by default as identity primary key,
	email varchar(64) not null unique,
	name varchar(64)
);

create table if not exists task
(
	id bigint generated by default as identity primary key,
	name varchar(128) not null,
	description text,
	status varchar(16) not null,
	priority varchar(16) not null,
	author_id bigint not null references users,
	responsible_id bigint not null references users
);

INSERT INTO users(email, name)
VALUES	('user1@user1.com', 'user1'),
		('user2@user2.com', 'user2'),
		('user3@user3.com', 'user3');
		
INSERT INTO task(name, description, status, priority, author_id, responsible_id)
VALUES	('task1', 'task1 description', 'IDLE', 'HIGH', 1, 2),
		('task2', 'task2 description', 'IN_PROGRESS', 'LOW', 1, 3),
		('task3', 'task3 description', 'TERMINATED', 'MIDDLE', 2, 1);	