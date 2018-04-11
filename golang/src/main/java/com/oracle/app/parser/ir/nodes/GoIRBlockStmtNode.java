package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBlockStmtNode extends GoBaseIRNode {

	GoBaseIRNode body;
	String lbrace;
	String rbrace;
	
	public GoIRBlockStmtNode(GoBaseIRNode body,String lbrace, String rbrace) {
		super("Block Statement Node");
		this.body = body;
		this.lbrace = lbrace;
		this.rbrace = rbrace;
	}
	
	public GoBaseIRNode getChild() {
		return body;
	}
	
	public int getLbrace(){
		String[] split = lbrace.split(":");
		int lbraceindex = Integer.parseInt(split[2]);
		return lbraceindex;
	}

	public int getRbrace(){
		String[] split = rbrace.split(":");
		int endindex = Integer.parseInt(split[2]);
		return endindex;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitBlockStmt(this);
	}

}
