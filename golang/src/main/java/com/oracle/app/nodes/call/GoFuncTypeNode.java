package com.oracle.app.nodes.call;

import java.util.LinkedHashMap;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFuncTypeNode extends GoExpressionNode{

	GoArrayExprNode params;
	String[] results;
	
	String type;
	
	public GoFuncTypeNode(GoArrayExprNode params, String[] results2) {
		this.params = params;
		this.results = results2;
	}
	
	public GoArrayExprNode getParams() {
		return params;
	}
	
	public String[] getResults() {
		return results;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(params == null) {
			return null;
		}
		return params.executeGeneric(frame);
	}
}