<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 5:20 PM
 *
 * Response object consists of data and the error object(if exists);
 */

class ResponseObject {
	var $data;
	var $error;
	var $msg;

	function __construct($error, $response, $msg = "") {
		$this->data = $response;

		if($error === 0) {
			$this->error = false;
			$this->msg = $msg;

		} else {
			$this->error = true;
			$this->msg = ResponseErrorType::getNameFromValue($error).": ".$msg;

			/* If its a repeatable error, we can tell the user to try again. */
			$this->repeatRequest = ((in_array($error, ResponseErrorType::getServerErrors())) ? true : false);
		}
	}
}