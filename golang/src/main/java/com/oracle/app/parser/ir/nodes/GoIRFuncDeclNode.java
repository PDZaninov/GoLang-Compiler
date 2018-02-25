package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

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
		ArrayList<GoBaseIRNode> list = new ArrayList<GoBaseIRNode>();
		list.add(receiver);
		list.add(name);
		list.add(type);
		list.add(body);
		return list;
	}

}
