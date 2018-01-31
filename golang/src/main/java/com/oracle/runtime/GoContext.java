package com.oracle.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.oracle.app.GoLanguage;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.object.Layout;
import com.oracle.truffle.api.source.Source;

/*
 * Keeps track of what Go code is running in this context? 
 * Used by some builtin functions
 */

public final class GoContext {
	private static final Source BUILTIN_SOURCE = Source.newBuilder("").name("Go builtin").mimeType(GoLanguage.MIME_TYPE).build();
	private static final Layout LAYOUT = Layout.createLayout();
	
	private final Env env;
	private final BufferedReader input;
	private final PrintWriter output;
	//GoFunctionRegistry
	//private final Shape emptyShape;
	private final GoLanguage language;
	
	public GoContext(GoLanguage language, Env env){
		this.env = env;
		this.input = new BufferedReader(new InputStreamReader(env.in()));
		this.output = new PrintWriter(env.out(), true);
		this.language = language;
		//this.allocationReporter = env.lookup(AllocationReporter.class;
		
		
	}
	
	public BufferedReader getInput() {
		return input;
	}
	
	public PrintWriter getOutput(){
		return output;
	}
	
	public CallTarget parse(Source source){
		return env.parse(source);
	}
	
	
	/*
	 * Builtin functions get installed in this class
	 */
}
