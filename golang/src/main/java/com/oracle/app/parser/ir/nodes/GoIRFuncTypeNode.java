package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFuncTypeNode extends GoBaseIRNode {
	private GoBaseIRNode params;
	private GoBaseIRNode results;
	
	public GoIRFuncTypeNode(GoBaseIRNode params, GoBaseIRNode results) {
		super("FuncType");
		this.params = params;
		this.results = results;
	}

	public GoBaseIRNode getParams() {
		return params;
	}
	
	public GoBaseIRNode getResults() {
		return results;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitFuncType(this);
	}

}
