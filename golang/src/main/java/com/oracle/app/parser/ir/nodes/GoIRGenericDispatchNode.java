package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;

public class GoIRGenericDispatchNode extends GoBaseIRNode {
	
	public GoIRGenericDispatchNode() {
		super("Generic Dispatch");
	}

	@Override
	public void setChildParent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void accept(GoVisitor visitor) { 
		visitor.visitGenericDispatch(this); 
	}
	
	@Override
	public void accept(GoTruffle visitor) { 
		visitor.visitGenericDispatch(this); 
	}

}
