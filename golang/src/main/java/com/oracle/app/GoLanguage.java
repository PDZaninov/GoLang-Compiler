package com.oracle.app;

import java.util.Map;

import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.Parser;
import com.oracle.app.nodes.GoBasicNode;
import com.oracle.app.nodes.GoEvalRootNode;
//import com.oracle.app.nodes.local.GoLexicalScope
import com.oracle.app.runtime.GoNull;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoBigNumber;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.metadata.ScopeProvider;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

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
		Parser parseNodes = new Parser(source.getName());
		parseNodes.beginParse();
		//GoBasicNode man = parsenodes.parseFile("HelloGo.ast");
		

		//Parser parsenodes = new Parser("root");
		
		//GoBasicNode man = parsenodes.parseFile("HelloGo.ast");
		
		//GoRootNode evalMain = new GoRootNode(this,null,man,null,"main");
		

		
		function = Parser.parseGo(this, source);
		
		GoRootNode main = function.get("main");
		GoRootNode evalMain;
		if(main != null) {
			evalMain = new GoEvalRootNode(this, main.getFrameDescriptor(), main.getBodyNode(), main.getSourceSection(), main.getName(), function);
		}
		else {
			evalMain = new GoEvalRootNode(this, null, null, null, "[no_main]", function);
		}
		
		return Truffle.getRuntime().createCallTarget(evalMain);
	}

	@Override
    protected Object findExportedSymbol(GoContext context, String globalName, boolean onlyExplicit) {
        return context.getFunctionRegistry().lookup(globalName, false);
    }

    @Override
    protected Object lookupSymbol(GoContext context, String symbolName) {
        return context.getFunctionRegistry().lookup(symbolName, false);
    }

    @Override
    protected Object getLanguageGlobal(GoContext context) {
        /*
         * The context itself is the global function registry. Go does not have global variables.
         */
        return context;
    }

    @Override
    protected boolean isVisible(GoContext context, Object value) {
        return value != GoNull.SINGLETON;
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
    	/*
        if (!(object instanceof TruffleObject)) {
            return false;
        }
        TruffleObject truffleObject = (TruffleObject) object;
        return truffleObject instanceof GoFunction || truffleObject instanceof GoBigNumber || GoContext.isGoObject(truffleObject);
        */
        return true;
    }

    @Override
    protected String toString(GoContext context, Object value) {
        if (value == GoNull.SINGLETON) {
            return "NULL";
        }
        if (value instanceof GoBigNumber) {
            return super.toString(context, ((GoBigNumber) value).getValue());
        }
        if (value instanceof Long) {
            return Long.toString((Long) value);
        }
        return super.toString(context, value);
    }

    @Override
    protected Object findMetaObject(GoContext context, Object value) {
        if (value instanceof Number || value instanceof GoBigNumber) {
            return "Number";
        }
        if (value instanceof Boolean) {
            return "Boolean";
        }
        if (value instanceof String) {
            return "String";
        }
        if (value == GoNull.SINGLETON) {
            return "Null";
        }
        if (value instanceof GoFunction) {
            return "Function";
        }
        return "Object";
    }

    @Override
    protected SourceSection findSourceLocation(GoContext context, Object value) {
        if (value instanceof GoFunction) {
            GoFunction f = (GoFunction) value;
            return f.getCallTarget().getRootNode().getSourceSection();
        }
        return null;
    }
/*
    @Override
    public AbstractScope findScope(GoContext context, Node node, Frame frame) {
        return GoLexicalScope.createScope(node);
    }
*/
    public static GoContext getCurrentContext() {
        return getCurrentContext(GoLanguage.class);
    }

}
