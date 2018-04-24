package com.oracle.app.parser.ir;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoArrayWriteNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteStructNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.GoTruffle.TypeInfo;
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
	
	public String getRhsKind(GoBaseIRNode rhs) {
		GoIRBasicLitNode value = (GoIRBasicLitNode) rhs;
		return value.getType();
	}
	
	public boolean typeCheck(GoBaseIRNode rhs, TypeInfo type) {
		String kind = getRhsKind(rhs);
		if(kind.equals(type.getType())) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Might need to change always inserting into the lexicalscope. Does not check if the name already exists.
	 */
	public Object visitIdent(GoIRIdentNode node) {
		
		String name = assignmentNode.getIdentifier();
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		
		FrameSlot slot = frame.findOrAddFrameSlot(name);
		
		if(scope.locals.get(name) != null) {
			boolean typeCheck = typeCheck(assignmentNode.getRHS(), scope.locals.get(name));
			if(typeCheck == false) {
				throw new GoException("wrong assignment type for \"" + name + "\"");
			}
		}
		
		scope.locals.put(name,new TypeInfo(name, ((GoIRBasicLitNode) assignmentNode.getRHS()).getType(), false, slot));
		return GoWriteLocalVariableNodeGen.create(value, slot);
	}
	
	public Object visitIndexNode(GoIRIndexNode node) {
		
		GoReadLocalVariableNode array = (GoReadLocalVariableNode) node.getName().accept(truffleVisitor);
		GoExpressionNode value = (GoExpressionNode) assignmentNode.getRHS().accept(truffleVisitor);
		GoExpressionNode index = (GoExpressionNode)node.getIndex().accept(truffleVisitor);
		
		FrameSlot slot = array.getSlot();
		String name = node.getIdentifier();
		if(scope.locals.get(name) != null) {
			boolean typeCheck = typeCheck(assignmentNode.getRHS(), scope.locals.get(name));
			if(typeCheck == false) {
				throw new GoException("wrong assignment type for \"" + name + "\"");
			}
		}
		
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
