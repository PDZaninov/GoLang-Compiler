package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRInvokeNode extends GoBaseIRNode implements GoIRVisitable {

	GoBaseIRNode functionNode;
	GoIRArrayListExprNode argumentNodes;
	String lparen;
	String ellipsis;
	String rparen;
	
	public GoIRInvokeNode(GoBaseIRNode functionNode, GoIRArrayListExprNode argumentNodes,String lparen,String ellipsis,String rparen) {
		super("Call Expr (Invoke)");
		this.functionNode = functionNode;
		this.argumentNodes = argumentNodes;
		this.lparen = lparen;
		this.ellipsis = ellipsis;
		this.rparen = rparen;
	}
	
	public GoBaseIRNode getFunctionNode() {
		return functionNode;
	}
	
	/*
	 * Maybe not needed
	 */
	public GoIRArrayListExprNode getArgumentNode(){
		return argumentNodes;
	}
	
	public int getArgumentsSize(){
		return argumentNodes.getSize();
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitInvoke(this); 
	}

}
