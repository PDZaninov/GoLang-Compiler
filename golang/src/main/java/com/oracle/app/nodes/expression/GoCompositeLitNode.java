package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoCompositeLitNode extends GoExpressionNode {

	private GoNonPrimitiveType type;
	private GoArrayExprNode elts;
	
	public GoCompositeLitNode(GoNonPrimitiveType type, GoArrayExprNode elts) {
		this.type = type;
		this.elts = elts;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return type.fillCompositeFields(frame, elts);
	}

}
