package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

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
		setChildParent();
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
	public void setChildParent() {
		names.setParent(this);
		if(type != null){
			type.setParent(this);
		}
		if(expr != null){
			expr.setParent(this);
		}
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitValueSpec(this);
	}

}
