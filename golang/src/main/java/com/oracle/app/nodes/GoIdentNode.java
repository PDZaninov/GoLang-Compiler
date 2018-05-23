package com.oracle.app.nodes;

import com.oracle.app.GoException;
import com.oracle.truffle.api.frame.VirtualFrame;

/*
 * Technically if an ident shows up in runtime, that is an undefined variable
 */
public class GoIdentNode extends GoExpressionNode{
	
	String name;
	@Child private GoExpressionNode child;
	
	public GoIdentNode(String name, GoExpressionNode child) {
		this.name = name;
		this.child = child;
	}

	public void setChild(GoExpressionNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	/**
	 * Child only executes when it has a function declared in it.
	 */
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(child != null) {
			return child.executeGeneric(frame);
		}
		throw new GoException("Undefined: "+ name);
	}
}
