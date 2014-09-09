package org.gwtwidgets.server.spring.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ReflectionUtilsTest.class);
		suite.addTestSuite(TestRPCExporter.class);
		suite.addTestSuite(TestJunit.class);
		suite.addTestSuite(TestHandler.class);
		//$JUnit-END$
		return suite;
	}

}
