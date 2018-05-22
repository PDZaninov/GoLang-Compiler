package com.oracle.app.parser.ir;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.global.GoWriteGlobalVariableNodeGen;
import com.oracle.app.nodes.local.GoArrayWriteNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteStructNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
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
public class GoGlobalWriteVisitor implements GoIRVisitor {

	private LexicalScope scope;
	private LexicalScope global;
	private GoTruffle truffleVisitor;
	private FrameDescriptor frame;
	private GoIRAssignmentStmtNode assignmentNode;
	
	public GoGlobalWriteVisitor(LexicalScope scope, GoTruffle visitor, FrameDescriptor frame, GoIRAssignmentStmtNode assignmentNode, LexicalScope global){
		this.scope = scope;
		truffleVisitor = visitor;
		this.frame = frame;
		this.assignmentNode = assignmentNode;
		this.global = global;
	}
	
	public Object visit(GoBaseIRNode node){
		return node.accept(this);
	}
	
	/**
	 * Might need to change always inserting into the lexicalscope. Does not check if the name already exists.
	 */
	public Object visitIdent(GoIRIdentNode node){
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		FrameSlot frameSlot = frame.findOrAddFrameSlot(name);
		if(global.locals.get(name) != null) {
			scope.locals.put(name,frameSlot);
			global.locals.put(name,frameSlot);
		}
		else {
			scope.locals.put(name,frameSlot);
		}
		return GoWriteGlobalVariableNodeGen.create(value, frameSlot);
	}
	
	public Object visitIndexNode(GoIRIndexNode node){
		GoReadLocalVariableNode array = (GoReadLocalVariableNode) node.getName().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoExpressionNode index = (GoExpressionNode)node.getIndex().accept(truffleVisitor);
		return GoArrayWriteNodeGen.create(index,value, array);
	}
	
	public Object visitStarNode(GoIRStarNode node){
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoReadLocalVariableNode pointee = (GoReadLocalVariableNode) node.getChild().accept(truffleVisitor);
		return GoWriteMemoryNodeGen.create(value, pointee);
	}
	
	@Override
	public Object visitSelectorExpr(GoIRSelectorExprNode node){
		GoReadLocalVariableNode expr = (GoReadLocalVariableNode) node.getExpr().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		String name = node.getName().getIdentifier();
		return GoWriteStructNodeGen.create(value, new GoStringNode(name), expr.getSlot());
	}
}