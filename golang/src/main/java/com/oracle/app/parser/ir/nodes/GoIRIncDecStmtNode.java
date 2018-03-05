package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIncDecStmtNode extends GoBaseIRNode {
	
	private String op;
	
	private GoBaseIRNode child;
	
	public GoIRIncDecStmtNode(String op, GoBaseIRNode child) {
		super("IncDec");
		this.op = op;
		this.child = child;
	}
	
	@Override
	public void setChildParent() {
		child.setParent(this);
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public GoBaseIRNode getChild() { return child; }
	
	public String getOp() {
		return op;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitIncDecStmt(this); 
	}

}
