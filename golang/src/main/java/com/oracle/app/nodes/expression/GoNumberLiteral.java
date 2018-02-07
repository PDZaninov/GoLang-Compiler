package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoNumberLiteral extends GoExpressionNode{
	private final int num;
	
	public GoNumberLiteral(int num){
		this.num = num;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return num;
	}
	
	
}
