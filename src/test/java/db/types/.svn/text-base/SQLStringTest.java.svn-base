package db.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class SQLStringTest {

	@Test
	public void testGetRHValue() {
		SQLString s = new SQLString("Blah \"%'\\ Blah \\'%\"");
		System.out.println(s.getRHValue());
		assertTrue(s.getRHValue().equals("'Blah \\\"\\%\\'\\\\ Blah \\\\\\'\\%\\\"'"));
	}

}
