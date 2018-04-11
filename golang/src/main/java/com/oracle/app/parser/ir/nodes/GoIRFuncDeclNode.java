package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

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
	public Object accept(GoIRVisitor visitor){
		return visitor.visitFuncDecl(this);
	}
	
	public GoBaseIRNode getName(){
		return name;
	}
	
	@Override
	public String getIdentifier() {
		return name.getIdentifier();
	}
	
	public GoBaseIRNode getReceiver(){
		if(receiver != null){
			return receiver;
		}
		return null;
	}
	
	public GoBaseIRNode getBody(){
		if(body != null){
			return body;
		}
		return null;
	}
	
	public GoBaseIRNode getType(){
		return type;
	}

}
