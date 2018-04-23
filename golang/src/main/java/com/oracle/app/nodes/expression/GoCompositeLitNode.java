package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoCompositeLitNode extends GoExpressionNode {

	private GoExpressionNode type;
	private GoArrayExprNode elts;
	
	public GoCompositeLitNode(GoExpressionNode type, GoArrayExprNode elts) {
		this.type = type;
		this.elts = elts;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(type == null){
			return elts.gatherResults(frame);
		}
		GoNonPrimitiveType result = (GoNonPrimitiveType) type.executeGeneric(frame);
		Object[] elements = elts.gatherResults(frame);
		return result.doCompositeLit(frame, elements);
	}

}
