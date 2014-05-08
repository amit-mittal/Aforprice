CREATE TABLE POST_PURCHASE_STAT
(
RETAILER_ID  varchar(20) NOT NULL,
STATDATE DATETIME NOT NULL,
CATEGORY_ID int,
TOTAL int ,
DROPPED7DAY int,
DROPPED15DAY int,
DROPPED30DAY int,
DROPPED90DAY int,
DROPAMT double,
ADDENDA1 varchar(20) NULL,
ADDENDA2 varchar(20) NULL,
ADDENDA3 varchar(20) NULL,
KEY  RETAILER_ID_DT_IDX ( RETAILER_ID, STATDATE)  USING BTREE
)

