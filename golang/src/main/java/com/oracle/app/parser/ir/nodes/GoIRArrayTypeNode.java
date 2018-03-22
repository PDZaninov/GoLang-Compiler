package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayTypeNode extends GoBaseIRNode{
	GoBaseIRNode len;
	GoBaseIRNode type;
	String source;
	
	boolean isSlice = false;
	
	public GoIRArrayTypeNode(GoBaseIRNode length, GoBaseIRNode type, String source) {
		super("IR Array Type Node");
		len = length;
		this.type = type;
		this.source = source;
	}
	
	public GoIRArrayTypeNode(GoBaseIRNode type, boolean isSlice, String source) {
		super("IR Array Type Node");
		this.type = type;
		this.isSlice = true;
		this.source = source;
	}
	
	public String getSource(){
		return source;
	}
	
	public GoBaseIRNode getLength(){
		return len;
	}
	
	public GoBaseIRNode getType(){
		return type;
	}
	
	public boolean getIsSlice() {
		return isSlice;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitArrayType(this);
	}

}
