package com.oracle.app.builtins.fmt;

import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoException;
import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Imports keep track of their own function definitions.
 * 1) Creating new imports will require a parent class to contain all imports
 * 2) A way to parse imports or simulate parsing library imports so that they return a function registry
 * @author Trevor
 *
 */
public class FmtFunctionList extends GoExpressionNode{

	private final Map<String, GoFunction> functions = new HashMap<>();
	private final GoLanguage language;
	
	public FmtFunctionList(GoLanguage language){
		this.language = language;
		installBuiltins();
	}
	
	public GoFunction register(String name, GoRootNode rootNode) {
        GoFunction function = new GoFunction(language,name);
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        function.setCallTarget(callTarget);
        functions.put(name, function);
        return function;
    }
	
	public void installBuiltins(){
		GoExpressionNode bodyNode = GoFmtPrintln.getFmtPrintln();
		String name = GoContext.lookupNodeInfo(bodyNode.getClass()).shortName();
		GoRootNode rootNode = new GoRootNode(language, new FrameDescriptor(), null, null, bodyNode, null, name);
		register(name,rootNode);
	}

	public GoFunction getFunction(String name){
		GoFunction result = functions.get(name);
		if(result == null){
			throw new GoException("Undefined: fmt."+name);
		}
		return result;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this;
	}
}
