package com.oracle.app;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class PointerTest {
	private PolyglotEngine engine;
	private PolyglotEngine.Value math;
	
	@Before
	public void setUp() throws Exception{
		engine = PolyglotEngine.newBuilder().build();
		Source source = Source.newBuilder(""
				+"package main\n"
				+ "\n"
				+ "func main() int{\n"
				+ "	a := 5 \n"
				+ "	ptr := &a \n"
				+ "	*ptr = 10 \n"
				+ "	return a \n"
				+ "}").
				name("UnitTest.go").
				mimeType("text/x-go").build();
		ToolChain.executeCommands(source);
		engine.eval(source);
		math = engine.findGlobalSymbol("main");
	}
	
	@After
	public void tearDown(){
		engine.dispose();
	}
	
	@Test
	public void test(){
		Number ret = math.execute().as(Number.class);
		assertEquals(10,ret.intValue());
	}
}
