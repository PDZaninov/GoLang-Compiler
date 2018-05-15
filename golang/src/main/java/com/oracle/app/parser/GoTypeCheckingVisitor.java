package com.oracle.app.parser;

import java.util.ArrayList;
import java.util.LinkedList;

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
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.GoTruffle.TypeInfo;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldListNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldNode;
import com.oracle.app.parser.ir.nodes.GoIRFloat32Node;
import com.oracle.app.parser.ir.nodes.GoIRFloat64Node;
import com.oracle.app.parser.ir.nodes.GoIRFuncTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRReturnStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRStringNode;
import com.oracle.app.parser.ir.nodes.GoIRTypes;

public class GoTypeCheckingVisitor implements GoIRVisitor{
	

	public GoTypeCheckingVisitor() {
		
	}
	
	 public Object visitReturnStmt(GoIRReturnStmtNode node) {
		 	return node.getChild().accept(this);
	}
	
	
	
	public Object visitInvoke(GoIRInvokeNode node) {
		String a = "";
		String b = "";
		
		if(node != null) {
			GoIRFuncTypeNode funcTypeNode = GoTruffle.IRFunctions.get(node.getFunctionNode().getIdentifier());
			if(funcTypeNode != null) {
				a = (String) funcTypeNode.getParams().accept(this);
				
			}else {
				//is builting function idk what to do
				return null;
			}
			if(node.getArgumentNode()!= null) {
				b = (String) node.getArgumentNode().accept(this);
			}
			
		}
		
		return Compare(a,b);
	}
	
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
	
	
	public Object visitFieldList(GoIRFieldListNode node){
		if(node.getFields()!= null) {
			return node.getFields().accept(this);
		}
		return "";
	}
	
	public Object visitField(GoIRFieldNode node)
	{
		return node.getType().getIdentifier();
	}
	
	public Object visitIdent(GoIRIdentNode node){
		return GoTruffle.lexicalscope.locals.get(node.getIdentifier()).getType();
	}
	
	
	//probably fix some issues here
	public static GoException TCInitialization(GoIRIdentNode node, GoBaseIRNode rhs, GoIRIdentNode type, GoRootNode j, String functionName) {
		
		if(node.getChild()!= null) {
			int pos = node.getAssignPos();
			
			if(j != null) {
				
				if(j.getNumReturns() != ((GoIRInvokeNode) rhs).getAssignLen()) {
					throw new GoException("assignment count mismatch: " + ((GoIRInvokeNode) rhs).getAssignLen() + " = " + Integer.toString(j.getNumReturns()));
					
				}
				String rhsType = j.getIndexResultType(pos);
				if(type == null) {
					return null;
				}
				else if(type.getIdentifier().equalsIgnoreCase("object")) {
					return null;
				}
				else if(!(type.getIdentifier().equalsIgnoreCase(rhsType))) {
					return new GoException("cannot use " + functionName +"() (type " + rhsType + ") as type " +type.getIdentifier() + " in assignment");
				}
			}

		}
		return null;
	}
	
	public static boolean TCAssignment(String name, GoBaseIRNode rhs, GoIRIdentNode node, GoRootNode j, TypeInfo type) {
		if(rhs instanceof GoIRInvokeNode) 
		{
			int pos = node.getAssignPos();
			
			if(j==null) {//means it is a builtin
				return true;
			}
			if(type.getType().equalsIgnoreCase(j.getIndexResultType(pos))||(type.getType().equals("object"))) {
				return true;
			}
			throw new GoException("some erorr" + type.getType());
		}
		else if(!(rhs instanceof GoIRTypes)) {
			return true;
		}
		else if(type.getType().equalsIgnoreCase("object")) {
			return true;
			//fix this later
		}
		String kind = ((GoIRTypes) rhs).getValueType();
		if(kind.equals(type.getType())) {
			return true;
		}
		return false;
	}
	
	public static GoException TCAssignmentError(String name, GoBaseIRNode rhs, GoIRIdentNode node, GoRootNode j, TypeInfo type, GoIRAssignmentStmtNode assignmentNode) {
		boolean typeCheck = GoTypeCheckingVisitor.TCAssignment(name, rhs,node, j,type);
		if(typeCheck == false) {
			String kind = type.getType();
			String typeVal = ((GoIRBasicLitNode) rhs).getValString();
			String typeName = ((GoIRBasicLitNode) assignmentNode.getRHS()).getType();
			return new GoException("cannot use \"" + typeVal + "\" (type " + typeName.toLowerCase() + ") as type " + kind.toLowerCase() + " in assignment");
		}
		return null;
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
	
	public static GoException Compare(String a,String b) {
		if(a == null && b == null) {
			return null;
		}
		String[] side1 = a.split(",");
		String[] side2 = b.split(",");
		
		if(side1.length != side2.length) {
			return new GoException("Uneven assignment: " + side1.length + "," + side2.length);
			
		}
		for(int i = 0; i < side2.length; i ++) {
			System.out.println("-- " + side1[i] + "," + side2[i]);
			if(!(side1[i].equalsIgnoreCase(side2[i]))) {
				return new GoException("Unequal types: " + side1[i] + "," + side2[i]);
			}
		}
		
		
		return null;
	}
	
}
