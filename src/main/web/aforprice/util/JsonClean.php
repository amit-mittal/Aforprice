<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 7:05 PM
 *
 * An interface to json encode and decode for handling errors properly
 */

class JsonClean {
	public static $error_id;
	public static $error_msg;

	private static $errors = array(
		JSON_ERROR_NONE             => NULL,
		JSON_ERROR_DEPTH            => 'Maximum stack depth exceeded',
		JSON_ERROR_STATE_MISMATCH   => 'Underflow or the modes mismatch',
		JSON_ERROR_CTRL_CHAR        => 'Unexpected control character found',
		JSON_ERROR_SYNTAX           => 'Syntax error, malformed JSON',
		JSON_ERROR_UTF8             => 'Malformed UTF-8 characters, possibly incorrectly encoded'
	);

	public static function decode(&$encoded_string) {
		$decodedObject = json_decode($encoded_string, true);

		self::$error_id = json_last_error();

		if(self::$error_id === JSON_ERROR_NONE) {
			return $decodedObject;
		} else {
			self::$error_msg = array_key_exists(self::$error_id, self::$errors) ? self::$errors[self::$error_id] : "Unknown Error in JSON decoding";

			return NULL;
		}
	}
};