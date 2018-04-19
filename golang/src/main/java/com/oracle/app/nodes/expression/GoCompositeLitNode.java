package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
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
		//Temporary workaround until I change arrays and slices and pointers
		//TO-DO REMOVE IF STATEMENTS
		if(type instanceof GoReadLocalVariableNode){
			GoStruct result = (GoStruct) type.executeGeneric(frame);
			return result.fillCompositeFields(frame,elts);
		}
		else{
			return ((GoArrayTypeExprNode) type).fillCompositeFields(frame, elts);
		}
	}

}
