package db;

public class Queries {
	public static final String SELECT_CATIDS_TO_PROC = "SELECT CATEGORY_ID "
			+ "FROM PRODUCTS.CATEGORY "
			+ "WHERE PARENT = 'N' AND ACTIVE = '1' AND CATEGORY_ID NOT IN "
			+ "(SELECT TASK_ID "
			+ " FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND TASK_NAME= ?) ORDER BY CATEGORY_ID DESC LIMIT 0,100";

	public static final String SELECT_CATIDS_TO_PROC_RET_OVERRIDE = "SELECT CATEGORY_ID "
			+ "FROM PRODUCTS.CATEGORY "
			+ "WHERE RETAILER_ID = ? AND PARENT = 'N' AND ACTIVE = '1' AND CATEGORY_ID NOT IN "
			+ "(SELECT TASK_ID "
			+ " FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND TASK_NAME= ?) ORDER BY CATEGORY_ID DESC LIMIT 0,100";

	public static final String SELECT_ERR_CATIDS_TO_PROC = "SELECT TASK_ID FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND ATTEMPT = ? AND TASK_NAME= ? AND ERR_TIME IS NOT NULL "
			+ "AND TASK_ID NOT IN (SELECT TASK_ID FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND ATTEMPT >= ? AND TASK_NAME= ?) LIMIT 0,100";

	public static final String SELECT_ERR_CATIDS_TO_PROC_RET_OVERRIDE = "SELECT TASK_ID FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND ATTEMPT = ? AND TASK_NAME= ? AND ERR_TIME IS NOT NULL "
			+ "AND TASK_ID NOT IN (SELECT TASK_ID FROM PRODUCTS.DAILY_TASKS_LOCK WHERE DATE=? AND ATTEMPT >= ? AND TASK_NAME= ?) LIMIT 0,100";

	public static final String INSERT_TASKS_TO_LOCK = "INSERT INTO PRODUCTS.DAILY_TASKS_LOCK (TASK_NAME, DATE, TASK_ID, LOCK_TIME, ATTEMPT )"
			+ "VALUES (?, ?, ?, ?, ?)";
	public static final String UPDATE_START_TIME = "UPDATE PRODUCTS.DAILY_TASKS_LOCK SET START_TIME=? WHERE TASK_NAME=? AND DATE=? AND TASK_ID=? AND ATTEMPT=?";
	public static final String UPDATE_END_TIME = "UPDATE PRODUCTS.DAILY_TASKS_LOCK SET END_TIME=? WHERE TASK_NAME=? AND DATE=? AND TASK_ID=? AND ATTEMPT=?";
	public static final String UPDATE_ERR_TIME = "UPDATE PRODUCTS.DAILY_TASKS_LOCK SET ERR_TIME=? WHERE TASK_NAME=? AND DATE=? AND TASK_ID=? AND ATTEMPT=?";

	public static final String SELECT_CATEGORY = "SELECT CATEGORY_ID, RETAILER_ID, CATEGORY_NAME, URL FROM PRODUCTS.CATEGORY WHERE CATEGORY_ID=?";
	public static final String SELECT_CATEGORY_ALL_ATTR = "SELECT CATEGORY_ID, CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, RETAILER_ID, "
			+ "URL, ACTIVE, PARENT, TIME_MODIFIED FROM PRODUCTS.CATEGORY WHERE CATEGORY_ID=?";
	public static String SELECT_ACTIVE_CATEGORIES_FOR_RETAILER = "SELECT CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "URL, ACTIVE, PARENT FROM PRODUCTS.CATEGORY WHERE RETAILER_ID=? AND ACTIVE = '1'";
	public static String SELECT_ACTIVE_CATEGORIES_FOR_RETAILER_COUNT = "SELECT COUNT(*) FROM PRODUCTS.CATEGORY WHERE RETAILER_ID=? AND ACTIVE = '1'";
	public static String SELECT_ALL_CATEGORIES_FOR_RETAILER = "SELECT CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "URL, ACTIVE, PARENT, EXT_ID FROM PRODUCTS.CATEGORY WHERE RETAILER_ID=? AND ACTIVE='1'";

	public static String SELECT_CATEGORIES_SUMMARY = "SELECT RETAILER_ID, CATEGORY_ID, PARENT, ACTIVE FROM PRODUCTS.CATEGORY ORDER BY RETAILER_ID, CATEGORY_ID ASC";
	public static String SELECT_CHILD_CATEGORIES = "SELECT CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "URL, ACTIVE, PARENT FROM PRODUCTS.CATEGORY WHERE RETAILER_ID=? AND PARENT_CATEGORY_NAME = ? AND ACTIVE = '1'";

	public static String SELECT_CHILD_CATEGORIES_CAT_ID = "SELECT CATEGORY_ID, CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, RETAILER_ID, "
			+ "URL, ACTIVE, PARENT, TIME_MODIFIED FROM PRODUCTS.CATEGORY WHERE PARENT_CATEGORY_ID=? AND ACTIVE = '1'";
	public static String SELECT_CHILD_CATEGORIES_RETAILER = "SELECT CATEGORY_ID, CATEGORY_NAME, GENERIC_CATEGORY_ID, GENERIC_CATEGORY_NAME, "
			+ "PARENT_CATEGORY_ID, PARENT_CATEGORY_NAME, RETAILER_ID, "
			+ "URL, ACTIVE, PARENT, TIME_MODIFIED FROM PRODUCTS.CATEGORY WHERE RETAILER_ID = ? AND PARENT_CATEGORY_ID = 0 AND ACTIVE = '1'";
	public static String UPDATE_CATEGORY_INACTIVE = "UPDATE CATEGORY SET ACTIVE = '0' WHERE CATEGORY_ID=?";

	public static final String RECORD_PRODUCT = "{call PRODUCTS.UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";

