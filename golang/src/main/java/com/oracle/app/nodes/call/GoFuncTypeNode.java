package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFuncTypeNode extends GoExpressionNode{

	GoArrayExprNode params;
	GoArrayExprNode results;
	
	String type;
	
	public GoFuncTypeNode(GoArrayExprNode params, GoArrayExprNode results) {
		this.params = params;
		this.results = results;
	}
	
	public GoArrayExprNode getParams() {
		return params;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return params.executeGeneric(frame);
	}
}