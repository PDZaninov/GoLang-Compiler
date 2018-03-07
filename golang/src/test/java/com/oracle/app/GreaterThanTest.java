package com.oracle.app;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GreaterThanTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GreaterThanTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GreaterThanTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testApp() throws IOException
    {	String[] args = new String[1];
		args[0] = "GreaterThan.ast";
        GoMain.main(args);
    }
}