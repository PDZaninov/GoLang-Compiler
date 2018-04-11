package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRDeclStmtNode extends GoBaseIRNode {

	GoBaseIRNode decl;
	
	public GoIRDeclStmtNode(GoBaseIRNode decl) {
		super("DeclStmt Node");
		this.decl = decl;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitDeclStmt(this);
	}

	public GoBaseIRNode getChild() {
		return decl;
	}

}
