package com.oracle.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.java.JavaInterop;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class SimpleMathTest {
	
	private PolyglotEngine engine;
	private PolyglotEngine.Value math;

	@Before
	public void setUp() throws Exception {
		engine = PolyglotEngine.newBuilder().build();
		engine.eval(
				Source.newBuilder(""
						+"package main\n"
						+"\n"
						+"func variable() int{\n"
						+"    var a int = 5 \n"
						+"    return a \n"
						+"\\}").
				name("VariablesTest.go").
				mimeType("text/x-go").
				build());
	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public void test() throws Exception{
		
		math = engine.findGlobalSymbol("variable");
		Number ret = math.execute().as(Number.class);
		assertEquals(5,ret.intValue());
		
	}

}
