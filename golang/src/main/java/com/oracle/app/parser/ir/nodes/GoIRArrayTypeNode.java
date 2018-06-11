package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayTypeNode extends GoIRTypes {
	GoBaseIRNode len;
	GoBaseIRNode type;
	String source;
	
	public GoIRArrayTypeNode(GoBaseIRNode length, GoBaseIRNode type, String source) {
		super("IR Array Type Node", type.getIdentifier(), type.getIdentifier());
		len = length;
		this.type = type;
		this.source = source;
	}
	
	public int getLBrackStartLine(){
		String[] split = source.split(":");
		return Integer.parseInt(split[1]);
	}
	
	public int getLBrackStartColumn(){
		String[] split = source.split(":");
		return Integer.parseInt(split[2]);
	}
	
	public GoBaseIRNode getLength(){
		return len;
	}
	
	public GoBaseIRNode getType(){
		return type;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitArrayType(this);
	}

}
