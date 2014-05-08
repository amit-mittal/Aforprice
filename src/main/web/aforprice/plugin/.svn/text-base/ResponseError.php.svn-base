<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 4:18 PM
 *
 * Contains the error object and error type enumeration.
 */

/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));

	include_once '../util/Enumeration.php';

class ResponseErrorType extends Enumeration {
	protected static $constCache = NULL;

	const UNKNOWN = -1;
	const SUCCESS = 0;
    const PARTIAL_SUCCESS = 1;
	const MISSING_ARGUMENT = 2;
	const INVALID_ARGUMENT = 3;
	const THRIFT = 4;
	const INVALID_REQUEST = 5;

	private static $serverErrors = NULL;

	public static function getServerErrors() {
		/* These are the errors which are from the server and maybe
		clarified if someone just tries again. */
		if(self::$serverErrors === NULL) {
			self::$serverErrors = array(
				ResponseErrorType::THRIFT,
				ResponseErrorType::PARTIAL_SUCCESS
			);
		}

		return self::$serverErrors;
	}
};