package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;

public class GoIRInvokeNode extends GoBaseIRNode {

	GoBaseIRNode functionNode;
	GoBaseIRNode[] argumentNodes;
	GoIRGenericDispatchNode dispatchNode;
	
	public GoIRInvokeNode(GoBaseIRNode functionNode, GoBaseIRNode[] argumentNodes) {
		super("Call Expr (Invoke)");
		this.functionNode = functionNode;
		this.argumentNodes = argumentNodes;
		this.dispatchNode = new GoIRGenericDispatchNode();
		setChildParent();
	}
	

	@Override
	public void setChildParent() {
		this.functionNode.setParent(this);
		
		for(int i = 0; i < argumentNodes.length; i++)
			this.argumentNodes[i].setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}
	
	@Override
	public void accept(GoVisitor visitor) { 
		visitor.visitInvoke(this); 
	}
	
	@Override
	public void accept(GoTruffle visitor) { 
		visitor.visitInvoke(this); 
	}

}
