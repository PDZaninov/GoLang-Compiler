package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;



public abstract class GoArray extends GoExpressionNode {
	abstract public Object readArray(int index);
	
	@Override
	abstract public GoArray executeGoArray(VirtualFrame virtualFrame);
	
	@Override
	abstract public GoArray executeGeneric(VirtualFrame frame);
	


}
