namespace java thrift.genereated.pricealert
namespace php Thrift.Generated.PriceAlert

const list<i32> PRICE_ALERT_SERVER_PORT = [25001, 25001]

enum PRICE_ALERT_ERROR_CODE{
	SYSTEM_ERROR,
	DUPLICATE_PRICE_ALERT_ON_PRODUCTID,
	INVALID_ALERTID
}
exception PriceAlertException {
	1: PRICE_ALERT_ERROR_CODE error_code,
	2: string error_msg
}
 
enum PriceAlertTypeThrift{
	ALERT_WHEN_AT_PRICE,
	ALERT_WHEN_PRICE_DROPS
}
 
struct PriceAlertThrift{
 	1: required string emailId;
	2: required i32 alertId;
 	3: required i32 productId;
 	4: required double alertPrice;
 	5: optional string timeModified;
	6: optional string alertCreationDate;
	7: optional string alertExpirationDate;
	8: required PriceAlertTypeThrift alertType;
	9: required bool alertActive;
	10: optional string retailer;
	11: optional string name;
	12: optional string url;
	13: optional string imageUrl;
	14: required double currPrice;
	15: required double purchasePrice;
	16: optional string purchaseDate; 	
}
 
service PriceAlertService{
 	bool addPriceAlertThrift(1:PriceAlertThrift alert) throws (1: PriceAlertException e);
 	bool updatePriceAlertThrift(1:PriceAlertThrift alert) throws (1: PriceAlertException e);
	bool removePriceAlertThrift(1:i32 alertId) throws (1: PriceAlertException e);
	PriceAlertThrift getPriceAlertThrift(1:string emailId, 2:i32 productId) throws (1: PriceAlertException e);
	list<PriceAlertThrift> getPriceAlerts(1:string emailId) throws (1: PriceAlertException e);
	bool verifyPriceAlertThrift(1:i32 alertId) throws (1: PriceAlertException e);
}