CREATE TABLE IF NOT EXISTS USERS(
    id SERIAL NOT NULL ,
    user_name varchar(100) NOT NULL ,
    password varchar(100) NOT NULL,
    registered_date_time timestamp NOT NULL default current_timestamp,
    is_active boolean
);