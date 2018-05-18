package pt.up.fc.dcc.asura.builder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple HTTP server to run a game.
 * 
 * @author José Paulo Leal <zp@dcc.fc.up.pt>
 * @author José Carlos Paiva <josepaiva94@gmail.com>
 */
public class GameServerTest extends TestCase {
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public GameServerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(GameServerTest.class);
	}

	/**
	 * Rigorous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}
}
