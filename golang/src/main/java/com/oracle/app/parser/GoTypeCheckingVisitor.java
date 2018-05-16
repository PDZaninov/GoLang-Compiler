package com.oracle.app.parser;

import java.util.ArrayList;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.types.GoFloat32Node;
import com.oracle.app.nodes.types.GoFloat64Node;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoTruffle.TypeInfo;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldListNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldNode;
import com.oracle.app.parser.ir.nodes.GoIRFloat32Node;
import com.oracle.app.parser.ir.nodes.GoIRFloat64Node;
import com.oracle.app.parser.ir.nodes.GoIRFuncTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRReturnStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRStringNode;
import com.oracle.app.parser.ir.nodes.GoIRTypes;

public class GoTypeCheckingVisitor implements GoIRVisitor{
	

	public GoTypeCheckingVisitor() {
		
	}
	
	/* returns a string of its children types
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitReturnStmt(com.oracle.app.parser.ir.nodes.GoIRReturnStmtNode)
	 */
	public Object visitReturnStmt(GoIRReturnStmtNode node) {
		 	return node.getChild().accept(this);
	}
	
	/* returns a string of its return types
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitInvoke(com.oracle.app.parser.ir.nodes.GoIRInvokeNode)
	 */
	public Object visitInvoke(GoIRInvokeNode node){
			String b = "";
			
			GoIRFuncTypeNode t = GoTruffle.IRFunctions.get(node.getFunctionNode().getIdentifier());
			if(t != null) {
				b = (String) t.getResults().accept(this);
			}
			System.out.println("-here-" +b);
			return b;
	}	
	 

	/* returns a concatenation of types by "," from its children
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitArrayListExpr(com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode)
	 */
	public Object visitArrayListExpr(GoIRArrayListExprNode node) {
		String z = "";
		if(node != null) {
			ArrayList<GoBaseIRNode> children = node.getChildren();
			if(children != null) {
				for(int i = 0 ; i < children.size(); i ++) {
					
					z += children.get(i).accept(this);
					if(i < children.size() -1) {
						z += ",";
					}
				}
			}
		}

		return z;
	}
	
	/* Returns the arraylistexpr string of types
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitFieldList(com.oracle.app.parser.ir.nodes.GoIRFieldListNode)
	 */
	public Object visitFieldList(GoIRFieldListNode node){
		if(node!= null) {
			if(node.getFields()!= null) {
				return node.getFields().accept(this);
			}
		}
		return "";
	}
	
	/* Returns the type in the field
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitField(com.oracle.app.parser.ir.nodes.GoIRFieldNode)
	 */
	public Object visitField(GoIRFieldNode node)
	{
		return node.getType().getIdentifier();
	}
	
	/* returns type of variable
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitIdent(com.oracle.app.parser.ir.nodes.GoIRIdentNode)
	 */
	public Object visitIdent(GoIRIdentNode node){
		TypeInfo m = GoTruffle.lexicalscope.locals.get(node.getIdentifier());
		if(m != null) {
			return m.getType();
		}
		return "";
	}
	
	public Object visitArrayType(GoIRArrayTypeNode node){
		
		return node.getType().accept(this);
	}
	
	/* TODO idk if this doing something right
	 * I think I need to check if its a number and not a string
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitIndexNode(com.oracle.app.parser.ir.nodes.GoIRIndexNode)
	 */
	public Object visitIndexNode(GoIRIndexNode node){
		String type = (String) node.getIndex().accept(this);
		if(!(type.equalsIgnoreCase("int"))) {
			throw new GoException("non-integer slice index \""+type +"\"" );
		}
		return type;
	}
	public Object visitIRIntNode(GoIRIntNode node){
		return node.getType();
	}

	public Object visitIRFloat32Node(GoIRFloat32Node node) { 
		return node.getType();
	}

	public Object visitIRFloat64Node(GoIRFloat64Node node) { 
		return node.getType(); 
	}
	
	public Object visitIRStringNode(GoIRStringNode node){
		return node.getType();
	}
	
	/* TODO must figure out proper promotion of types
	 * and other actual typechecking, for now only check if they
	 * are of the same type
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitBinaryExpr(com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode)
	 */
	public Object visitBinaryExpr(GoIRBinaryExprNode node){
		String l = node.getType();
		if(l!= null) {//already discovered type of children
			return l;
		}
		l = (String) node.getLeft().accept(this);
		String r = (String) node.getRight().accept(this);
		GoException error = Compare(l,r,"visitbinary");
		if(error != null) {
			throw error;
		}
		node.setType(l);//so it doesnt have to repeatedly check
		// like 3+3+3+3+3... would make a big tree and we dont need to repeatedly check
		return l;
	}
	
	/* Compares 2 strings in length then .equalsIgnoreCase
	 * if not equal in length or content, return GoException
	 * Prints out message for debugging
	 * 
	 */
	public static GoException Compare(String a,String b, String message) {
		String[] side1 = null;
		String[] side2 = null;
		if(a == null && b == null) {
			return null;
		}
		if(a!=null){
			side1 = a.split(",");
		}
		else {
			side1 = new String[] {""};
		}
		if(b!=null){
			side2 = b.split(",");
		}
		else {
			side2 = new String[] {""};
		}
		if(side1.length != side2.length) {
			return new GoException("Uneven assignment: (" + side1.length + "," + side2.length + ") " + message);
			
		}
		for(int i = 0; i < side2.length; i ++) {
			System.out.println(message + "-- " + side1[i] + "," + side2[i]);
			if(!(side1[i].equalsIgnoreCase(side2[i]))) {
				return new GoException("Unequal types: (" + side1[i] + "," + side2[i] + ") " + message);
			}
		}
		
		
		return null;
	}
	
}
