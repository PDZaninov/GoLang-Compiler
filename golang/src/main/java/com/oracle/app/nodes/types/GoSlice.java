package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;


public abstract class GoSlice extends GoExpressionNode {
	protected int length;
	protected int capacity;
	
	abstract public Object readSlice(int index);
	
	@Override
	public String toString() {
		return "GoSlice [length=" + length + "]";
	}

	abstract public int len(GoSlice a);

	//abstract public void setArray(int index, int value);
	
	@Override
	abstract public GoSlice executeGoSlice(VirtualFrame virtualFrame);
	
	@Override
	abstract public GoSlice executeGeneric(VirtualFrame frame);
	


}
