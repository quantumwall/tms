ALTER TABLE IF EXISTS users
ADD COLUMN password text NOT NULL;

ALTER TABLE IF EXISTS users
ADD COLUMN active boolean NOT NULL DEFAULT true;

INSERT INTO users(email, name, password, active)
VALUES	('user1@user1.com', 'user1', '$2y$10$pTVmOsxmSz1BHfWErX3iqeueDwILgCOFTvk5tFHJZyShEtxMgvccS', true),
		('user2@user2.com', 'user2', '$2y$10$pTVmOsxmSz1BHfWErX3iqeueDwILgCOFTvk5tFHJZyShEtxMgvccS', true),
		('user3@user3.com', 'user3', '$2y$10$pTVmOsxmSz1BHfWErX3iqeueDwILgCOFTvk5tFHJZyShEtxMgvccS', true);
		
INSERT INTO task(name, description, status, priority, author_id, responsible_id)
VALUES	('task1', 'task1 description', 'IDLE', 'HIGH', 1, 2),
		('task2', 'task2 description', 'IN_PROGRESS', 'LOW', 1, 3),
		('task3', 'task3 description', 'TERMINATED', 'MIDDLE', 2, 1);