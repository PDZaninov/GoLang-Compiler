package com.oracle.app;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class SliceTest {
	private PolyglotEngine engine;
	private PolyglotEngine.Value math;

	@Before
	public void setUp() throws Exception {
		engine = PolyglotEngine.newBuilder().build();
		Source source = Source.newBuilder(""
				+"package main\n"
				+"\n"
				+"func main() int{ \n"
				+ "	  result := 0 \n"
				+ "   b := [5]int{1 , 2, 3, 4, 5} \n"
				+ "	  s := b[1:3] \n"
				+ "	  for j := 0; j < len(s); j++ { \n"
				+ "		result += s[j] \n"
				+ "	  } \n"
				+ "	  s = b[:3] \n"
				+ "	  for j := 0; j < len(s); j++ { \n"
				+ "		result += s[j] \n"
				+ "	  } \n"
				+ "	  s = b[3:] \n"
				+ "	  for j := 0; j < len(s); j++ { \n"
				+ "		result += s[j] \n"
				+ "	  } \n"
				+ "	  s[0] = 10 \n"
				+ "	  for j := 0; j < len(s); j++ { \n"
				+ "		result += s[j] \n"
				+ "	  } \n"
				+ "	  return result \n"
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
		assertEquals(35,ret.intValue());
		
	}
}
