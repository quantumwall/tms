CREATE TABLE IF NOT EXISTS comment
(
	id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	message text NOT NULL,
	user_id bigint NOT NULL REFERENCES users,
	task_id bigint NOT NULL REFERENCES task
);