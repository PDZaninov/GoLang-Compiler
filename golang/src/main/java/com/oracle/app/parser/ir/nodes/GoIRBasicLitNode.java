package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.StringEscape;

/**
 * Abstract BasicLiteral node class for any basic type node.
 * Create basic IR nodes using the static createBasicLit method so that the 
 * type handling is dealt with early on. When creating Truffle Nodes the type can
 * be created in one line.
 * Missing type:
 * 	FLOAT
 * 	IMAG
 * 	CHAR
 * @author Trevor
 *
 */
public abstract class GoIRBasicLitNode extends GoBaseIRNode {

	public GoIRBasicLitNode() {
		super("Basic Lit Node");
	}
	
	@Override
	public void setChildParent() {
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}

	public static GoIRBasicLitNode createBasicLit(String name, String value){
		switch(name){
		case "INT":
			return new GoIRIntNode(value);
		case "STRING":
			return new GoIRStringNode(value);
		default:
			System.out.println("Unimplemented Basic Lit type");
			return null;
		}
	
	}
	
	public static class GoIRIntNode extends GoIRBasicLitNode{
		
		private int value;
		
		public GoIRIntNode(String value) {
			this.value = Integer.parseInt(value);
		}
		
		public GoIRIntNode(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}

		@Override
		public Object accept(GoIRVisitor visitor) {
			return visitor.visitIRIntNode(this);
		}
		
	}
	
	public static class GoIRStringNode extends GoIRBasicLitNode{
		private String value;
		public GoIRStringNode(String value) {
			if(value.length() > 2){
				value = value.substring(2, value.length()-2);
				value = value.replace("\\\\", "\\");
			
				value = StringEscape.unescape_perl_string(value);
			}
			this.value = value;
		}

		public String getValue(){
			return value;
		}
		
		@Override
		public Object accept(GoIRVisitor visitor) {
			return visitor.visitIRStringNode(this);
		}
		
	}
	
}
