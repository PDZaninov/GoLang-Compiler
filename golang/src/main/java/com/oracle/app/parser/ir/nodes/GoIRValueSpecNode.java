package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRValueSpecNode extends GoBaseIRNode {

	private GoIRArrayListExprNode names;
	private GoBaseIRNode type;
	private GoIRArrayListExprNode expr;
	
	public GoIRValueSpecNode(GoIRArrayListExprNode names, GoBaseIRNode type, GoIRArrayListExprNode expr) {
		super("Value Spec Node");
		this.names = names;
		this.type = type;
		this.expr = expr;
	}

	public GoIRArrayListExprNode getNames(){
		return names;
	}
	
	public GoIRArrayListExprNode getExpr(){
		return expr;
	}
	
	public GoBaseIRNode getType(){
		return type;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return null;
		//return visitor.visitValueSpec(this);
	}

}
