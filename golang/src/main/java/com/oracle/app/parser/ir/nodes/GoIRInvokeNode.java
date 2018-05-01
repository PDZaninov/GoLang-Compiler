package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;
import java.util.Map;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.GoTruffle;

public class GoIRInvokeNode<LexicalScope> extends GoBaseIRNode implements GoIRVisitable {

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
	
	public int getEndPos(){
		int endpos = Integer.parseInt(rparen.split(":")[2]);
		return endpos;
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
