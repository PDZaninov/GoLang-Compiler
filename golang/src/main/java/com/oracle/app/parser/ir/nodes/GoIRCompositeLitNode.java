package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRCompositeLitNode extends GoBaseIRNode {

	GoBaseIRNode expr;
	String lbrace;
	GoIRArrayListExprNode elts;
	String rbrace;
	
	public GoIRCompositeLitNode(GoBaseIRNode expr, String lbrace, GoIRArrayListExprNode elts, String rbrace) {
		super("Composite Lit Node");
		this.expr = expr;
		this.lbrace = lbrace;
		this.elts = elts;
		this.rbrace = rbrace;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visit(this);
	}

	public GoBaseIRNode getExpr() {
		return expr;
	}

	public int getLBraceLineNum() {
		String[] split = lbrace.split(":");
		return Integer.parseInt(split[1]);
	}
	
	public int getLBraceStartColumn(){
		String[] split = lbrace.split(":");
		return Integer.parseInt(split[2]);
	}

	public GoIRArrayListExprNode getElts() {
		return elts;
	}

	public int getRBraceEndColumn() {
		String[] split = rbrace.split(":");
		return Integer.parseInt(split[2]);
	}

}
