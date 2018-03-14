package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayTypeNode extends GoBaseIRNode{
	GoBaseIRNode len;
	GoBaseIRNode type;
	
	public GoIRArrayTypeNode(GoBaseIRNode length, GoBaseIRNode type) {
		super("IR Array Type Node");
		len = length;
		this.type = type;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		len.setParent(this);
		type.setParent(this);
		
	}
	
	public GoBaseIRNode getLength(){
		return len;
	}
	
	public GoBaseIRNode getType(){
		return type;
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		ArrayList<GoBaseIRNode> m = new ArrayList<GoBaseIRNode>();;
		m.add(len);
		m.add(type);
		return m;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitArrayType(this);
	}

}
