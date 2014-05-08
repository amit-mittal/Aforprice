namespace java thrift.genereated.config
namespace php Thrift.Generated.Config

const list<i32> CONFIG_SERVER_PORT =  [27001, 27001]

struct DateObj{
	1:required i16 date;
	2:required i16 month;
	3:required i16 year;
}

struct Event{
	1: required DateObj eventDate;
	2: required string description;
}

const list<Event> EVENTS = 	[
								{"eventDate": {"date": 1, "month": 1, "year": 2013}, "description": "New Year's Day"},
								{"eventDate": {"date": 21, "month": 1, "year": 2013}, "description": "Martin Luther King Day"},
								{"eventDate": {"date": 14, "month": 2, "year": 2013}, "description": "Valentine's Day"},
								{"eventDate": {"date": 18, "month": 2, "year": 2013}, "description": "Presidents' Day (Washington's Birthday)"},
								{"eventDate": {"date": 12, "month": 5, "year": 2013}, "description": "Mother's Day"},
								{"eventDate": {"date": 27, "month": 5, "year": 2013}, "description": "Memorial Day"},
								{"eventDate": {"date": 16, "month": 6, "year": 2013}, "description": "Father's Day"},
								{"eventDate": {"date": 4, "month": 7, "year": 2013}, "description": "Independence Day"},
								{"eventDate": {"date": 2, "month": 9, "year": 2013}, "description": "Labor Day"},
								{"eventDate": {"date": 14, "month": 10, "year": 2013}, "description": "Columbus Day"},
								{"eventDate": {"date": 31, "month": 10, "year": 2013}, "description": "Halloween"},
								{"eventDate": {"date": 11, "month": 11, "year": 2013}, "description": "Veterans Day"},
								{"eventDate": {"date": 28, "month": 11, "year": 2013}, "description": "Thanksgiving Day"},
								{"eventDate": {"date": 24, "month": 12, "year": 2013}, "description": "Christmas Eve"},
								{"eventDate": {"date": 25, "month": 12, "year": 2013}, "description": "Christmas Day"},
								{"eventDate": {"date": 31, "month": 12, "year": 2013}, "description": "New Year's Eve"}
							]

service ConfigService{
	list<Event> getEvents(1:DateObj startDate, 2:DateObj endDate);
}