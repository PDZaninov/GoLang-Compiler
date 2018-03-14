package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;


public abstract class GoArray extends GoExpressionNode {
	protected int length;
	
	abstract public Object readArray(int index);
	
	@Override
	public String toString() {
		return "GoArray [length=" + length + "]";
	}

	abstract public int len(GoArray a);

	//abstract public void setArray(int index, int value);
	
	@Override
	abstract public GoArray executeGoArray(VirtualFrame virtualFrame);
	
	@Override
	abstract public GoArray executeGeneric(VirtualFrame frame);
	


}
