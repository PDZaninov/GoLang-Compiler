package com.oracle.app;

import java.util.Map;

import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.Parser;
import com.oracle.app.nodes.GoEvalRootNode;

import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.metadata.ScopeProvider;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

@TruffleLanguage.Registration(id = "go", name = "Go",version = "0.1", mimeType = GoLanguage.MIME_TYPE)
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
	 * Needs to be able to find a main function and return it
	 */
	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception{
		Source source = request.getSource();
		Map<String, GoRootNode> function;
		
		function = Parser.parseGo(this, source);
		
		GoRootNode main = function.get("main");
		GoRootNode evalMain;
		if(main != null) {
			evalMain = new GoEvalRootNode(this, main.getFrameDescriptor(), main.getBodyNode(), main.getSourceSection(), main.getName(), function);
		}
		else {
			evalMain = new GoEvalRootNode(this, null, null, null, "[no_main]", function);
		}

		return Truffle.getRuntime().createCallTarget(main);
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
