create database if not exists st4_db;
drop table if exists st4_db.subscriptions;
drop table if exists st4_db.users;
drop table if exists st4_db.roles;
drop table if exists st4_db.periodicals;
drop table if exists st4_db.themes;
drop table if exists st4_db.periods;

CREATE TABLE IF NOT EXISTS st4_db.roles (
    role_id TINYINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS st4_db.users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100),
    blocked BOOLEAN NOT NULL DEFAULT true,
    role_id TINYINT NOT NULL default 1,
    balance DOUBLE(7 , 2 ) DEFAULT 0,
    FOREIGN KEY (role_id)
        REFERENCES roles (role_id),
    token_hash VARCHAR(40),
    lang VARCHAR(5) DEFAULT 'en',
    email varchar(256) UNIQUE
    , register_timestamp BIGINT
    , salt varchar(40)
    , fb_id bigint
    , fb_only boolean DEFAULT false NOT NULL
);

CREATE TABLE IF NOT EXISTS themes (
    theme_id SMALLINT AUTO_INCREMENT PRIMARY KEY,
    theme_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS periods (
    period_id SMALLINT AUTO_INCREMENT PRIMARY KEY,
    period_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS periodicals (
    periodical_id INT(6) AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DOUBLE(7 , 2 ) NOT NULL,
    theme_id SMALLINT NOT NULL,
    FOREIGN KEY (theme_id)
        REFERENCES themes (theme_id),
    period_id SMALLINT NOT NULL,
    FOREIGN KEY (period_id)
        REFERENCES periods (period_id)
	, is_active boolean default true
);

CREATE TABLE IF NOT EXISTS subscriptions (
    subscription_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    periodical_id INT,
    FOREIGN KEY (periodical_id)
        REFERENCES periodicals (periodical_id),
    user_id BIGINT,
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
	, subscription_date timestamp
);

create or replace view periodical_entities as select periodicals.*, theme_name, period_name from periodicals
join themes using(theme_id)
join periods using(period_id);

create or replace view users_periodicals as select periodical_entities.*, user_id 
from subscriptions join users using (user_id)
join periodical_entities using (periodical_id);

create or replace view available_periodicals as select * from periodical_entities where is_active=true;

insert into periods values(default, "weekly");
insert into periods values(default, "monthly");
insert into periods values(default, "quarterly");
insert into periods values(default, "yearly"); 

insert into themes (theme_name) values("biology");
insert into themes (theme_name) values("geography");
insert into themes (theme_name) values("history");  

insert into periodicals (title, price, theme_id, period_id) values("Nature", 200, 2, 1);
insert into periodicals (title, price, theme_id, period_id) values("National Geographic", 500.5, 2, 2);
insert into periodicals (title, price, theme_id, period_id) values("U.S. History", 1000, 3, 3);

insert into roles (role_name) values('client');
insert into roles (role_name) values('admin');

insert into users (login,password, blocked, email) values("lera", sha1("lera"), false, "lera@lera.com");
insert into users (login,password, blocked, email, role_id) values("admin", sha1("admin"), false, "admin@admin.com", 2);
