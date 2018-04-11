package com.oracle.app;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class SimpleMathTest {
	
	private PolyglotEngine engine;
	private PolyglotEngine.Value math;

	@Before
	public void setUp() throws Exception {
		engine = PolyglotEngine.newBuilder().build();
		Source source = Source.newBuilder(""
				+"package main\n"
				+"\n"
				+"func main() int{\n"
				+"    var a, n int = 1, 5 \n"
				+"	  for x:= 1; x <= n; x++ {\n"
				+"	  	a *= x\n"
				+"    }\n"
				+"    return a \n"
				+"}").
		name("UnitTest.go").
		mimeType("text/x-go").
		build();
		ToolChain.executeCommands(source);
		engine.eval(source);
		math = engine.findGlobalSymbol("main");
	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public void test() throws Exception{
		
		Number ret = math.execute().as(Number.class);
		assertEquals(120,ret.intValue());
		
	}

}
