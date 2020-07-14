drop table if exists persons;
CREATE table persons (
	id SERIAL,
	"firstName" varchar(255) NOT NULL,
	"lastName" varchar(255),
	age int NOT NULL,
	weight int,
	PRIMARY KEY (id)
);