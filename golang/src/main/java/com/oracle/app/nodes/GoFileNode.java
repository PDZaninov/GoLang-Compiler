package com.oracle.app.nodes;

import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

/*
 * File node is at the top of every Go program and contains the info for
 * the entire code in that file
 */
@NodeInfo(language = "Go", description = "The base node for every Go file")
public class GoFileNode extends GoExpressionNode{
	@Child private GoDeclNode bodyNode; 
	
	public GoFileNode(GoDeclNode bodyNode){
		this.bodyNode = bodyNode;
	}
	
	public void executeVoid(VirtualFrame frame){
		bodyNode.executeVoid(frame);
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