	public static final String PRODUCT_GET = "SELECT PRODUCT_ID, TIME_MODIFIED FROM PRODUCTS.PRODUCT WHERE CATEGORY_ID=? AND RETAILER_ID=? AND PRODUCT_NAME=?";

	public static final String PRODS_COUNT_BY_CATEGORY = "SELECT COUNT(*) AS COUNT FROM PRODUCTS.PRODUCT WHERE CATEGORY_ID=? AND RETAILER_ID=?";

	/*public static final String LAST_PRICE = "SELECT PRICE FROM PRODUCTS.PRODUCT_PRICE_HISTORY "
			+ "WHERE PRODUCT_ID=? AND TIME = (SELECT MAX(TIME) FROM PRODUCTS.PRODUCT_PRICE_HISTORY WHERE PRODUCT_ID=?)";*/

	public static final String INSERT_DOWN_PRODUCTS = "INSERT INTO PRODUCT_DOWNLOAD (RETAILER_ID, CATEGORY_ID, PRODUCT_NAME, DOWNLOAD_TIME, MODEL_NO, DESCRIPTION, IMAGE_URL, "
			+ "URL, PRICE, PROCESSED, MODE, REVIEW_RATING, NUM_REVIEWS, BEST_SELLER_RANK) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	public static final String MAX_DOWNLOAD_ID = "SELECT MAX(ID) FROM PRODUCT_DOWNLOAD";

	public static final String MIN_DOWNLOAD_ID = "SELECT MIN(ID) FROM PRODUCT_DOWNLOAD";

	public static final String INSERT_CATEGORY_ERR = "INSERT INTO CATEGORY_ERR (TIME_ERR, CATEGORY_ID) "
			+ "VALUES ( ?, ?)";

	public static final String UPDATE_PRODUCT = "UPDATE PRODUCT set MODEL_NO=?, DESCRIPTION=?, IMAGE_URL=?, TIME_MODIFIED=? WHERE PRODUCT_ID=?";
	public static final String UPDATE_PRODUCT_TS = "UPDATE PRODUCT set TIME_MODIFIED=? WHERE PRODUCT_ID=?";

	public static final String MAX_PRODUCT_ID = "SELECT MAX(PRODUCT_ID) FROM PRODUCT_SUMMARY";

	public static final String INSERT_DAILY_REC = "INSERT INTO DAILY_REC (ID, NAME, RUN_DATE, TYPE, TEXT, COUNT, TIME_MODIFIED) VALUES ( ?, ?, ?, ?, ?, ?, ?)";

	public static final String INSERT_CAT_PARSE_ERR = "INSERT INTO CATEGORY_PARSE_ERR (RETAILER_ID, CATEGORY_ID, CATEGORY_NAME, RECORD_TIME, TYPE, TEXT) VALUES (?, ?, ?, ?, ?, ?)";

	public static final String INSERT_PRODUCT = "INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, CATEGORY_ID, GENERIC_PRODUCT_ID, RETAILER_ID, MODEL_NO, DESCRIPTION, IMAGE_URL, START_DATE, URL, ACTIVE, TIME_MODIFIED) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	public static final String INSERT_CATEGORY_REC = "INSERT INTO CATEGORY_REC (RETAILER_ID, RUN_DATE, TYPE, TEXT, COUNT, TIME_MODIFIED) VALUES ( ?, ?, ?, ?, ?, ?)";

	public static final String INSERT_CATEGORY_HISTORY = "INSERT INTO CATEGORY_HISTORY SELECT CATEGORY_ID, CATEGORY_NAME, GENERIC_CATEGORY_ID, PARENT_CATEGORY_ID, RETAILER_ID, CATEGORY_FETCH_RULE_ID, URL, ACTIVE, PARENT, TIME_MODIFIED "
			+ "from CATEGORY " + "where CATEGORY_ID=?";
	public static final String DISABLE_CATEGORY = "UPDATE CATEGORY SET ACTIVE = '0', TIME_MODIFIED = ? WHERE CATEGORY_ID=?";
	public static final String GET_DOWNLOADED_PRODS = "SELECT * from PRODUCTS.PRODUCT_DOWNLOAD where RETAILER_ID=? and CATEGORY_ID=? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME <= ? AND MODE=? order by DOWNLOAD_TIME";
	public static final String GET_DOWNLOADED_PRODS_COUNT = "SELECT COUNT(*) from PRODUCTS.PRODUCT_DOWNLOAD where RETAILER_ID=? and CATEGORY_ID=? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME <= ? AND MODE=?";
	public static final String GET_DOWNLOADED_PRODS_FOR_RETAILER = "SELECT * from PRODUCTS.PRODUCT_DOWNLOAD where RETAILER_ID=? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME <= ? AND MODE=? order by DOWNLOAD_TIME";
	public static final String GET_DOWNLOADED_PRODS_FOR_RETAILER_COUNT = "SELECT COUNT(*) from PRODUCTS.PRODUCT_DOWNLOAD where RETAILER_ID=? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME <= ? AND MODE=?";
	public static final String GET_DOWNLOADED_PRODS_TO_PROCESS = "SELECT ID, RETAILER_ID, CATEGORY_ID, PRODUCT_NAME, DOWNLOAD_TIME, MODEL_NO, "
			+ "IMAGE_URL, URL, PRICE, REVIEW_RATING, NUM_REVIEWS, "
			+ "BEST_SELLER_RANK from PRODUCTS.PRODUCT_DOWNLOAD "
			+ "WHERE ID >=? AND ID <= ? AND PROCESSED NOT IN ('F', 'U', 'M') order by DOWNLOAD_TIME";
	public static final String MARK_PROCESSED = "UPDATE PRODUCTS.PRODUCT_DOWNLOAD SET PROCESSED = ? WHERE ID=?";
	public static final String GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER = "SELECT ID, RETAILER_ID, CATEGORY_ID, PRODUCT_NAME, PRICE, URL, IMAGE_URL, MODEL_NO, REVIEW_RATING, NUM_REVIEWS, BEST_SELLER_RANK, DOWNLOAD_TIME, "
			+ "FROM PRODUCTS.PRODUCT_DOWNLOAD WHERE ID >= ? AND ID <= ? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME < ?";
	public static final String GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER_AVLBL_INFO = "SELECT ID, RETAILER_ID, CATEGORY_ID, PRODUCT_NAME, PRICE, URL, REVIEW_RATING, NUM_REVIEWS, BEST_SELLER_RANK, DOWNLOAD_TIME, MODEL_NO, "
			+ "IF(LENGTH(IMAGE_URL)>0,\"I\",\"\") AS IMAGE_URL "
			+ "FROM PRODUCTS.PRODUCT_DOWNLOAD WHERE ID >= ? AND ID <= ? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME < ?";
	public static final String GET_MAX_ID_TIMERANGE = "SELECT MAX(ID) FROM PRODUCTS.PRODUCT_DOWNLOAD  USE INDEX (DOWNLOAD_TIME_idx) WHERE DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME < ?";
	public static final String GET_MIN_ID_TIMERANGE = "SELECT MIN(ID) FROM PRODUCTS.PRODUCT_DOWNLOAD  USE INDEX (DOWNLOAD_TIME_idx) WHERE DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME < ?";

