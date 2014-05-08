use PRODUCTS
DROP TABLE IF EXISTS PRODUCT_PRICE_HISTORY;
/*
RETAILER_ID is actually not required to be kept in this table,
but is required for figuring out what was the last record updated
in this table for a particular retailer. This is required if the 
file upload fails for a retailer
*/
CREATE table PRODUCT_PRICE_HISTORY
( 
	PRODUCT_ID INTEGER(8) NOT NULL,	
	TIME DATETIME NOT NULL,	
	PRICE FLOAT NOT NULL,
	RETAILER_ID VARCHAR(20) NOT NULL,		
	TIME_MODIFIED DATETIME NOT NULL,
	END_TIME DATETIME, 

	PRIMARY KEY USING BTREE (PRODUCT_ID, TIME),
	CONSTRAINT FOREIGN KEY PRODUCT_FKEY (PRODUCT_ID) REFERENCES PRODUCT (PRODUCT_ID) MATCH FULL
) ENGINE=InnoDB;
describe PRODUCT_PRICE_HISTORY;	