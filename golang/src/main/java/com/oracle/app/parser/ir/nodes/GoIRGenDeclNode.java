package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRGenDeclNode extends GoBaseIRNode {

	private String token;
	private GoIRArrayListExprNode children;
	
	public GoIRGenDeclNode(String token, GoIRArrayListExprNode children) {
		super("GenDecl Node");
		this.token = token;
		this.children = children;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		children.setParent(this);
	}
	
	public GoIRArrayListExprNode getChild(){
		return children;
	}
	
	public String getToken(){
		return token;
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitGenDecl(this);
	}

}