	public static final String GET_ALL_DOWNLOADED_PRODS_FOR_RETAILER_COUNT = "SELECT COUNT(*) from PRODUCTS.PRODUCT_DOWNLOAD use index (DOWNLOAD_TIME_idx) WHERE DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME < ?";

	public static final String GET_DOWNLOADED_PRODS_FOR_RETAILER_100 = "SELECT * from PRODUCTS.PRODUCT_DOWNLOAD where RETAILER_ID=? AND DOWNLOAD_TIME >= ? AND DOWNLOAD_TIME <= ? order by DOWNLOAD_TIME LIMIT 100";

	public static final String UPDATE_PROD_REC_STATUS = "UPDATE PRODUCTS.PRODUCT_DOWNLOAD SET RECON_SUCCESS=?, RECON_TEXT=? WHERE ID=?";
	public static final String GET_PROD_ATTR_MAPPING = "SELECT * FROM PROD_ATTR_MAPPING";
	public static final String GET_GENERIC_PRODUCTS = "SELECT * FROM PRODUCTS.GENERIC_PRODUCTS WHERE THRU_DATE IS NULL";
	public static final String GET_GENERIC_PRODUCTS_SELECTIVE = "SELECT * FROM PRODUCTS.GENERIC_PRODUCTS WHERE THRU_DATE IS NULL AND PROD_ID_INTERNAL ";
	public static final String GET_GENERIC_PRICES = "SELECT * FROM PRODUCTS.GENERIC_PRICE WHERE THRU_DATE IS NULL";
	public static final String GET_GENERIC_PRICES_SELECTIVE = "SELECT * FROM PRODUCTS.GENERIC_PRICE WHERE THRU_DATE IS NULL AND PROD_ID_INTERNAL ";
	public static final String GET_GENERIC_PRODS_DATA_TYPES = "SELECT * FROM PRODUCTS.GENERIC_PRICE PR, PRODUCTS.GENERIC_PRODUCTS PD WHERE 1=2";

	public static final String GET_PRODUCT = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY WHERE PRODUCT_NAME=?";
	public static final String GET_PRODUCT_BY_PRODUCT_ID = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY WHERE PRODUCT_ID=?";
	public static final String GET_PRODUCT_BY_UID_AND_RETAILER = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY WHERE UID=? AND RETAILER_ID=?";
	// PRODUCT_SUMMARY TABLE SHOULD ONLY HAVE ACTIVE PRODUCTS, TODO: this is not true anymore, what needs to be done to fix it?
	public static final String GET_ALL_ACTIVE_PRODUCTS = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY WHERE PRODUCT_ID > ? AND PRODUCT_ID <= ? and ACTIVE = '1'";
	public static final String GET_ALL_ACTIVE_PRODUCTS_COUNT = "SELECT COUNT(*) FROM PRODUCTS.PRODUCT_SUMMARY where ACTIVE = '1'";
	public static final String GET_PRODUCT_SUMMARY_MAX_ID = "SELECT MAX(PRODUCT_ID) FROM PRODUCTS.PRODUCT_SUMMARY";
	public static final String GET_ALL_PRODS_AVLBL_INFO = "SELECT PRODUCT_ID, RETAILER_ID, PRODUCT_NAME, URL, PRICE, MODEL_NO, REVIEW_RATING, NUM_REVIEWS, BEST_SELLER_RANK, START_DATE, LAST_DOWNLOAD_TIME, "
			+ "IMAGE_URL, DESCRIPTION, ACTIVE FROM PRODUCTS.PRODUCT_SUMMARY WHERE PRODUCT_ID > ? AND PRODUCT_ID <= ?";
	public static final String GET_ALL_ACTIVE_PRODS_AVLBL_INFO = GET_ALL_PRODS_AVLBL_INFO + " AND ACTIVE = '1'";
	public static final String GET_PRODUCTS_FOR_RETAILER = "select P.PRODUCT_ID, PRODUCT_NAME, MODEL_NO, IMAGE_URL, URL from PRODUCT_SUMMARY P"
			+ " where P.RETAILER_ID = ? and P.PRODUCT_ID >=? and P.PRODUCT_ID<? and P.ACTIVE = '1'";
	public static final String GET_ALL_PRODUCTS_FOR_RETAILER = "select P.PRODUCT_ID, PRODUCT_NAME, MODEL_NO, IMAGE_URL, URL from PRODUCT_SUMMARY P"
			+ " where P.RETAILER_ID = ? and P.START_DATE < ? and P.PRODUCT_ID >=? and P.PRODUCT_ID<? ";
	public static final String GET_PRODUCT_IDS_FOR_CATEGORY = "select PRODUCT_ID from PRODUCT_CATEGORY where CATEGORY_ID = ?";
	public static final String GET_PRODUCTS_FOR_CATEGORY = "select P.PRODUCT_ID, PRODUCT_NAME, MODEL_NO, IMAGE_URL, URL from PRODUCT_SUMMARY P, PRODUCT_CATEGORY C"
			+ " where C.CATEGORY_ID = ? and P.PRODUCT_ID = C.PRODUCT_ID and P.ACTIVE = '1'";
	public static final String GET_PRODUCTS_FOR_CATEGORY_COUNT = "select COUNT(*) from PRODUCT_SUMMARY P, PRODUCT_CATEGORY C"
			+ " where C.CATEGORY_ID = ? and P.PRODUCT_ID = C.PRODUCT_ID and P.ACTIVE = '1'";

