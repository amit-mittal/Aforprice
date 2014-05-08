namespace java thrift.genereated.user
namespace php Thrift.Generated.User

const list<i32> USER_SERVER_PORT = [26001, 26001]

exception UserException {
	1: i32 error_code,
	2: string error_msg
}

struct UserThrift{
	1: required string emailId;
	2: optional string name;
	3: optional string password;
	4: optional string country;
	5: optional string phone;
	6: optional string lastLoggedIn;
	7: optional bool active;
	8: optional bool newsletter;
	9: optional bool registered;
}

service UserService{
	bool addUserThrift(1:UserThrift user) throws (1: UserException e);
	bool updateUserThrift(1:UserThrift user) throws (1: UserException e);
	bool loginUserThrift(1:string emailId, 2:string password) throws (1: UserException e);
	UserThrift getUserThrift(1:string emailId) throws (1: UserException e);
	bool verifyUserThrift(1:string emailId) throws (1: UserException e);
}