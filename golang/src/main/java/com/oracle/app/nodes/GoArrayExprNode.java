package com.oracle.app.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoArrayExprNode extends GoExpressionNode {

	@Children final private GoExpressionNode[] children;
	
	public GoArrayExprNode(GoExpressionNode[] children) {
		this.children = children;
	}
	
	public GoExpressionNode[] getArguments(){
		return children;
	}
	
	public int getSize() {
		return children.length;
	}

	public int getSize(){
		return children.length;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		for(GoExpressionNode child : children){
			child.executeGeneric(frame);
		}
		return null;
	}

	/*
	 * Called by composite lit node. Gathers the results to initialize a non primitive type.
	 */
	public Object[] gatherResults(VirtualFrame frame){
		Object[] results = new Object[children.length];
		for(int i = 0; i < results.length; i++){
			results[i] = children[i].executeGeneric(frame);
		}
		return results;
	}
}
