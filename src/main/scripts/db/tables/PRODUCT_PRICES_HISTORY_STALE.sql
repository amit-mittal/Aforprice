use PRODUCTS;

CREATE table PRODUCT_PRICES_HISTORY_STALE
( 
	PRODUCT_ID INTEGER(8) NOT NULL,	
	TIME DATETIME NOT NULL,	
	PRICE FLOAT NOT NULL,		
	TIME_MODIFIED DATETIME NOT NULL,
	END_TIME DATETIME, 
	RETAILER_ID_NUM int(4) DEFAULT NULL,
	INDEX PRODUCT_ID_idx using BTREE (PRODUCT_ID),
	INDEX TIME_MODIFIED_idx using BTREE ( TIME_MODIFIED ) 
) ENGINE=MYISAM;
describe PRODUCT_PRICES_HISTORY_STALE;

--ALTER TABLE PRODUCT_PRICES_HISTORY_STALE ADD COLUMN RETAILER_ID_NUM int(4) DEFAULT NULL