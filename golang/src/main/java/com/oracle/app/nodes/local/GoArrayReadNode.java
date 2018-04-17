package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoArrayLikeTypes;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class GoArrayReadNode extends GoExpressionNode {

	protected GoExpressionNode expr;
	protected GoExpressionNode index;
	
	public GoArrayReadNode(GoExpressionNode expr, GoExpressionNode index){
		this.expr = expr;
		this.index = index;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		int i = -1;
		try {
			i = index.executeInteger(frame);
		} catch (UnexpectedResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GoArrayLikeTypes array = (GoArrayLikeTypes) expr.executeGeneric(frame);
		return array.readArray(frame, i);
	}

}
