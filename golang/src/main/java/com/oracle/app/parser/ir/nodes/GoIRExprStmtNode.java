package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRExprStmtNode extends GoBaseIRNode {

	GoBaseIRNode expression;
	String lparen;
	String rparen;
	
	public GoIRExprStmtNode(GoBaseIRNode expression, String lparen, String rparen) {
		super("Parenthesis Expression");
		this.expression = expression;
		this.lparen = lparen;
		this.rparen = rparen;
	}
	public GoIRExprStmtNode(GoBaseIRNode expression) {
		super("Stand alone Expression");
		this.expression = expression;
	}
	
	public GoBaseIRNode getChild() {
		return expression;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitExprStmt(this);
	}

}
