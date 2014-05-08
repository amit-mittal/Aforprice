USE PRODUCTS
DROP TABLE IF EXISTS PRODUCT_ERR;
/*
There could be two products in the same category with same product name but different price/url. Url should
always be unique
*/
CREATE table PRODUCT_ERR
( 	
	PRODUCT_ID INTEGER(8) UNSIGNED,
	PRODUCT_NAME VARCHAR(300),
	CATEGORY_ID INTEGER,
	RETAILER_ID VARCHAR(20),		
	GENERIC_PRODUCT_ID VARCHAR(20),
	MODEL_NO VARCHAR(100),		
	DESCRIPTION VARCHAR(5000),
	IMAGE BLOB,
	IMAGE_URL VARCHAR(500),
	START_DATE DATETIME,
	END_DATE DATETIME,
	URL VARCHAR(500),
	ACTIVE CHAR(1),
	TIME_MODIFIED DATETIME	
) ENGINE=InnoDB;
describe PRODUCT_ERR;