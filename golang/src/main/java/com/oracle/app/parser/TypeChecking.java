package com.oracle.app.parser;

import java.util.ArrayList;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldListNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRReturnStmtNode;

public class TypeChecking {

	public static GoException TCNumReturns(int returnStmtNum , int signatureNum){
		if(returnStmtNum > signatureNum) {
			return new GoException("Too many arguments to return" + Integer.toString(returnStmtNum) +" " + Integer.toString(signatureNum));
		}
		else if(returnStmtNum < signatureNum) {
			return new GoException("Not enough arguments to return");
		}
		return null;
	}
	
	public static GoException TCReturnTypes(GoIRFieldListNode r,GoIRReturnStmtNode node) {
		int signatureNum = 0;
		int returnStmtNum = 0;
		if(r != null) {
			signatureNum =  r.getFields().getSize();
		}
		if(node.getChild()!= null) {
			returnStmtNum = node.getChild().getSize();
		}

		GoException m = TypeChecking.TCNumReturns(returnStmtNum, signatureNum);
		if (m != null) {
			return m;
		}
		else if(r!=null && node.getChild()!=null) {
			ArrayList<GoBaseIRNode> expectedTypes = r.getFields().getChildren();
			ArrayList<GoBaseIRNode> returnVals = ((GoIRArrayListExprNode)node.getChild()).getChildren();
			
			for(int i = 0; i < signatureNum; i ++) {
				GoBaseIRNode val = returnVals.get(i);
				String valType = "";
				if(val instanceof GoIRIdentNode) {
					valType = GoTruffle.lexicalscope.locals.get(val.getIdentifier()).getType();
				}
				else if(val instanceof GoIRBasicLitNode) {
					valType = ((GoIRBasicLitNode) val).getType();
				}
				String expectedTypeString = ((GoIRFieldNode)expectedTypes.get(i)).getType().getIdentifier(); 
				if(!(expectedTypeString.equalsIgnoreCase(valType))) {
					return new GoException("Cannot use " + val.getIdentifier() + " (type " + valType +") as type " + expectedTypeString +" in return argument");
				}
			}
		}
		return null;
	}
	
	public static GoException TCInvokeArgNum(GoRootNode j, GoIRInvokeNode node) {
		int signatureParamNum = 0;
		int argumentNum = 0;
		if(j!= null) {// can only type check non builtin parameters
			GoArrayExprNode signatureParams = j.getParameters();
			GoIRArrayListExprNode argumentNode = node.getArgumentNode();
			if(signatureParams != null) {
				signatureParamNum = signatureParams.getSize();
			}
			if(argumentNode != null) {
				argumentNum = argumentNode.getSize();
			}
			
			if(argumentNum < signatureParamNum) {
				throw new GoException("not enough arguments in call to " + node.getFunctionNode().getIdentifier());
			}
			else if(argumentNum > signatureParamNum) {
				throw new GoException("too many arguments in call to " + node.getFunctionNode().getIdentifier());
			}
		}
		
		/*//			else if(argumentNode!= null && signatureParams != null) {
//		ArrayList<GoBaseIRNode> argChildren = argumentNode.getChildren();
//		String argString = " lol ";
//		System.out.println("----------");
//		for(int index = 0; index < argumentNum; index++) {
//			
//			if(argChildren.get(index) instanceof GoIRBasicLitNode) {
//				System.out.println("1");
//				argString = ((GoIRBasicLitNode)argChildren.get(index)).getType();
//			}
//			else if(argChildren.get(index) instanceof GoIRIdentNode) {
//				System.out.println("2");
//				System.out.println(((GoIRIdentNode)argChildren.get(index)).getIdentifier());
//				System.out.println(lexicalscope.locals.get(((GoIRIdentNode)argChildren.get(index)).getIdentifier()).getType());
//				argString = lexicalscope.locals.get(((GoIRIdentNode)argChildren.get(index)).getIdentifier()).getType();
//				
//			}
//			///                                                        FuncTypeNode          -> FieldList  -> Arraylistexpr -> ...
//			GoIRArrayListExprNode some = ((GoIRFieldListNode)(IRFunctions.get(node.getFunctionNode().getIdentifier())).getParams()).getFields();
//			//                                   ArrayListExpr -> FieldNode -> Ident
//
//			System.out.println(node.getFunctionNode().getIdentifier());
//			System.out.println(argString + " ..");
//			System.out.println(((GoIRFieldNode)some.getChildren().get(index)).getType().getIdentifier());
//			String paramName = ((GoIRFieldNode)some.getChildren().get(index)).getType().getIdentifier();
//			if(!(argString.equalsIgnoreCase(lexicalscope.locals.get(paramName).getType()))) {
//				throw new GoException(argString + " Type do not match in arguments to paramters" + lexicalscope.locals.get(paramName).getType());
//			}
//		}
//	}
*/			
		return null;
	}
	
	
}
