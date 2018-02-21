package com.oracle.app.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

import com.oracle.app.GoLanguage;
import com.oracle.app.builtins.GoBuiltinNode;
import com.oracle.app.builtins.GoPrintfBuiltinFactory;
import com.oracle.app.builtins.GoPrintlnBuiltinFactory;
import com.oracle.app.builtins.GoTrueValueBuiltinFactory;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.local.GoReadArgumentsNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.NodeInfo;
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
	private final GoFunctionRegistry functionRegistry;
	//private final Shape emptyShape;
	private final GoLanguage language;
	
	public GoContext(GoLanguage language, Env env){
		this.env = env;
		this.input = new BufferedReader(new InputStreamReader(env.in()));
		this.output = new PrintWriter(env.out(), true);
		this.language = language;
		this.functionRegistry = new GoFunctionRegistry(language);
		//this.allocationReporter = env.lookup(AllocationReporter.class;
		installBuiltins();
		
	}
    public static Object fromForeignValue(Object a) {
        if (a instanceof Long || a instanceof BigInteger || a instanceof String || a instanceof Boolean) {
            return a;
        } else if (a instanceof Character) {
            return String.valueOf(a);
        } else if (a instanceof Number) {
            return fromForeignNumber(a);
        } else if (a instanceof TruffleObject) {
            return a;
        } else if (a instanceof GoContext) {
            return a;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalStateException(a + " is not a Truffle value");
    }
    
    @TruffleBoundary
    private static long fromForeignNumber(Object a) {
        return ((Number) a).longValue();
    }
    
	public BufferedReader getInput() {
		return input;
	}
	
	public PrintWriter getOutput(){
		return output;
	}

	public GoFunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }
	
	public CallTarget parse(Source source){
		return env.parse(source);
	}
	
	
	/*
	 * Builtin functions get installed in this class
	 */
	private void installBuiltins(){
		installBuiltin(GoPrintfBuiltinFactory.getInstance());
		installBuiltin(GoPrintlnBuiltinFactory.getInstance());
		installBuiltin(GoTrueValueBuiltinFactory.getInstance());
	}
	
	public void installBuiltin(NodeFactory<? extends GoBuiltinNode> factory){
		int argumentCount = factory.getExecutionSignature().size();
		GoExpressionNode[] argumentNodes = new GoExpressionNode[argumentCount];
		//Gets the parameters of the function, Need GoReadArgumentNode
		for(int i = 0; i < argumentCount; i++){
			argumentNodes[i] = new GoReadArgumentsNode(i);
		}
		
		
		GoBuiltinNode builtinBodyNode = factory.createNode((Object) argumentNodes);
		builtinBodyNode.addRootTag();
		String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
		//Source section goes here and into the rootnode where null is at
		
		GoRootNode rootNode = new GoRootNode(language, new FrameDescriptor(), builtinBodyNode, null, name);
		getFunctionRegistry().register(name, rootNode);
	}
	
	/*
	 * Gets the shorthand name of the builtin function
	 */
	public static NodeInfo lookupNodeInfo(Class<?> clazz){
		if(clazz == null){
			return null;
		}
		NodeInfo info = clazz.getAnnotation(NodeInfo.class);
		if(info != null){
			return info;
		}
		else{
			return lookupNodeInfo(clazz.getSuperclass());
		}
	}
}
