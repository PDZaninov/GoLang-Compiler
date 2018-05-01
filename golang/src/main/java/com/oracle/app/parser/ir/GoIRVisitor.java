package com.oracle.app.parser.ir;


import com.oracle.app.nodes.GoFileNode;
import com.oracle.app.nodes.types.GoFloat32Node;
import com.oracle.app.nodes.types.GoFloat64Node;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.nodes.*;


public interface GoIRVisitor {
	
	default Object visitObject(GoTempIRNode	 node){
		System.out.println("Default Base IR Node visit");
		return null;
	}
	
	default Object visitIdent(GoIRIdentNode node){
		System.out.println("Default Identifier Visit");
		return null;
	}
	
	default Object visitBinaryExpr(GoIRBinaryExprNode node){
		System.out.println("Default Binary Expression Visit");
		return null;
	}
	
	default Object visitInvoke(GoIRInvokeNode node){
		System.out.println("Default Invoke Visit");
		return null;
	}	

	default Object visitFuncDecl(GoIRFuncDeclNode node){
		System.out.println("Default Function Declaration Visit");
		return null;
	}

	default Object visitArrayListExpr(GoIRArrayListExprNode node){
		System.out.println("Default ArrayList Expression Visit");
		return null;
	}
	
	default Object visitBlockStmt(GoIRBlockStmtNode node){
		System.out.println("Default Block Statement Visit");
		return null;
	}

	default Object visitExprStmt(GoIRExprStmtNode node){
		System.out.println("Default Expression Statement Visit");
		return null;
	}
	
	default Object visitExpr(GoIRExprNode node){
		System.out.println("Default Expression Visit");
		return null;
	}
	
	default Object visitStmt(GoIRStmtNode node){
		System.out.println("Default Statement Visit");
		return null;
	}
	
	default Object visitUnary(GoIRUnaryNode node){
		System.out.println("Default Unary Operator Visit");
		return null;
	}

	default Object visitDeclStmt(GoIRDeclStmtNode goIRDeclStmtNode){
		System.out.println("Default Decl Statement Visit");
		return null;
	}

	default Object visitGenDecl(GoIRGenDeclNode goIRGenDeclNode){
		System.out.println("Default Gen Decl Visit");
		return null;
	}
<<<<<<< HEAD

=======
	
>>>>>>> f8a098fc90b97faae97ed182db94157e9c369b08
	default Object visitForLoop(GoIRForNode node){
		System.out.println("Default For Loop Visit");
		return null;
	}
	
	default Object visitIncDecStmt(GoIRIncDecStmtNode node){
		System.out.println("Default Increment Statement Visit");
		return null;
	}
	
	default Object visitBranchStmt(GoIRBranchStmtNode node){
		System.out.println("Default Branch Statement Visit");
		return null;
	}

	default Object visitCaseClause(GoIRCaseClauseNode goIRCaseClauseNode){
		System.out.println("Default Case Clause Visit");
		return null;
	}

	default Object visitSwitchStmt(GoIRSwitchStmtNode goIRSwitchStmtNode){
		System.out.println("Default Switch Statement Visit");
		return null;
	}

	default Object visitIfStmt(GoIRIfStmtNode goIRIfStmtNode){
		System.out.println("Default If Statement Visit");
		return null;
	}

	default Object visitArrayType(GoIRArrayTypeNode goIRArrayTypeNode){
		System.out.println("Default Array Type Visit");
		return null;
	}

	default Object visitIndexNode(GoIRIndexNode goIRIndexNode){
		System.out.println("Default Index Node Visit");
		return null;
	}

	default GoIntNode visitIRIntNode(GoIRIntNode goIRIntNode){
		System.out.println("Default Basic Int Visit");
		return null;
	}

	default GoFloat32Node visitIRFloat32Node(GoIRFloat32Node goIRFloat32Node){
		System.out.println("Default Basic Float 32 visit");
		return null;
	}

	default GoFloat64Node visitIRFloat64Node(GoIRFloat64Node goIRFloat64Node){
		System.out.println("Default Basic Float 64 visit");
		return null;
	}

	default GoStringNode visitIRStringNode(GoIRStringNode goIRStringNode){
		System.out.println("Default Basic String Visit");
		return null;
	}

	default Object visitAssignment(GoIRAssignmentStmtNode goIRAssignmentStmtNode){
		System.out.println("Default Assignment Statement Visit");
		return null;
	}

	default Object visitStarNode(GoIRStarNode goIRStarNode){
		System.out.println("Default Star Node");
		return null;
	}

	default Object visit(GoIRCompositeLitNode goIRCompositeLitNode){
		System.out.println("Default Composite Lit Node");
		return null;
	}
	
	default Object visitImportSpec(GoIRImportSpecNode goIRImportSpecNode){
		System.out.println("Default ImportSpec Visit");
		return null;
	}

	default Object visitSelectorExpr(GoIRSelectorExprNode goIRSelectorExprNode){
		System.out.println("Default SelectorExpr Visit");
		return null;
	}

	default Object visitArrayField(GoIRArrayFieldNode goIRArrayFieldNode)
	{
		System.out.println("Default ArrayField Visit");
		return null;
	}
	
	default Object visitField(GoIRFieldNode goIRFieldNode)
	{
		System.out.println("Default Field Visit");
		return null;
	}

	default Object visitReturnStmt(GoIRReturnStmtNode goIRReturnStmtNode) {

		System.out.println("Default Return Stmt Visit");
		return null;
	}

	default Object visitSliceExpr(GoIRSliceExprNode goIRSliceExprNode){
		System.out.println("Default Slice Expr Visit");
		return null;
	}
	
	default Object visitFuncType(GoIRFuncTypeNode node){
		System.out.println("Default Func Type Visit");
		return null;
	}

	default Object visitTypeSpec(GoIRTypeSpecNode goIRTypeSpecNode){
		System.out.println("Default Type Spec visit");
		return null;
	}

	default Object visitStructType(GoIRStructTypeNode goIRStructTypeNode){
		System.out.println("Default Struct Type visit");
		return null;
	}

	default Object visitFieldList(GoIRFieldListNode goIRFieldListNode){
		System.out.println("Default Field List visit");
		return null;
	}

	default GoFileNode visitFile(GoIRFileNode goIRFileNode){
		System.out.println("Default File Node visit");
		return null;
	}

	default Object visitObjectNode(GoIRObjectNode node) {
		System.out.println("Default Object visit");
		return null;
	}

	default Object visitKeyValue(GoIRKeyValueNode goKeyValueExprNode){
		System.out.println("Default Key Value Node visit");
		return null;
	}

	default Object visitMapType(GoIRMapTypeNode goIRMapTypeNode){
		System.out.println("Default Map Type visit");
		return null;
	}

}
