package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class GoBlockNode extends GoStatementNode {

	@Children private final GoStatementNode[] bodyNodes;
	
	public GoBlockNode(GoStatementNode[] bodyNodes){
		this.bodyNodes = bodyNodes;
	}
	
	@Override
	@ExplodeLoop
	public void executeVoid(VirtualFrame frame) {
		// TODO Auto-generated method stub
		for(GoStatementNode statement : bodyNodes){
			statement.executeVoid(frame);
		}
	}

}
