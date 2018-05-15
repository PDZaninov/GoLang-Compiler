package com.oracle.app.nodes.call;

import java.util.Arrays;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFuncTypeNode extends GoExpressionNode{

	GoArrayExprNode params;
	GoArrayExprNode results;
	
	String type;
	
	public GoFuncTypeNode(GoArrayExprNode params, GoArrayExprNode results) {
		this.params = params;
		this.results = results;
	}
	
	public void appendReceiverStruct(GoWriteLocalVariableNode receiver){
		GoExpressionNode[] newparams;
		if(params != null){
			int oldsize = params.getSize();
			newparams = Arrays.copyOf(params.getArguments(), oldsize + 1);
			newparams[oldsize] = receiver;
		}
		else{
			newparams = new GoExpressionNode[1];
			newparams[0] = receiver;
		}
		params = new GoArrayExprNode(newparams);
	}
	
	public GoArrayExprNode getParams() {
		return params;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(params == null) {
			return null;
		}
		return params.executeGeneric(frame);
	}
}