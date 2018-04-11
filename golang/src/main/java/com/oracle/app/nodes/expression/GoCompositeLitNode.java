package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoCompositeLitNode extends GoExpressionNode {

	private GoArrayTypeExprNode type;
	private GoArrayExprNode elts;
	
	public GoCompositeLitNode(GoArrayTypeExprNode type, GoArrayExprNode elts) {
		this.type = type;
		this.elts = elts;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return type.fillCompositeFields(frame, elts);
	}

}
