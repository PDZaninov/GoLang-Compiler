package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoSliceNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class GoSliceExprNode extends GoExpressionNode {

	GoReadLocalVariableNode expr;
	GoExpressionNode low;
	GoExpressionNode high;
	GoExpressionNode max;
	
	public GoSliceExprNode(GoReadLocalVariableNode expr, GoExpressionNode low, GoExpressionNode high, GoExpressionNode max) {
		this.expr = expr;
		this.low = low;
		this.high = high;
		this.max = max;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		GoArray array = (GoArray) expr.executeGeneric(frame);
		FrameSlot slot = expr.getSlot();
		int lowerbound = 0;
		int highbound = 0;
		int maxsize = 0;
		try {
			lowerbound = low.executeInteger(frame);
		} catch (UnexpectedResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(high == null){
			highbound = array.len();
		}
		else{
			try {
				highbound = high.executeInteger(frame);
			} catch (UnexpectedResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(max == null){
			maxsize = array.len() - lowerbound;
		}
		else{
			try {
				maxsize = max.executeInteger(frame);
			} catch (UnexpectedResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		GoSliceNode slice = new GoSliceNode(slot,lowerbound,highbound,maxsize);
		slice.setType(array.getType());
		return slice;
	}

}
