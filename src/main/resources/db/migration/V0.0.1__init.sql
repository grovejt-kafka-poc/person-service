-- drop table person if exists
-- drop sequence if exists hibernate_sequence

create sequence hibernate_sequence start with 1 increment by 1;

create table person (
	id bigint not null, 
	first_name varchar(255), 
	middle_name varchar(255), 
	last_name varchar(255), 
	email varchar(255), 
	birth_date timestamp, 
	primary key (id));
	
