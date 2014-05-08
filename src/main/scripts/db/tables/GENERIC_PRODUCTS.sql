USE PRODUCTS;

--DROP TABLE IF EXISTS PROD_ATTR_MAPPING;
--DROP TABLE IF EXISTS GENERIC_PROD_ATTRS;
--DROP TABLE IF EXISTS GENERIC_PRICE;

--DROP TABLE IF EXISTS GENERIC_PRODUCTS;

CREATE TABLE GENERIC_PRODUCTS (
  PROD_ID_INTERNAL VARCHAR(128) NOT NULL,
  PROD_ID_VENDOR VARCHAR(128) NOT NULL,
  VENDOR VARCHAR(32) NOT NULL,
  TITLE VARCHAR(300) NOT NULL,
  DESCRIPTION VARCHAR(4098) NOT NULL,
  PROD_TYPE VARCHAR(64) DEFAULT NULL,
  MODEL_ID VARCHAR(128) DEFAULT NULL,
  PROD_CATEGORY VARCHAR(64) NOT NULL,
  PROD_URL VARCHAR(512) NOT NULL,
  MOBILE_URL VARCHAR(512) NOT NULL,
  ADD_2_CART_URL VARCHAR(512) NOT NULL,
  IMG_URL VARCHAR(512) NOT NULL,
  ADDL_IMG_URL VARCHAR(512) DEFAULT NULL,
  PROD_CONDITION VARCHAR(32) DEFAULT NULL,
  MANUFACTURER VARCHAR(64) DEFAULT NULL,
  BRAND VARCHAR(64) DEFAULT NULL,
  GTIN VARCHAR(32) DEFAULT NULL,
  MPN VARCHAR(32) DEFAULT NULL,
  GENDER CHAR(1) DEFAULT NULL,
  AGE_GROUP VARCHAR(32) DEFAULT NULL,
  COLOR VARCHAR(16) DEFAULT NULL,
  SIZE VARCHAR(32) DEFAULT NULL,
  MATERIAL VARCHAR(64) DEFAULT NULL,
  PATTERN VARCHAR(32) DEFAULT NULL,
  ITEM_GRP_ID VARCHAR(64) DEFAULT NULL,
  FROM_DATE DATETIME NOT NULL,
  THRU_DATE DATETIME DEFAULT NULL,
  ACTIVE CHAR(8) DEFAULT NULL,
  UPDATE_TIME DATETIME NOT NULL,
  PRIMARY KEY (PROD_ID_INTERNAL) USING BTREE,
  UNIQUE KEY UNIQ_PROD_KEY (PROD_ID_INTERNAL) USING BTREE,
  KEY ID_IDX (PROD_ID_INTERNAL) USING BTREE,
  KEY VENDOR_IDX (VENDOR) USING BTREE,
  KEY TITLE_IDX (TITLE(255)) USING BTREE,
  KEY DESCRIPTION_IDX (DESCRIPTION(255)) USING BTREE,
  KEY UPDATE_TIME_IDX (UPDATE_TIME) USING BTREE,
  KEY THRU_DATE_IDX (THRU_DATE) USING BTREE
) ENGINE=InnoDB;

DESCRIBE GENERIC_PRODUCTS;

CREATE TABLE GENERIC_PROD_ATTRS (
  PROD_ID_INTERNAL VARCHAR(128) NOT NULL,
  ATTR_NAME VARCHAR(64) NOT NULL,
  ATTR_VAL VARCHAR(128) NOT NULL,
  FROM_DATE DATETIME NOT NULL,
  THRU_DATE DATETIME DEFAULT NULL,
  UPDATE_TIME DATETIME NOT NULL,
  BEST_SELLING_RANK VARCHAR(10) DEFAULT NULL, 
  CUSTOMER_REVIEW_AVERAGE VARCHAR(10) DEFAULT NULL, 
  CUSTOMER_REVIEW_COUNT VARCHAR(10) DEFAULT NULL,
  PRIMARY KEY (PROD_ID_INTERNAL) USING BTREE,
  KEY ATTR_NAME (ATTR_NAME) USING BTREE,
  KEY ATTR_VAL (ATTR_VAL) USING BTREE,
  KEY UPDATE_TIME (UPDATE_TIME) USING BTREE,
  CONSTRAINT FOREIGN KEY GENERIC_PROD_ATTRS_FK_1 (PROD_ID_INTERNAL) REFERENCES GENERIC_PRODUCTS (PROD_ID_INTERNAL)
) ENGINE=InnoDB;