	public static final String MAX_PROD_ID = "SELECT MAX(PRODUCT_ID) FROM PRODUCTS.PRODUCT_SUMMARY";

	public static final String GET_PRODUCT_CATEGORIES = "SELECT CATEGORY_ID FROM PRODUCTS.PRODUCT_CATEGORY WHERE PRODUCT_ID=?";
	public static final String GET_PRODUCT_CATEGORIES_ALL = "SELECT PRODUCT_ID, CATEGORY_ID FROM PRODUCTS.PRODUCT_CATEGORY";
	public static final String GET_PRODUCT_PRICES_HISTORY = "SELECT PRICE, TIME, PRODUCT_ID, TIME_MODIFIED FROM %s WHERE PRODUCT_ID=?";

	public static final String INSERT_PRODUCT_SUMM = "INSERT INTO PRODUCTS.PRODUCT_SUMMARY (PRODUCT_ID, UID, RETAILER_ID, PRODUCT_NAME, PRICE, REVIEW_RATING, NUM_REVIEWS, BEST_SELLER_RANK, MODEL_NO, IMAGE_URL, START_DATE, URL, ACTIVE, LAST_DOWNLOAD_TIME, TIME_MODIFIED) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?)";
	public static final String REMOVE_PRODUCT_PRICES_HISTORY = "DELETE P FROM %s P, PRODUCT_SUMMARY C  WHERE "
			+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	public static final String REMOVE_PRODUCT_REVIEWS_HISTORY = "DELETE P FROM %s P, PRODUCT_SUMMARY C WHERE "
			+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	//public static final String REMOVE_PRODUCT_SELL_RANKS_HISTORY = "DELETE P FROM PRODUCT_SELL_RANKS_HISTORY P, PRODUCT_SUMMARY C WHERE "
		//	+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	public static final String REMOVE_PRODUCT_CATEGORY = "DELETE C FROM PRODUCT_CATEGORY C, PRODUCT_SUMMARY P WHERE P.RETAILER_ID=? AND C.PRODUCT_ID=P.PRODUCT_ID";
	public static final String REMOVE_CATEGORY = "DELETE FROM CATEGORY WHERE RETAILER_ID = ?";
	public static final String REMOVE_PRODUCT_SUMM = "DELETE FROM PRODUCTS.PRODUCT_SUMMARY WHERE RETAILER_ID=?";

	public static final String REMOVE_PRODUCT_PRICES_HISTORY_STALE = "DELETE P FROM PRODUCT_PRICES_HISTORY_STALE P, PRODUCT_SUMMARY_STALE C  WHERE "
			+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	public static final String REMOVE_PRODUCT_REVIEWS_HISTORY_STALE = "DELETE P FROM PRODUCT_REVIEWS_HISTORY_STALE P, PRODUCT_SUMMARY_STALE C WHERE "
			+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	public static final String REMOVE_PRODUCT_SELL_RANKS_HISTORY_STALE = "DELETE P FROM PRODUCT_SELL_RANKS_HISTORY_STALE P, PRODUCT_SUMMARY_STALE C WHERE "
			+ "C.RETAILER_ID = ? AND P.PRODUCT_ID = C.PRODUCT_ID";
	public static final String REMOVE_PRODUCT_SUMMARY_STALE = "DELETE FROM PRODUCTS.PRODUCT_SUMMARY_STALE WHERE RETAILER_ID=?";

	public static final String UPDATE_PRODUCT_SUMM_DYN_ATTR = "UPDATE PRODUCTS.PRODUCT_SUMMARY SET PRODUCT_NAME = ?, URL=?, PRICE=?, REVIEW_RATING = ?, NUM_REVIEWS = ?, BEST_SELLER_RANK = ?, MODEL_NO=?, IMAGE_URL=?, LAST_DOWNLOAD_TIME=?, TIME_MODIFIED=? WHERE PRODUCT_ID=?";
	public static final String INSERT_PRODUCT_CATEGORY = "REPLACE INTO PRODUCTS.PRODUCT_CATEGORY VALUES(?, ?, ?)";
	public static final String INSERT_CAT_PARENT_MAP = "REPLACE INTO PRODUCTS.CHILD_ALLPARENT_CAT_MAP VALUES(?, ?, ?)";
	public static final String SELECT_CAT_PARENT_MAP = "SELECT CATEGORY_ID, PARENT_CATEGORY_ID FROM PRODUCTS.CHILD_ALLPARENT_CAT_MAP";

