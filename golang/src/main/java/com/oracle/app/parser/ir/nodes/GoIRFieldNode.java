package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFieldNode extends GoBaseIRNode {

	private GoBaseIRNode ident;//ident type, ident name for var
	private GoBaseIRNode type;// ident type, ident of type for var
	
	public GoIRFieldNode(String name,GoBaseIRNode ident, GoBaseIRNode type) {
		super(name);
		this.ident = ident;
		this.type = type;
	}

	public GoIRFieldNode(String name, GoBaseIRNode type) {
		super(name);
		this.type = type;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitField(this);
	}

}