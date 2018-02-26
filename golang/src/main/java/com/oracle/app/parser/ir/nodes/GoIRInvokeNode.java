package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRInvokeNode extends GoBaseIRNode implements GoIRVisitable {

	GoBaseIRNode functionNode;
	GoIRArrayListExprNode argumentNodes;
	GoIRGenericDispatchNode dispatchNode;
	
	public GoIRInvokeNode(GoBaseIRNode functionNode, GoIRArrayListExprNode argumentNodes) {
		super("Call Expr (Invoke)");
		this.functionNode = functionNode;
		this.argumentNodes = argumentNodes;
		this.dispatchNode = new GoIRGenericDispatchNode();
		setChildParent();
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
	
	
	public GoIRGenericDispatchNode getDispatchNode(){
		return dispatchNode;
	}
	
	//TODO
	@Override
	public void setChildParent() {
		functionNode.setParent(this);
		argumentNodes.setParent(this);
		dispatchNode.setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		System.out.println("Invoke Node needs getChildren Function");
		return null;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitInvoke(this); 
	}

}
