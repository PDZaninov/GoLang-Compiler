package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRStarNode extends GoBaseIRNode {

	GoBaseIRNode expr;
	String star;
	
	public GoIRStarNode(GoBaseIRNode expr, String star) {
		super("Star Node");
		this.expr = expr;
		this.star = star;
	}

	public GoBaseIRNode getChild(){
		return expr;
	}	
	
	@Override
	public String getIdentifier() {
		return expr.getIdentifier();
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visitStarNode(this);
	}

}
