package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRInvokeNode extends GoBaseIRNode {

	GoBaseIRNode functionNode;
	GoIRArrayListExprNode argumentNodes;
	String lparen;
	String ellipsis;
	String rparen;
	int assignLen = 0;
	
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
	
	public int getEndPos(){
		int endpos = Integer.parseInt(rparen.split(":")[2]);
		return endpos;
	}

	public void incAssignLen() {
		assignLen++;
	}
	
	public int getAssignLen() {
		return assignLen;
	}
	
	public int getPositionOfRightParen(){
		String[] split = rparen.split(":");
		return Integer.parseInt(split[2]);
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
