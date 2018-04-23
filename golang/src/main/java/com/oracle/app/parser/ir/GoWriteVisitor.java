package com.oracle.app.parser.ir;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoArrayWriteNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteStructNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRSelectorExprNode;
import com.oracle.app.parser.ir.nodes.GoIRStarNode;
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
	
	public GoWriteVisitor(LexicalScope scope, GoTruffle visitor, FrameDescriptor frame, GoIRAssignmentStmtNode assignmentNode){
		this.scope = scope;
		truffleVisitor = visitor;
		this.frame = frame;
		this.assignmentNode = assignmentNode;
	}
	
	public Object visit(GoBaseIRNode node){
		return node.accept(this);
	}
	
	public boolean typeCheck(GoIRIdentNode lhs, GoBaseIRNode rhs, FrameSlot frameslot) {
		System.out.println(frameslot.toString());
		GoIRBasicLitNode value = (GoIRBasicLitNode) rhs;
		String kind = value.getType();
		String frameKind = frameslot.getKind().toString();
		switch(frameKind) {
			case "Object":
				break;
			case "Illegal":
				break;
			case "Long":
				break;
			case "Int":
				break;
			case "Double":
				break;
			case "Float":
				break;
			case "Boolean":
				break;
			default:
				System.out.println("Type checking not implemented for this type");
		}
		
		return false;
	}
	
	public boolean typeCheck(GoIRIndexNode lhs, GoBaseIRNode rhs) {
		
		return false;
	}
	
	public boolean typeCheck(GoIRStarNode lhs, GoBaseIRNode rhs) {
		
		return false;
	}
	
	public boolean typeCheck(GoIRSelectorExprNode lhs, GoBaseIRNode rhs) {
		
		return false;
	} 
	
	/**
	 * Might need to change always inserting into the lexicalscope. Does not check if the name already exists.
	 */
	public Object visitIdent(GoIRIdentNode node) {
		
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		FrameSlot frameSlot = frame.findOrAddFrameSlot(name);
		
		if(!typeCheck(node, assignmentNode.getRHS(), frameSlot)) {
			System.out.println("Type Error Ident");
		}
		
		scope.locals.put(name,frameSlot);
		return GoWriteLocalVariableNodeGen.create(value, frameSlot);
	}
	
	public Object visitIndexNode(GoIRIndexNode node) {
		
		if(!typeCheck(node, assignmentNode.getRHS())) {
			System.out.println("Type Error Index");
		}
		
		GoReadLocalVariableNode array = (GoReadLocalVariableNode) node.getName().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoExpressionNode index = (GoExpressionNode)node.getIndex().accept(truffleVisitor);
		return GoArrayWriteNodeGen.create(index,value, array);
	}
	
	public Object visitStarNode(GoIRStarNode node) {
		
		if(!typeCheck(node, assignmentNode.getRHS())) {
			System.out.println("Type Error Star");
		}
		
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoReadLocalVariableNode pointee = (GoReadLocalVariableNode) node.getChild().accept(truffleVisitor);
		return GoWriteMemoryNodeGen.create(value, pointee);
	}
	
	@Override
	public Object visitSelectorExpr(GoIRSelectorExprNode node) {
		
		if(!typeCheck(node, assignmentNode.getRHS())) {
			System.out.println("Type Error Selector");
		}
		
		GoReadLocalVariableNode expr = (GoReadLocalVariableNode) node.getExpr().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		String name = node.getName().getIdentifier();
		return GoWriteStructNodeGen.create(value, new GoStringNode(name), expr.getSlot());
	}
}
