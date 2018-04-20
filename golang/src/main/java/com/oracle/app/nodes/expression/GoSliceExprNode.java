package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.GoArrayLikeTypes;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

/**
 * Creates a {@link GoSlice} node from either a {@link GoSlice} or a {@link GoArray}
 * @author Trevor
 *
 */
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
		GoArrayLikeTypes array = (GoArrayLikeTypes) expr.executeGeneric(frame);
		FrameSlot slot = expr.getSlot();
		if(array instanceof GoSlice){
			slot = ((GoSlice) array).getSlot();
		}
		int lowerbound = 0;
		int highbound = 0;
		int maxsize = 0;
		if(low == null){
			lowerbound = array.lowerBound();
		}
		else{
			try {
				lowerbound = low.executeInteger(frame) + array.lowerBound();
			} catch (UnexpectedResultException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(high == null){
			highbound = array.cap();
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
			maxsize = array.cap();
		}
		else{
			try {
				maxsize = max.executeInteger(frame);
			} catch (UnexpectedResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//GoSlice slice = new GoSlice(slot,lowerbound,highbound,maxsize);
		//slice.setType(array.getType());
		return null;
	}

}
