package com.oracle.app.nodes;

import com.oracle.app.nodes.GoBasicNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node.Children;

public class GoBasicNode extends GoExpressionNode{
	String name;
	String[] attr = new String[25];
	public GoBasicNode parent;
	@Children private final GoBasicNode[] children = new GoBasicNode[7];
	
	public GoBasicNode(String named) {
		name = named;
		
	}
	
	
	public void executeVoid(VirtualFrame frame){
		printSelf(0);
		System.out.println("Void\n");
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
	
	public void printSelf(int spacing) {
		//System.out.print(spacing);
		Spacing(spacing);
		
		System.out.println("Name: " + name);
		if(name != "root") {
			Spacing(spacing);
			System.out.println("Parent: " + parent.name);
		}
/*			for(int x = 0; x < children.length; x++) {
			if(children[x] == null)
				break;
			Spacing(spacing);
			System.out.println("Children: " + children[x].name);
		}*/
		for(int x = 0; x < attr.length; x++) {
			if(attr[x] == null)
				break;
			Spacing(spacing);
			System.out.println("Attr: " + attr[x]);
		}
	}
	public void Spacing(int spacing) {
		for(int x = 0; x < spacing; x++) {
			System.out.print(" . ");
		}
	}
	
	public void printTree(GoBasicNode root, int spacing) {
		root.printSelf(spacing);
		spacing += 1;
		for(int x = 0; x < root.children.length; x++) {
			if(root.children[x] != null) {
				root.printTree(root.children[x], spacing);
				
			}else {
				break;
			}
		}
	}


	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		printSelf(0);
		System.out.println("Generic\n");
		for(GoBasicNode child : children){
			if(child != null){
				child.executeGeneric(frame);
		
			}
		}
		return null;
	}
}
	