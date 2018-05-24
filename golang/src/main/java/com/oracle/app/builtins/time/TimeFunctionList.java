package com.oracle.app.builtins.time;

import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoException;
import com.oracle.app.GoLanguage;
import com.oracle.app.builtins.fmt.GoFmtPrintln;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;

public class TimeFunctionList extends GoExpressionNode {

	private final Map<String,GoFunction> functions = new HashMap<>();
	private final GoLanguage language;
	
	public TimeFunctionList(GoLanguage language){
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
		GoExpressionNode bodyNode = GoTimeNow.getTimeNow(language);
		String name = GoContext.lookupNodeInfo(bodyNode.getClass()).shortName();
		GoRootNode rootNode = new GoRootNode(language, new FrameDescriptor(), null, bodyNode, null, name);
		register(name,rootNode);
	}

	public GoFunction getFunction(String name){
		GoFunction result = functions.get(name);
		if(result == null){
			throw new GoException("Undefined: time."+name);
		}
		return result;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this;
	}

}
