package com.oracle.app;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class ControlFlowTest {

	private PolyglotEngine engine;
	private PolyglotEngine.Value math;
	
	@Before
	public void setUp() throws IOException, InterruptedException{
		engine = PolyglotEngine.newBuilder().build();
		Source source = Source.newBuilder(""
				+ "package main \n"
				+ "\n"
				+ "func main() int{ \n"
				+ "	a := 1 \n"
				+ "	if a == 2 { \n"
				+ "		a = 2 \n"
				+ "	} else if num := 10; num < 0 { \n"
				+ "		a = 3 \n"
				+ "	} else { \n"
				+ "		a = 4 \n"
				+ "	} \n"
				+ "\n"
				+ "	i := 3 \n"
				+ "	for i <= 5 { \n"
				+ "		a += 1 \n"
				+ "		i += 1 \n"
				+ "	} \n"
				+ "\n"
				+ "	switch a { \n"
				+ "		case 7: \n"
				+ "			a = 100 \n"
				+ "		case 5: \n"
				+ "			a = 50 \n"
				+ "		default: \n"
				+ "			a = 10 \n"
				+ "		} \n"
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
		assertEquals(100,ret.intValue());
	}
}
