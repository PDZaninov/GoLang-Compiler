package com.oracle.app.parser.ir;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteMemoryNodeGen;
import com.oracle.app.parser.ir.GoTruffle.LexicalScope;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRStarNode;
import com.oracle.truffle.api.frame.FrameSlot;

/**
 * Mini visitor called inside {@link GoTruffle} which will handle all assignment visits
 * to simplify deciding between a read and write variable.
 * @author Trevor
 *
 */
public class GoWriteVisitor implements GoIRVisitor {

	private LexicalScope scope;
	private GoExpressionNode value;
	private String name;
	private FrameSlot slot;
	
	public GoWriteVisitor(LexicalScope scope, GoExpressionNode value, String name,FrameSlot slot){
		this.scope = scope;
		this.value = value;
		this.name = name;
		this.slot = slot;
	}
	
	public Object visit(GoBaseIRNode node){
		return node.accept(this);
	}
	
	public Object visitIdent(GoIRIdentNode node){
		scope.locals.put(name,slot);
		return GoWriteLocalVariableNodeGen.create(value, slot);
	}
	
	public Object visitStarNode(GoIRStarNode node){
		return GoWriteMemoryNodeGen.create(value, slot);
	}
}
