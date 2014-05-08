CREATE table CATEGORY
( 
	CATEGORY_ID INTEGER AUTO_INCREMENT,
	CATEGORY_NAME VARCHAR(100) NOT NULL,
	GENERIC_CATEGORY_ID INT,
	GENERIC_CATEGORY_NAME VARCHAR(100),
	PARENT_CATEGORY_ID INTEGER NOT NULL,
	PARENT_CATEGORY_NAME VARCHAR(100) NOT NULL,
	RETAILER_ID VARCHAR(20) NOT NULL,
	URL VARCHAR(500),
	ACTIVE  CHAR(1) NOT NULL,
	PARENT CHAR(1) NOT NULL,
	TIME_MODIFIED DATETIME NOT NULL,
	EXT_ID VARCHAR(100) NULL,

	PRIMARY KEY USING HASH (CATEGORY_ID, RETAILER_ID),
	INDEX GENERIC_CATEGORY_ID_idx using BTREE (GENERIC_CATEGORY_NAME),
	INDEX GENERIC_CATEGORY_ID_idx2 using BTREE (GENERIC_CATEGORY_ID),
	INDEX PARENT_CATEGORY_NAME_idx using BTREE (PARENT_CATEGORY_NAME),
	INDEX PARENT_CATEGORY_ID_idx2 using BTREE (PARENT_CATEGORY_ID),
	INDEX RETAILER_ID_idx using BTREE (RETAILER_ID),
	INDEX URL_idx USING BTREE (URL),
	INDEX TIME_MODIFIED_idx using BTREE (TIME_MODIFIED),
	CONSTRAINT UNIQUE KEY UNIQ_CATEGORY_URL USING BTREE (CATEGORY_NAME, PARENT_CATEGORY_ID, RETAILER_ID)
) ENGINE=MyISAM;
describe CATEGORY;

#ALTER TABLE CATEGORY ADD COLUMN EXT_ID VARCHAR(100) NULL