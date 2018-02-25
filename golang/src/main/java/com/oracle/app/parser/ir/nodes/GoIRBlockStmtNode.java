package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBlockStmtNode extends GoBaseIRNode {

	private GoBaseIRNode body;
	
	public GoIRBlockStmtNode(GoBaseIRNode body) {
		super("Block Statement Node");
		this.body = body;
		setChildParent();
	}
	
	public GoBaseIRNode getChild() {
		return body;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitBlockStmt(this);
	}
	
	@Override
	public void setChildParent() {
		body.setParent(this);

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