	public static final String INSERT_PRODUCT_PRICES_HISTORY = "INSERT INTO %s (PRODUCT_ID, TIME, PRICE, TIME_MODIFIED) "
			+ "VALUES (?, ?, ?, ?)";
	// public static final String LAST_PRODUCT_PRICES_HISTORY_BEFORE =
	// "SELECT PRODUCT_ID, TIME, PRICE, END_TIME, TIME_MODIFIED FROM PRODUCTS.PRODUCT_PRICES_HISTORY "
	// + "WHERE PRODUCT_ID=? AND TIME <= ? ORDER BY TIME DESC LIMIT 1";
	public static final String INSERT_PRODUCT_REVIEWS_HISTORY = "INSERT INTO %s (PRODUCT_ID, TIME, RATING, NUM_REVIEWS, TIME_MODIFIED) "
			+ "VALUES(?, ?, ?, ?, ?)";
	// public static final String LAST_PRODUCT_REVIEWS_HISTORY_BEFORE =
	// "SELECT PRODUCT_ID, TIME, RATING, NUM_REVIEWS, TIME_MODIFIED, END_TIME FROM PRODUCTS.PRODUCT_REVIEWS_HISTORY WHERE PRODUCT_ID=? AND TIME <= ? ORDER BY TIME DESC LIMIT 1";

	public static final String INSERT_PRODUCT_SELL_RANKS_HISTORY = "INSERT INTO PRODUCTS.PRODUCT_SELL_RANKS_HISTORY (PRODUCT_ID, TIME, RANK, TIME_MODIFIED) "
			+ "VALUES(?, ?, ?, ?)";
	// public static final String LAST_PRODUCT_SELL_RANKS_HISTORY_BEFORE =
	// "SELECT PRODUCT_ID, TIME, RANK, TIME_MODIFIED, END_TIME FROM PRODUCTS.PRODUCT_SELL_RANKS_HISTORY WHERE PRODUCT_ID=? AND TIME <= ? ORDER BY TIME DESC LIMIT 1";
	public static final String PRODUCT_SELL_RANKS_HISTORY_QUERY = "SELECT PRODUCT_ID, TIME, RANK, TIME_MODIFIED, END_TIME FROM %s WHERE PRODUCT_ID=?";
	public static final String PRODUCT_REVIEWS_HISTORY_QUERY = "SELECT PRODUCT_ID, TIME, RATING, NUM_REVIEWS, TIME_MODIFIED FROM %s WHERE PRODUCT_ID=?";
	public static final String PRODUCT_PRICES_HISTORY_QUERY = "SELECT PRODUCT_ID, TIME, PRICE, TIME_MODIFIED FROM %s WHERE PRODUCT_ID=?";

	public static final String INSERT_PRODUCT_SUMMARY_LIST_PAGE = "INSERT INTO PRODUCT_SUMMARY_LIST_PAGE (URL, DOWNLOAD_PATH) VALUES(?, ?)";
	public static final String INSERT_PRODUCT_SUMMARY_PAGE = "INSERT INTO PRODUCT_SUMMARY_PAGE (PRODUCT_NAME, PRODUCT_SUMMARY_PAGE_ID, DOWNLOAD_TIME) VALUES(?, ?, ?)";
	public static final String UPDATE_LAST_PROCESSED = "UPDATE LAST_PROCESSED SET LAST_VALUE=? WHERE PROCESS_NAME=?";
	public static final String GET_LAST_PROCESSED = "SELECT LAST_VALUE FROM LAST_PROCESSED WHERE PROCESS_NAME=?";

	public static final String INSERT_PERFORMANCE_METRIC = "INSERT INTO PRODUCTS.PERFORMANCE_METRIC (NAME, PERIOD, TOTAL_PROCESS_TIME, TOTAL_PROCESS_COUNT ) "
			+ "VALUES (?, ?, ?, ?)";
	public static final String DAILY_RECON_GET = "SELECT ID, NAME, RUN_DATE, TYPE, TEXT, COUNT, TIME_MODIFIED FROM PRODUCTS.DAILY_REC WHERE ID like ? AND TYPE = ? AND NAME = ? AND RUN_DATE between ? and ?";

	public static final String GET_UPDATED_PRODUCTS = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY P use index (TIME_MODIFIED_idx) "
			+ " JOIN PRODUCTS.PRODUCT_CATEGORY C WHERE P.TIME_MODIFIED >= ? AND C.PRODUCT_ID = P.PRODUCT_ID";
	public static final String GET_UPDATED_PRODUCTS_BY_RETAILER = "SELECT * FROM PRODUCTS.PRODUCT_SUMMARY P use index (TIME_MODIFIED_idx) "
			+ " JOIN PRODUCTS.PRODUCT_CATEGORY C WHERE P.TIME_MODIFIED >= ? AND P.RETAILER_ID = ? AND C.PRODUCT_ID = P.PRODUCT_ID";
	public static final String GET_UPDATED_CATEGORIES = "SELECT * FROM PRODUCTS.CATEGORY WHERE TIME_MODIFIED >= ?";
	public static final String GET_UPDATED_CATEGORIES_COUNT = "SELECT COUNT(*) FROM PRODUCTS.CATEGORY WHERE TIME_MODIFIED >= ?";
	public static final String GET_MAX_TIME_MODIFIED_PRODUCTS = "SELECT MAX(TIME_MODIFIED) FROM PRODUCTS.PRODUCT_SUMMARY";
	public static final String GET_MAX_TIME_MODIFIED_CATEGORIES = "SELECT MAX(TIME_MODIFIED) FROM PRODUCTS.CATEGORY";

