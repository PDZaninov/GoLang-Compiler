package com.oracle.app;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ModuloTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ModuloTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ModuloTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     * @throws InterruptedException 
     */
    public void testApp() throws IOException, InterruptedException
    {	String[] args = new String[1];
		args[0] = "Modulo.ast";
        GoMain.main(args);
    }
}
