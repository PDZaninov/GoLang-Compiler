package com.oracle.app.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoArrayExprNode extends GoExpressionNode {

	@Children final private GoExpressionNode[] children;
	
	public GoArrayExprNode(GoExpressionNode[] children) {
		this.children = children;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		for(GoExpressionNode child : children){
			child.executeGeneric(frame);
		}
		return null;
	}

}
