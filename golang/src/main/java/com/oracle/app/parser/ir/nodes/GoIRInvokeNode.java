package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRInvokeNode extends GoBaseIRNode implements GoIRVisitable {

	GoBaseIRNode functionNode;
	ArrayList<GoBaseIRNode> argumentNodes;
	GoIRGenericDispatchNode dispatchNode;
	
	public GoIRInvokeNode(GoBaseIRNode functionNode, ArrayList<GoBaseIRNode> argumentNodes) {
		super("Call Expr (Invoke)");
		this.functionNode = functionNode;
		this.argumentNodes = argumentNodes;
		this.dispatchNode = new GoIRGenericDispatchNode();
		setChildParent();
	}
	
	public GoBaseIRNode getFunctionNode() {
		return functionNode;
	}
	

	@Override
	public void setChildParent() {
		this.functionNode.setParent(this);
		
		for(int i = 0; i < argumentNodes.size(); i++)
			this.argumentNodes.get(i).setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}
	
	@Override
	public void accept(GoIRVisitor visitor) { 
		visitor.visitInvoke(this); 
	}

}
