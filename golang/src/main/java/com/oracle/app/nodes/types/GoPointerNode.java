package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoPointerNode extends GoExpressionNode{

	private int ptr;
	
	public GoPointerNode(int ptr){
		this.ptr = ptr;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}

}
