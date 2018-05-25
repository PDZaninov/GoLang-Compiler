package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFuncTypeNode extends GoBaseIRNode {
	private GoBaseIRNode params;
	private GoBaseIRNode results;
	private String funcpos;
	
	public GoIRFuncTypeNode(GoBaseIRNode params, GoBaseIRNode results, String funcpos) {
		super("FuncType");
		this.params = params;
		this.results = results;
		this.funcpos = funcpos;
	}

	public int getFuncLinePos(){
		String[] split = funcpos.split(":");
		return Integer.parseInt(split[1]);
	}
	
	public int getFuncStartColumn(){
		String[] split = funcpos.split(":");
		return Integer.parseInt(split[2]);
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