	public static final String INSERT_PRICE_MOVEMENT_SUMMARY = "INSERT INTO PRODUCTS.DAILY_PRICE_MOVEMENT_SUMMARY (  PRODUCT_ID,LATEST_PRICE, "
			+ " LATEST_PRICE_TIME,LAST_PRICE,LAST_PRICE_TIME, MAX_PRICE,MAX_PRICE_TIME,AVERAGE_PRICE, IS_PRICE_DROP, TIME_MODIFIED ) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_PRICE_MOVEMENT_SUMMARY = "UPDATE PRODUCTS.DAILY_PRICE_MOVEMENT_SUMMARY set LATEST_PRICE = ?, "
			+ " LATEST_PRICE_TIME = ? ,LAST_PRICE = ? ,LAST_PRICE_TIME = ?, MAX_PRICE = ? ,MAX_PRICE_TIME = ? ,AVERAGE_PRICE = ?, IS_PRICE_DROP = ?, TIME_MODIFIED = ? "
			+ " where  PRODUCT_ID = ? ";

	/*
	 * This query is too slow in PROD public static final String
	 * GET_PRICE_HISTORY_FOR_SPAN =
	 * "select * from PRODUCT_PRICES_HISTORY C where C.PRODUCT_ID in ( select distinct A.PRODUCT_ID from PRODUCT_PRICES_HISTORY A "
	 * +
	 * "where A.TIME between ? and ? and  exists ( select * from PRODUCT_PRICES_HISTORY B where "
	 * + "B.PRODUCT_ID = A.PRODUCT_ID and B.TIME  < ? )) and C.TIME < ? ";
	 */
	public static final String GET_PRODUCT_PRICES_HISTORY_FOR_SPAN2 = "SELECT B.* FROM %s A, %s B "
			+ "WHERE A.PRODUCT_ID = B.PRODUCT_ID "
			+ "AND A.TIME >= ? AND A.TIME < ?  " + " AND B.TIME < ? ";

	/*
	 * public static final String GET_PRODUCT_ID_WITH_PRICECHANGE =
	 * "SELECT DISTINCT PRODUCT_ID FROM PRODUCT_PRICES_HISTORY " +
	 * "WHERE TIME >= ? AND TIME < ?  ";
	 */

	/*
	 * This query is too slow in PROD public static final String
	 * GET_PRICE_HISTORY_BY_ROWMODTIME =
	 * "select * from PRODUCT_PRICES_HISTORY C where C.PRODUCT_ID in ( select distinct A.PRODUCT_ID from PRODUCT_PRICES_HISTORY A "
	 * +
	 * "where A.TIME_MODIFIED between ? and ? and  exists ( select * from PRODUCT_PRICES_HISTORY B where "
	 * +
	 * "B.PRODUCT_ID = A.PRODUCT_ID and B.TIME_MODIFIED  < ? )) and C.TIME_MODIFIED < ? "
	 * ;
	 */
	/*
	 * public static final String GET_PRICE_HISTORY_BY_ROWMODTIME =
	 * "SELECT B.* FROM PRODUCT_PRICES_HISTORY A, PRODUCT_PRICES_HISTORY B " +
	 * "WHERE A.PRODUCT_ID = B.PRODUCT_ID " +
	 * "AND A.TIME_MODIFIED BETWEEN ? AND ?  " + "AND B.TIME_MODIFIED < ? ";
	 */

	public static String GET_PRICE_MOVEMENT_SUMMARY_SELECT = "SELECT PM.PRODUCT_ID, PM.LATEST_PRICE, PM.LATEST_PRICE_TIME, PM.LAST_PRICE, PM.LAST_PRICE_TIME, PM.MAX_PRICE, PM.MAX_PRICE_TIME, PM.AVERAGE_PRICE, PC.CATEGORY_ID, PS.PRODUCT_NAME, PS.URL, PS.IMAGE_URL "
			+ " FROM DAILY_PRICE_MOVEMENT_SUMMARY PM "
			+ " LEFT JOIN PRODUCT_SUMMARY PS  on PS.PRODUCT_ID=PM.PRODUCT_ID "
			+ " LEFT JOIN PRODUCT_CATEGORY PC  on PC.PRODUCT_ID = PM.PRODUCT_ID ";

	public static String GET_PRICE_MOVEMENT_SUMMARY_BY_DATE = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " where PM.LATEST_PRICE_TIME between ? and ?";

	public static String GET_PRICE_MOVEMENT_SUMMARY_BY_CATEGORY = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " WHERE PC.CATEGORY_ID = ? and PS.PRODUCT_ID=PM.PRODUCT_ID";

	public static String GET_PRICE_MOVEMENT_SUMMARY_BY_PRODUCT = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " WHERE PM.PRODUCT_ID = ? and PS.PRODUCT_ID=PM.PRODUCT_ID  ";

	public static String GET_ACTIVE_PRICE_MOVEMENT_SUMMARY_BY_RETAILER = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " where  PS.RETAILER_ID = ? and PS.ACTIVE='1'";

	public static String GET_PRICE_MOVEMENT_SUMMARY_BY_TIMEMODIFIED = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " WHERE PM.TIME_MODIFIED > ? ";

	public static String GET_PRICE_MOVEMENT_SUMMARY_OF_RETAILER_BY_TIMEMODIFIED = GET_PRICE_MOVEMENT_SUMMARY_SELECT
			+ " WHERE PM.TIME_MODIFIED > ? and PS.RETAILER_ID = ? ";

	public static final String GET_MAX_TIME_MODIFIED_PRICE_MOVEMENT_SUMMARY = "SELECT MAX(TIME_MODIFIED) FROM PRODUCTS.DAILY_PRICE_MOVEMENT_SUMMARY";

