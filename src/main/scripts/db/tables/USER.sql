USE PRODUCTS;
DROP TABLE IF EXISTS USER;

CREATE TABLE USER
(
	USER_ID INTEGER(8) UNSIGNED NOT NULL AUTO_INCREMENT,
	EMAIL_ID VARCHAR(256) NOT NULL,
	NAME VARCHAR(100),
	PASSWORD VARCHAR(64),
	COUNTRY VARCHAR(50),
	PHONE VARCHAR(20),
	LAST_LOGGED_IN DATETIME NOT NULL,
	ACTIVE CHAR(1) NOT NULL,
	NEWSLETTER CHAR(1) NOT NULL,
	REGISTERED CHAR(1) NOT NULL,
	
	PRIMARY KEY USING BTREE (USER_ID),
	INDEX EMAIL_ID_idx using BTREE (EMAIL_ID)
) ENGINE=MYISAM;
describe USER;

#ALTER TABLE USER MODIFY USER_ID INTEGER NOT NULL AUTO_INCREMENT;