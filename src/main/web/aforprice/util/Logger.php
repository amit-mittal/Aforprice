<?php
/**
 * User: chirag
 * Date: 25/12/13
 * Time: 4:07 PM
 *
 * A simple class to log messages to a file.
 */

/* TODO: make it "system" friendly */

class Logger {
	private static $directory = NULL;

	const DEFAULT_DIRECTORY = "/var/log/aforprice";

	private static $defaultPermissions = 0777;

	private static $isOpen_log = false;
	private static $fileName_log = "";
	private static $fileHandle_log = NULL;

	private static $isOpen_error = false;
	private static $fileName_error = "";
	private static $fileHandle_error = NULL;

	public function __construct() {
		self::openLog();
//		self::openError();
	}

	private static function openFile($directory, $filename) {
		if(!file_exists($directory)) {
			if(!mkdir($directory, self::$defaultPermissions, true)) {
				error_log("class Logger: Cannot make directory: ".$directory);
				return NULL;
			}
		}

		if(file_exists($filename) && !is_writable($filename)) {
			error_log("class Logger: Log File is not writable: ".$filename);
			return NULL;
		}

		if(!($fileHandle = fopen($filename, 'a'))) {
			error_log("class Logger: Cannot open file: ".$filename);
			return NULL;
		}

		return $fileHandle;
	}

	private static function openLog() {
		if(self::$isOpen_log)
			return true;

		if(is_null(self::$directory)) {
			self::$directory = self::DEFAULT_DIRECTORY;
		}

		self::$fileName_log = self::$directory
			.DIRECTORY_SEPARATOR
			.'phplog_'
			.date('Y-m-d')
			.'.log';

		if((self::$fileHandle_log = self::openFile(self::$directory, self::$fileName_log)) === NULL) {
			return false;
		}

		self::$isOpen_log = true;
		return true;
	}

	private static function openError() {
		if(self::$isOpen_error)
			return true;

		if(is_null(self::$directory)) {
			self::$directory = self::DEFAULT_DIRECTORY;
		}

		self::$fileName_error = self::$directory
			.DIRECTORY_SEPARATOR
			.'phperror_'
			.date('Y-m-d')
			.'.log';

		if((self::$fileHandle_error = self::openFile(self::$directory, self::$fileName_error)) === NULL) {
			return false;
		}

		self::$isOpen_error = true;
		return true;
	}

	public static function logMessage($message) {
		if(!self::openLog()) {
			error_log("[LOG] ".$message);
			return false;
		}

		fwrite(self::$fileHandle_log, '['.date('Y-m-d G:i:s').'] '.$message."\r\n");
	}

	public static function logError($error) {
		if(!self::openError()) {
			error_log("[ERROR] ".$error);
			return false;
		}

		fwrite(self::$fileHandle_error, '['.date('Y-m-d G:i:s').'] '.$error."\r\n");
	}

	public function __destruct() {
		if(self::$isOpen_log)
			fclose(self::$fileHandle_log);
		if(self::$isOpen_error)
			fclose(self::$fileHandle_error);
	}
};

$LOGGER = new Logger();