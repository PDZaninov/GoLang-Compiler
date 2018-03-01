package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBranchStmtNode extends GoBaseIRNode {
	
	private String type;
	
	private String ident;
	
	private GoBaseIRNode child;
	
	public GoIRBranchStmtNode(String type, String ident, GoBaseIRNode child) {
		super("BranchStmt");
		this.type = type;
		this.ident = ident;
		this.child = child;
	}
	
	public String getType() {
		return type;
	}
	
	public String getIdent() {
		return ident;
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
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitBranchStmt(this); 
	}

}
