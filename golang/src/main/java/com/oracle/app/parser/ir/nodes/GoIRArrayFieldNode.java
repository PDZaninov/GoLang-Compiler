package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayFieldNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRArrayFieldNode(ArrayList<GoBaseIRNode> children) {
		super("ArrayList Expression Node");
		this.children = children;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitArrayField(this);
	}

	public int  getSize(){
		return children.size();
	}

}