	public static final String GET_COUNT_EMAIL_ID = "SELECT COUNT(*) FROM PRODUCTS.USER WHERE EMAIL_ID = ?";
	public static final String GET_COUNT_REGISTERED_EMAIL_ID = "SELECT COUNT(*) FROM PRODUCTS.USER WHERE EMAIL_ID = ? AND REGISTERED='1'";
	public static final String GET_MAX_USER_ID = "SELECT MAX(USER_ID) FROM PRODUCTS.USER";
	public static final String INSERT_USER = "INSERT INTO PRODUCTS.USER (EMAIL_ID, NAME, PASSWORD, COUNTRY, PHONE, LAST_LOGGED_IN, "
			+ "ACTIVE, NEWSLETTER, REGISTERED) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_USER = "UPDATE PRODUCTS.USER SET NAME=?, PASSWORD=?, COUNTRY=?, PHONE=?, LAST_LOGGED_IN=?, ACTIVE=?, "
			+ "NEWSLETTER=?, REGISTERED=? WHERE EMAIL_ID=?";
	public static final String GET_USER = "SELECT * FROM PRODUCTS.USER WHERE EMAIL_ID=?";
	public static final String VERIFY_USER = "UPDATE PRODUCTS.USER SET ACTIVE='1' WHERE EMAIL_ID=?";

	public static final String INSERT_PRICE_ALERT = "INSERT INTO PRODUCTS.PRICE_ALERT (ALERT_ID, EMAIL_ID, PRODUCT_ID, ALERT_PRICE, TIME_MODIFIED, ALERT_START_TIME, "
			+ "ALERT_END_TIME, ALERT_TYPE, ACTIVE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_PRICE_ALERT = "UPDATE PRODUCTS.PRICE_ALERT " +
			"SET EMAIL_ID=?, ALERT_PRICE=?, TIME_MODIFIED=?, "
			+ "ALERT_END_TIME=?, ALERT_TYPE=?, ACTIVE=? WHERE ALERT_ID=?";
	public static final String UPDATE_STATUS_PRICE_ALERT = "UPDATE PRODUCTS.PRICE_ALERT SET ACTIVE=?, TIME_MODIFIED=? WHERE ALERT_ID=?";
	//TIME_MODIFIED is parameterized as we need alertid-<time_modified> unique in this table and same row from price_alert can be inserted twice in history table 
	public static final String INSERT_PRICE_ALERT_ARCHIVE = "INSERT INTO PRODUCTS.PRICE_ALERT_ARCHIEVE SELECT ALERT_ID, EMAIL_ID, PRODUCT_ID, ALERT_PRICE, ?, ALERT_START_TIME, ALERT_END_TIME," +
			"ALERT_TYPE, ACTIVE FROM PRICE_ALERT WHERE ALERT_ID=?";
	public static final String GET_PRICE_ALERT = "SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ALERT_ID=?";
	public static final String GET_MAX_ALERT_ID = "SELECT MAX(ALERT_ID) FROM PRODUCTS.PRICE_ALERT";
	public static final String GET_COUNT_UPDATED_PRICE_ALERTS = "SELECT COUNT(*) FROM PRODUCTS.PRICE_ALERT WHERE TIME_MODIFIED >= ?";
	public static final String GET_UPDATED_PRICE_ALERTS = "SELECT * FROM PRODUCTS.PRICE_ALERT WHERE TIME_MODIFIED >= ?";
	public static final String GET_COUNT_ACTIVE_PRICE_ALERTS_FROM_PRODUCT_ID = "SELECT COUNT(*) FROM PRODUCTS.PRICE_ALERT WHERE PRODUCT_ID = ? AND ACTIVE = '1'";
	public static final String GET_ACTIVE_PRICE_ALERTS_FROM_PRODUCT_ID = "SELECT * FROM PRODUCTS.PRICE_ALERT WHERE PRODUCT_ID = ? AND ACTIVE = '1'";
	public static final String GET_DISTINCT_ACTIVE_PRODUCT_IDS = "SELECT DISTINCT PRODUCT_ID FROM PRODUCTS.PRICE_ALERT WHERE ACTIVE = '1'";
	public static final String GET_ALL_ACTIVE_PRICE_ALERTS = "SELECT * FROM PRODUCTS.PRICE_ALERT WHERE ACTIVE = '1'";
	public static final String GET_PRICE_ALERT_FROM_USER_PRODUCT_ID = "SELECT * FROM PRODUCTS.PRICE_ALERT WHERE EMAIL_ID=? AND PRODUCT_ID=? AND ACTIVE='1'";
	public static final String GET_ALL_PRICE_ALERTS_OF_USER = "SELECT A.ALERT_ID, A.PRODUCT_ID, A.ALERT_PRICE, A.TIME_MODIFIED, A.ALERT_START_TIME, " +
			"A.ALERT_END_TIME, A.ALERT_TYPE, A.ACTIVE, B.RETAILER_ID, B.PRODUCT_NAME, B.PRICE, B.IMAGE_URL, B.URL FROM " +
			"PRICE_ALERT A, PRODUCT_SUMMARY B WHERE EMAIL_ID=? and B.PRODUCT_ID=A.PRODUCT_ID";

	public static final String INSERT_PRICE_ALERT_HISTORY = "INSERT INTO PRODUCTS.PRICE_ALERT_HISTORY (ALERT_ID, TIME_SENT, NOTIFICATION_PRICE) VALUES (?, ?, ?)";
	public static final String GET_PRICE_ALERT_HISTORY = "SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE ALERT_ID=?";
	public static final String GET_LATEST_FROM_PRICE_ALERT_HISTORY = "SELECT * FROM PRODUCTS.PRICE_ALERT_HISTORY WHERE TIME_SENT >= ?";
	// public static final String UPDATE_PRICE_ALERT_HISTORY =
	// "UPDATE PRODUCTS.PRICE_ALERT_HISTORY SET ALERT_ID=?, TIME_SENT=?, NOTIFICATION_PRICE=? WHERE ALERT_HISTORY_ID=?";
	// public static final String GET_MAX_ALERT_HISTORY_ID =
	// "SELECT MAX(ALERT_HISTORY_ID) FROM PRODUCTS.PRICE_ALERT_HISTORY";

