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
	private ByteArrayOutputStream os;

	@Before
	public void setUp() throws Exception {
		os = new ByteArrayOutputStream();
		engine = PolyglotEngine.newBuilder().setOut(os).build();
		
	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public void test() throws Exception{
		engine.eval(
				Source.newBuilder(""
						+"package main\n"
						+"\n"
						+"func main() {\n"
						+"    var a int = 5 \n"
						+"    println(a) \n"
						+"\\}").
				name("VariablesTest.go").
				mimeType("text/x-go").
				build());
		math = engine.findGlobalSymbol("main");
		final Object value = math.get();
		assertTrue("It's truffle object", value instanceof TruffleObject);
		Runnable runnable = JavaInterop.asJavaFunction(Runnable.class, (TruffleObject) value);
        runnable.run();
		assertEquals("5\n", os.toString("UTF-8"));
		
	}

}
