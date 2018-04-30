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
		GoArrayLikeTypes array = (GoArrayLikeTypes) expr.executeGeneric(frame);
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
				e.printStackTrace();
			}
		}
		if(high == null){
			highbound = array.len() + array.lowerBound();
		}
		else{
			try {
				highbound = high.executeInteger(frame) + array.lowerBound();
			} catch (UnexpectedResultException e) {
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
				e.printStackTrace();
			}
		}
		GoSlice slice = GoSlice.createGoSlice(array,lowerbound,highbound,maxsize);
		return slice;
	}

}
