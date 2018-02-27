package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRDeclStmtNode extends GoBaseIRNode {

	GoBaseIRNode decl;
	
	public GoIRDeclStmtNode(GoBaseIRNode decl) {
		super("DeclStmt Node");
		this.decl = decl;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		decl.setParent(this);
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitDeclStmt(this);
	}

	public GoBaseIRNode getChild() {
		return decl;
	}

}
