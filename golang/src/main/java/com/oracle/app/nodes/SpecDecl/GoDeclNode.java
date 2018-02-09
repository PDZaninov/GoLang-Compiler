package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class GoDeclNode extends GoStatementNode {

	@Children private final GoStatementNode[] bodyNodes;
	
	public GoDeclNode(GoStatementNode[] bodyNodes){
		this.bodyNodes = bodyNodes;
	}

	@Override
	@ExplodeLoop
	public void executeVoid(VirtualFrame frame) {
		// TODO Auto-generated method stub
		for(GoStatementNode node : bodyNodes){
			node.executeVoid(frame);
		}
		
	}
}
