package com.oracle.app.parser.ir;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.expression.GoIndexExprNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode.GoReadArrayNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen.GoReadArrayNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRStarNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

/**
 * Mini visitor called inside {@link GoTruffle} which will handle all assignment visits
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
	
	/*
	 * Might need to change always inserting into the lexicalscope. Does not check if the name already exists.
	 * (non-Javadoc)
	 * @see com.oracle.app.parser.ir.GoIRVisitor#visitIdent(com.oracle.app.parser.ir.nodes.GoIRIdentNode)
	 */
	public Object visitIdent(GoIRIdentNode node){
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		FrameSlot frameSlot = frame.findOrAddFrameSlot(name);
		scope.locals.put(name,frameSlot);
		return GoWriteLocalVariableNodeGen.create(value, frameSlot);
	}
	
	public Object visitIndexNode(GoIRIndexNode node){
		GoReadLocalVariableNode name = (GoReadLocalVariableNode) node.getName().accept(truffleVisitor);
		GoIndexExprNode array = new GoIndexExprNode(name,(GoExpressionNode)node.getIndex().accept(truffleVisitor));
		return array;
	}
	
	public Object visitStarNode(GoIRStarNode node){
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		FrameSlot frameSlot = frame.findOrAddFrameSlot(name);
		return GoWriteMemoryNodeGen.create(value, frameSlot);
	}
}