DESCRIBE GENERIC_PROD_ATTRS;

CREATE TABLE PROD_ATTR_MAPPING (
  VENDOR_ATTR_NAME VARCHAR(64) NOT NULL,
  ATTR_NAME VARCHAR(64) NOT NULL,
  ATTR_TYPE VARCHAR(32) NOT NULL,
  PRIMARY KEY (VENDOR_ATTR_NAME,ATTR_NAME) USING BTREE,
  KEY VENDOR_ATTR_NAME (VENDOR_ATTR_NAME) USING BTREE,
  KEY ATTR_NAME (ATTR_NAME) USING BTREE,
  KEY ATTR_TYPE (ATTR_TYPE) USING BTREE
) ENGINE=InnoDB;

DESCRIBE PROD_ATTR_MAPPING;



INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'regularPrice','PRICE','PRICE');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'salePrice','SALE_PRICE','PRICE');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'shippingCost','SHIPPING_USD','PRICE');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'shippingWeight','SHIPPING_WEIGHT_LB','PRICE');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'tableName','PRODUCTS.GENERIC_PRICE','PRICE');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'active','ACTIVE','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'addToCartUrl','ADD_2_CART_URL','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'categoryPath.category.name','PROD_CATEGORY','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'condition','PROD_CONDITION','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'image','IMG_URL','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'largeFrontImage','ADDL_IMG_URL','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'manufacturer','BRAND','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'manufacturer','MANUFACTURER','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'mobileUrl','MOBILE_URL','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'modelNumber','MODEL_ID','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'name','TITLE','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'productId','PROD_ID_VENDOR','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'productIdInternal','PROD_ID_INTERNAL','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'shortDescription','DESCRIPTION','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'source','VENDOR','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'tableName','PRODUCTS.GENERIC_PRODUCTS','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'type','PROD_TYPE','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'upc','GTIN','PROD');
INSERT INTO PRODUCTS.PROD_ATTR_MAPPING VALUES( 'url','PROD_URL','PROD');


CREATE TABLE GENERIC_PRICE (
  PROD_ID_INTERNAL VARCHAR(128) NOT NULL,
  PRICE FLOAT NOT NULL,
  SALE_PRICE FLOAT DEFAULT NULL,
  TAX_USD FLOAT DEFAULT NULL,
  SHIPPING_USD FLOAT DEFAULT NULL,
  SHIPPING_WEIGHT_LB FLOAT DEFAULT NULL,
  FROM_DATE DATETIME NOT NULL,
  THRU_DATE DATETIME DEFAULT NULL,
  UPDATE_TIME DATETIME NOT NULL,
  PRIMARY KEY (PROD_ID_INTERNAL,FROM_DATE) USING BTREE,
  KEY PROD_ID_INTERNAL (PROD_ID_INTERNAL) USING BTREE,
  KEY PRICE (PRICE) USING BTREE,
  KEY SALE_PRICE (SALE_PRICE) USING BTREE,
  CONSTRAINT GENERIC_PRICE_IBFK_1 FOREIGN KEY (PROD_ID_INTERNAL) REFERENCES GENERIC_PRODUCTS (PROD_ID_INTERNAL)
) ENGINE=InnoDB;

DESCRIBE GENERIC_PRICE;

