<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 4:25 PM
 *
 * Extending PhP by introducing Enums in PHP.
 */

abstract class Enumeration {
	protected static $constCache = NULL;

	private static function getConstants() {
		if(static::$constCache == NULL) {
			$reflect = new ReflectionClass(get_called_class());
			static::$constCache = $reflect->getConstants();
		}

		return static::$constCache;
	}

	public static function isValidName($str, $strict = true) {
		$constants = self::getConstants();

		if($strict)
			return array_key_exists($str, $constants);

		$keys = array_map('strtolower', array_keys($constants));
		return in_array(strtolower($str), $keys);
	}

	public static function isValidValue($val) {
		$values = array_values(self::getConstants());
		return in_array($val, $values, $strict = true);
	}

	/*
	 * Return 1: If valid value
	 * Return 2: If Valid Name
	 *
	 * Returns 0 if INVALID Enumeration */
	public static function isValid($val) {
		if(is_numeric($val) && self::isValidValue(intval($val)))
			return 1;
		if(self::isValidName($val))
			return 2;
		return 0;
	}

	public static function getValueFromName($name) {
		$constants = self::getConstants();

		return $constants[$name];
	}

	public static function getNameFromValue($val) {
		$constants = self::getConstants();

		if($name = array_search($val, $constants, $strict = true))
			return $name;
		else
			return "Not Known";
	}
};