package com.oracle.app;

import com.oracle.runtime.GoContext;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.metadata.ScopeProvider;
import com.oracle.truffle.api.nodes.Node;

@TruffleLanguage.Registration(id = "go", name = "Go	",version = "0.1", mimeType = GoLanguage.MIME_TYPE)
public final class GoLanguage extends TruffleLanguage<GoContext> implements ScopeProvider<GoContext>{

	public static volatile int counter;
	
	public static final String MIME_TYPE = "text/plain";
	
	public GoLanguage(){
		counter++;
	}
	
	@Override
	public AbstractScope findScope(GoContext langContext, Node node,
			Frame frame) {
		//Returns something to do with lexical scope
		return null;
	}

	@Override
	protected GoContext createContext(Env env) {
		return new GoContext(this, env);                    
	}
	
	/*
	 * Call the parse function here. Set the calltarget to the rootnode found
	 * while parsing then return it
	 */
	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception{
		//MAiN PARSiNG STUFF
		System.out.println("PARSE FUNC");
		return null;
	}

	@Override
	protected Object getLanguageGlobal(GoContext context) {
		//Might need to look into this. Something to do with global variables
		return context;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		//Returns whether or not the object is a TruffleObject
		return false;
	}
	
	public static GoContext getCurrentContext(){
		return getCurrentContext(GoLanguage.class);
	}

}
