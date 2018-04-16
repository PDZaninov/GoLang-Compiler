package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFuncTypeNode extends GoBaseIRNode {
	private GoIRArrayListExprNode params;
	private GoIRArrayListExprNode results;
	
	public GoIRFuncTypeNode(GoIRArrayListExprNode params, GoIRArrayListExprNode results) {
		super("FuncType");
		this.params = params;
		this.results = results;
	}

	public GoIRArrayListExprNode getParams() {
		return params;
	}
	
	public GoIRArrayListExprNode getResults() {
		return results;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitFuncType(this);
	}

}
