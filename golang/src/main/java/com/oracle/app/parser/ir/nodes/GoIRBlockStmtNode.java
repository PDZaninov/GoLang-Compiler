package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBlockStmtNode extends GoBaseIRNode {

	GoBaseIRNode body;
	String lbrace;
	String rbrace;
	
	public GoIRBlockStmtNode(GoBaseIRNode body,String lbrace, String rbrace) {
		super("Block Statement Node");
		this.body = body;
		this.lbrace = lbrace;
		this.rbrace = rbrace;
	}
	
	public GoBaseIRNode getChild() {
		return body;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitBlockStmt(this);
	}

}
