USE PRODUCTS
DROP TABLE IF EXISTS TEMP_UNIQ_PRODUCTS;

CREATE table TEMP_UNIQ_PRODUCTS
(
	RETAILER_ID VARCHAR(20),
	CATEGORY_ID INTEGER,
	PRODUCT_NAME VARCHAR(300),	
	COUNT INTEGER,	
	PRIMARY KEY USING HASH (RETAILER_ID, CATEGORY_ID, PRODUCT_NAME)
) ENGINE=InnoDB;
ALTER TABLE TEMP_UNIQ_PRODUCTS ADD INDEX CATEGORY_PRODUCT_IDX USING BTREE(CATEGORY_ID, PRODUCT_NAME);
ALTER TABLE TEMP_UNIQ_PRODUCTS ADD INDEX NEW_PRODUCT_ID_IDX USING BTREE(NEW_PRODUCT_ID);
describe TEMP_UNIQ_PRODUCTS;


CREATE table TEMP_PRODUCT_ID_MAP
(
	OLD_PRODUCT_ID INTEGER(8) UNSIGNED NOT NULL,
	NEW_PRODUCT_ID INTEGER(8) UNSIGNED NOT NULL,
	PRIMARY KEY USING HASH (OLD_PRODUCT_ID)
) ENGINE=InnoDB;
ALTER TABLE TEMP_PRODUCT_ID_MAP ADD INDEX OLD_PRODUCT_ID_IDX USING BTREE(OLD_PRODUCT_ID);
ALTER TABLE TEMP_PRODUCT_ID_MAP ADD INDEX NEW_PRODUCT_ID_IDX USING BTREE(NEW_PRODUCT_ID);
describe TEMP_PRODUCT_ID_MAP;