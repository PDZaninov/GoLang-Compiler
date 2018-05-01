package com.oracle.app;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

public class LexicalScopeTest {

	private PolyglotEngine engine;
	private PolyglotEngine.Value math;
	private File goSource;
	String filename = "lexical_scope_same_ident";
	
	@Before
	public void setUp() throws IOException, InterruptedException{
		engine = PolyglotEngine.newBuilder().build();
		goSource = new File("src/test/java/com/oracle/app/lexical_scope_same_ident.go");
		Source source = Source.newBuilder(goSource).build();
		ToolChain.executeCommands(source);
		engine.eval(source);
		math = engine.findGlobalSymbol("main");
	}
	
	@After
	public void tearDown() throws IOException, InterruptedException{
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("rm " + filename + ".go " + filename + ".ast");
		p.waitFor();
		engine.dispose();
	}
	
	@Test
	public void test() {
		Number ret = math.execute().as(Number.class);
		assertEquals(2,ret.intValue());
	}
}
