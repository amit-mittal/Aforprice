USE PRODUCTS
DROP TABLE IF EXISTS PRODUCT_SUMMARY_LIST_PAGE;
/*
The table stores the html page information for product summary page
*/
CREATE table PRODUCT_SUMMARY_LIST_PAGE
( 	
	ID INTEGER AUTO_INCREMENT,
	URL VARCHAR(600) NOT NULL,			
	DOWNLOAD_PATH VARCHAR(200) NOT NULL,	
	PRIMARY KEY USING HASH (ID)
) ENGINE=InnoDB;
describe PRODUCT_SUMMARY_LIST_PAGE;

 alter table PRODUCT_SUMMARY_LIST_PAGE modify URL VARCHAR(600) not null;
alter table PRODUCT_DOWNLOAD modify URL VARCHAR(600);