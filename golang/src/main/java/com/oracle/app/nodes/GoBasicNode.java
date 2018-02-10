package com.oracle.app.nodes;

import com.oracle.app.nodes.GoBasicNode;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node.Children;

public class GoBasicNode extends GoExpressionNode{
	String name;
	String[] attr = new String[25];
	public GoBasicNode parent;
	@Children final private GoStatementNode[] children;
	
	public GoBasicNode(String named, GoStatementNode[] children) {
		name = named;
		this.children = children;
	}
	
	
	public void executeVoid(VirtualFrame frame){
		CompilerAsserts.compilationConstant(children.length);

        for (GoStatementNode statement : children) {
            statement.executeVoid(frame);
        }
	}
	
	public void addData(String someData) {
		for(int i = 0; i < attr.length; i++) {
			if(attr[i] == null) {
				attr[i] = someData;
				break;
			}
		}
	}
	public GoBasicNode setParent(GoBasicNode someNode) {
		parent = someNode;
		return parent;
	}

	public int addChildren(GoBasicNode theChosenOne) {
		int i;
		for(i = 0; i < children.length;i++) {
			if(children[i]== null) {
				children[i] = theChosenOne;
				break;
			}

		}
		theChosenOne.setParent(this);
		return i;
	}
	
	public void trufflechildren(GoBasicNode m) {
		this.insert(m);
	}
	
	public void Spacing(int spacing) {
		for(int x = 0; x < spacing; x++) {
			System.out.print(" . ");
		}
	}
	



	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return "string " + this.name;
	}
}
	