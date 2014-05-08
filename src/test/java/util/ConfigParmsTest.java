package util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import entities.DbCredentials;
import util.ConfigParms;
import util.ConfigParms.Environment;

public class ConfigParmsTest extends AbstractTest{

	@Test
	public void testEnvironment() {
		//Default is QA
		_assertEquals(Environment.QA, ConfigParms.getInstance().getEnvironment());
		if(Constants.isWindows()){
			if(ConfigParms.getInstance().isAshishLaptop())
				_assertEquals(new DbCredentials("localhost:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS"), ConfigParms.getInstance().getDbCredentials());
			else
				_assertEquals(new DbCredentials("localhost", "mysqlqa", "mysqlPa2s", "PRODUCTS"), ConfigParms.getInstance().getDbCredentials());
		}
		else
			_assertEquals(new DbCredentials("192.168.1.120:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS"),
						ConfigParms.getInstance().getDbCredentials());

		System.setProperty("ENVIRONMENT", "PRODUCTION");
		ConfigParms.regtestResetInstance();
		_assertEquals(Environment.PRODUCTION, ConfigParms.getInstance().getEnvironment());
		if(ConfigParms.getInstance().isLocalNetwork())
			assertEquals(new DbCredentials("192.168.1.120", "mysqlprod", "mysqlprodPa2s", "PRODUCTS"),
				ConfigParms.getInstance().getDbCredentials());
		else
			assertEquals(new DbCredentials("69.136.251.135", "mysqlprod", "mysqlprodPa2s", "PRODUCTS"),
					ConfigParms.getInstance().getDbCredentials());
		System.setProperty("ENVIRONMENT", "PRODUCTION_REPLICA");
		ConfigParms.regtestResetInstance();
		_assertEquals(Environment.PRODUCTION_REPLICA, ConfigParms.getInstance().getEnvironment());
		_assertEquals(new DbCredentials("192.168.1.90", "mysqlprod2", "mysqlprod2Pa2s", "PRODUCTS"),
				ConfigParms.getInstance().getDbCredentials());
		
		System.setProperty("ENVIRONMENT", "QA");
		ConfigParms.regtestResetInstance();
		_assertEquals(Environment.QA, ConfigParms.getInstance().getEnvironment());
		if(ConfigParms.getInstance().isAshishLaptop())
			_assertEquals(new DbCredentials("localhost:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS"),
					ConfigParms.getInstance().getDbCredentials());
		else if(Constants.isWindows())
			_assertEquals(new DbCredentials("localhost", "mysqlqa", "mysqlPa2s", "PRODUCTS"),
						ConfigParms.getInstance().getDbCredentials());
		else
			_assertEquals(new DbCredentials("192.168.1.120:3307", "mysqlqa", "mysqlPa2s", "PRODUCTS"),
						ConfigParms.getInstance().getDbCredentials());

		System.setProperty("ENVIRONMENT", "QA_REPLICA");
		ConfigParms.regtestResetInstance();
		_assertEquals(Environment.QA_REPLICA, ConfigParms.getInstance().getEnvironment());
		_assertEquals(new DbCredentials("192.168.1.90:3307", "mysqlqa2", "mysql2Pa2s", "PRODUCTS"),
				ConfigParms.getInstance().getDbCredentials());

		System.setProperty("ENVIRONMENT", "DEV");
		ConfigParms.regtestResetInstance();
		_assertEquals(Environment.DEV, ConfigParms.getInstance().getEnvironment());
		_assertEquals(new DbCredentials("localhost", "mysqlqa", "mysqlPa2s", "PRODUCTS"),
				ConfigParms.getInstance().getDbCredentials());
	}

}