	public static final String REMOVE_USER = "DELETE FROM PRODUCTS.USER WHERE EMAIL_ID=?";
	public static final String REMOVE_PRICE_ALERTS = "DELETE FROM PRODUCTS.PRICE_ALERT WHERE EMAIL_ID=?";

	
	public static final String GET_LATEST_FROM_PRODUCT_PRICES_HISTORY = "SELECT * FROM %s WHERE TIME_MODIFIED >= ?";
	//public static final String GET_PRODUCT_PRICES_HISTORY_FOR_SPAN = "SELECT * FROM PRODUCTS.PRODUCT_PRICES_HISTORY WHERE TIME_MODIFIED between ? and ? order by TIME_MODIFIED";

	//public static final String GET_PRICE_HISTORY_BY_PRODUCT_ID_RANGE = "SELECT PRODUCT_ID, PRICE, TIME FROM PRODUCT_PRICES_HISTORY "
		//	+ " WHERE PRODUCT_ID >=? and PRODUCT_ID<?";
	public static final String GET_PRODUCT_PRICES_HISTORY_BY_ACTIVE_PRODUCT_ID_RANGE_AND_RETAILER = "SELECT B.PRODUCT_ID, B.PRICE, B.TIME FROM PRODUCT_SUMMARY A, "
			+ " PRODUCT_PRICES_HISTORY B WHERE A.RETAILER_ID=? AND B.PRODUCT_ID>=? and B.PRODUCT_ID<? AND A.PRODUCT_ID=B.PRODUCT_ID AND A.ACTIVE='1'";
	public static final String GET_PRODUCT_PRICES_HISTORY_BY_RETAILER = "SELECT PRODUCT_ID, PRICE, TIME FROM %s";// table name is decided at run time
	public static final String GET_PRODUCT_REVIEWS_HISTORY_BY_RETAILER = "SELECT PRODUCT_ID, TIME, RATING, NUM_REVIEWS FROM %s";// table name is decided at run time

	public static final String GET_PRODUCT_SUMMARY_PRODUCT_ID_MAX = "SELECT MAX(PRODUCT_ID) from PRODUCT_SUMMARY";

	//public static final String GET_PRODUCT_ID_MAX_FROM_PRODUCT_REVIEWS_HISTORY = "SELECT MAX(PRODUCT_ID) from PRODUCT_REVIEWS_HISTORY";
	//public static final String GET_PRODUCT_REVIEWS_HISTORY_BY_PRODUCT_ID_RANGE = "SELECT PRODUCT_ID, TIME, RATING, NUM_REVIEWS FROM PRODUCT_REVIEWS_HISTORY "
		//	+ " WHERE PRODUCT_ID >=? and PRODUCT_ID<?";
	public static final String GET_PRODUCT_REVIEWS_HISTORY_BY_ACTIVE_PRODUCT_ID_RANGE_AND_RETAILER = "SELECT B.PRODUCT_ID, B.RATING, B.TIME, B.NUM_REVIEWS FROM PRODUCT_SUMMARY A, "
			+ " PRODUCT_REVIEWS_HISTORY B WHERE A.RETAILER_ID=? AND B.PRODUCT_ID>=? and B.PRODUCT_ID<? AND A.PRODUCT_ID=B.PRODUCT_ID AND A.ACTIVE='1'";

	//public static final String GET_PRODUCT_ID_MAX_FROM_PRODUCT_SELL_RANKS_HISTORY = "SELECT MAX(PRODUCT_ID) from PRODUCT_SELL_RANKS_HISTORY";
	//public static final String GET_PRODUCT_SELL_RANKS_HISTORY_BY_PRODUCT_ID_RANGE = "SELECT PRODUCT_ID, TIME, RANK FROM PRODUCT_SELL_RANKS_HISTORY "
		//	+ " WHERE PRODUCT_ID >=? and PRODUCT_ID<?";
	//public static final String GET_PRODUCT_SELL_RANKS_HISTORY_BY_ACTIVE_PRODUCT_ID_RANGE_AND_RETAILER = "SELECT B.PRODUCT_ID, B.RANK, B.TIME FROM PRODUCT_SUMMARY A, "
		//	+ " PRODUCT_SELL_RANKS_HISTORY B WHERE A.RETAILER_ID=? AND B.PRODUCT_ID>=? and B.PRODUCT_ID<? AND A.PRODUCT_ID=B.PRODUCT_ID AND A.ACTIVE='1'";

	// PRODUCTS CLEANUP QUERIES
	public static final String MARK_STALE_PRODUCTS_AS_INACTIVE = "UPDATE PRODUCT_SUMMARY SET ACTIVE = 0 where LAST_DOWNLOAD_TIME<? and ACTIVE=1";
	public static final String GET_VERY_STALE_PRODUCTS = "SELECT PRODUCT_ID, LAST_DOWNLOAD_TIME FROM PRODUCT_SUMMARY WHERE LAST_DOWNLOAD_TIME<? and ACTIVE=0";

	// PRODUCT_DOWNLOAD_RETRY QUERIES
	public static final String INSERT_PRODUCTS_DOWNLOAD_RETRY = "INSERT INTO PRODUCTS.PRODUCTS_DOWNLOAD_RETRY (RETAILER_ID, CATEGORY_ID, CATEGORY_NAME, START_URL, CREATE_TIME, RETRY_REASON) VALUES(?, ?, ?, ?, ?, ?)";
	public static final String GET_PRODUCT_DOWNLOAD_RETRY = "SELECT * WHERE RETAILER_ID=? AND CREATE_TIME >= ? AND PROCESSED != 'N'";
	public static final String SET_PROCESSED = "UPDATE PRODUCTS.PRODUCTS_DOWNLOAD_RETRY SET PROCESSED='Y' WHERE ID=?";
}