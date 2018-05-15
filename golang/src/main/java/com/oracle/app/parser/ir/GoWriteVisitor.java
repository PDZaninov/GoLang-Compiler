package com.oracle.app.parser.ir;

import java.util.Map;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.local.GoArrayWriteNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteStructNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.GoTypeCheckingVisitor;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.GoTruffle.TypeInfo;
import com.oracle.app.parser.ir.nodes.GoIRArrayTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRCompositeLitNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRObjectNode;
import com.oracle.app.parser.ir.nodes.GoIRSelectorExprNode;
import com.oracle.app.parser.ir.nodes.GoIRSliceExprNode;
import com.oracle.app.parser.ir.nodes.GoIRStarNode;
import com.oracle.app.parser.ir.nodes.GoIRTypes;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

/**
 * Mini visitor called inside {@link GoTruffle} which will handle all assignment visits on the write side
 * to simplify deciding between a read and write variable.
 * @author Trevor
 *
 */
public class GoWriteVisitor implements GoIRVisitor {

	private LexicalScope scope;
	private GoTruffle truffleVisitor;
	private FrameDescriptor frame;
	private GoIRAssignmentStmtNode assignmentNode;
	private Map<String, GoRootNode> allFunctions;
	
	public GoWriteVisitor(LexicalScope scope, GoTruffle visitor, FrameDescriptor frame, GoIRAssignmentStmtNode assignmentNode, Map<String, GoRootNode> allFunctions){
		this.scope = scope;
		truffleVisitor = visitor;
		this.frame = frame;
		this.assignmentNode = assignmentNode;
		this.allFunctions = allFunctions;
	}
	
	public Object visit(GoBaseIRNode node){
		return node.accept(this);
	}
	
	
	
	/**
	 * Might need to change always inserting into the lexicalscope. Does not check if the name already exists.
	 */
	public Object visitIdent(GoIRIdentNode node) {
		GoBaseIRNode rhs = assignmentNode.getRHS();
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) rhs.accept(truffleVisitor);
		
		FrameSlot slot = frame.findOrAddFrameSlot(name);
		
		//check if the variable already exists
		if(rhs instanceof GoIRInvokeNode) {
			
		
			if(scope.locals.get(name) != null) {
				GoRootNode j =  allFunctions.get(((GoIRInvokeNode) rhs).getFunctionNode().getIdentifier());
				
				GoTypeCheckingVisitor.TCAssignmentError(name, rhs,node, j, scope.locals.get(name),assignmentNode);
				
			}else {
					String functionName = ((GoIRInvokeNode) rhs).getFunctionNode().getIdentifier();
					GoRootNode j =  allFunctions.get(functionName);
					GoTypeCheckingVisitor.TCInitialization(node,rhs,assignmentNode.getType(),j,functionName);
					
				}
			
		}
		
		// Check if the rhs is an instance of types, then just directly get the value type
		if(rhs instanceof GoIRTypes) {
			scope.locals.put(name,new TypeInfo(name, ((GoIRTypes) rhs).getValueType(), false, slot));
		}
		
		/*
		 * Various other instances. Specific to those cases in the IR, get type by the identifier.
		 */
		else if(rhs instanceof GoIRCompositeLitNode) {
			if(((GoIRCompositeLitNode) rhs).getExpr() instanceof GoIRArrayTypeNode) {
				GoIRArrayTypeNode child = (GoIRArrayTypeNode) ((GoIRCompositeLitNode) rhs).getExpr();
				scope.locals.put(name,  new TypeInfo(name, child.getType().getIdentifier().toUpperCase(), false, slot));
			}
			else {
				String childName = ((GoIRIdentNode) ((GoIRCompositeLitNode) rhs).getExpr()).getIdentifier();
				scope.locals.put(name,  new TypeInfo(name, scope.locals.get(childName).getType(), false, slot));
			}
		}
		else if(rhs instanceof GoIRSliceExprNode) {
			String childName = ((GoIRIdentNode) ((GoIRSliceExprNode)rhs).getExpr()).getIdentifier();
			scope.locals.put(name,  new TypeInfo(name, scope.locals.get(childName).getType(), false, slot));
		}
		else {
			scope.locals.put(name,  new TypeInfo(name, "object", false, slot));
		}

		return GoWriteLocalVariableNodeGen.create(value, slot);
	}
	
	public Object visitIndexNode(GoIRIndexNode node) {
		GoReadLocalVariableNode array = (GoReadLocalVariableNode) node.getName().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoExpressionNode index = (GoExpressionNode)node.getIndex().accept(truffleVisitor);

		
		return GoArrayWriteNodeGen.create(index,value, array);
	}
	
	public Object visitStarNode(GoIRStarNode node) {
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoReadLocalVariableNode pointee = (GoReadLocalVariableNode) node.getChild().accept(truffleVisitor);
		return GoWriteMemoryNodeGen.create(value, pointee);
	}
	
	@Override
	public Object visitSelectorExpr(GoIRSelectorExprNode node) {
		GoReadLocalVariableNode expr = (GoReadLocalVariableNode) node.getExpr().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		String name = node.getName().getIdentifier();
		return GoWriteStructNodeGen.create(value, new GoStringNode(name), expr.getSlot());
	}
}
