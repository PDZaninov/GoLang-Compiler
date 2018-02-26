package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRStmtNode extends GoBaseIRNode {
	
	private GoBaseIRNode child;
	
	public GoIRStmtNode(GoBaseIRNode child) {
		super("Stmt");
		this.child = child;
		setChildParent();
	}
	
	@Override
	public void setChildParent() {
		if(child != null)
			child.setParent(this);
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}
	
	public GoBaseIRNode getChild() {
		return child;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitStmt(this); 
	}
	
	
}
