package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoIdentNode extends GoExpressionNode{
	
	String name;
	@Child private GoExpressionNode child;
	
	private final ContextReference<GoContext> reference;
	
	public GoIdentNode(GoLanguage language, String name, GoExpressionNode child) {
		this.name = name;
		this.child = child;
		reference = language.getContextReference();
	}

	@Override
	public String toString() {
		return name;
	}
	
	public GoFunction getFunction(){
		return reference.get().getFunctionRegistry().lookup(name, true);
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(child != null) {
			return child.executeGeneric(frame);
		}
		return null;
	}
	
	

}
