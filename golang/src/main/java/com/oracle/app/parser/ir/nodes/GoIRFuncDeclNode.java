package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;

public class GoIRFuncDeclNode extends GoBaseIRNode {
	GoBaseIRNode receiver;
	GoBaseIRNode name;
	GoBaseIRNode type;
	GoBaseIRNode body;

	public GoIRFuncDeclNode(GoBaseIRNode receiver, GoBaseIRNode name, GoBaseIRNode type, GoBaseIRNode body) {
		super("Function Declaration Node");
		this.receiver = receiver;
		this.name = name;
		this.type = type;
		this.body = body;
	}

	@Override
	public void setChildParent() {
		
		if(receiver != null){
			receiver.setParent(this);
		}
		name.setParent(this);
		type.setParent(this);
		if(body != null){
			body.setParent(this);
		}

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
