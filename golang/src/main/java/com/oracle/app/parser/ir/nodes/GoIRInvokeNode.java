package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;

public class GoIRInvokeNode extends GoBaseIRNode {

	GoBaseIRNode[] argumentNodes;
	GoIRGenericDispatchNode dispatchNode;
	
	public GoIRInvokeNode(GoBaseIRNode functionNode, GoBaseIRNode[] argumentNodes) {
		super("Call Expr (Invoke)");
		this.setChild(functionNode);
		this.argumentNodes = argumentNodes;
		this.dispatchNode = new GoIRGenericDispatchNode();
	}
	

	@Override
	public void setChildParent() {
		//TODO 
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
