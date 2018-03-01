package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(shortName = "for", description = "The node implementing a for loop")
public class GoForNode extends GoStatementNode {
	
	@Child private GoExpressionNode init;

	@Child private LoopNode loopNode;

    public GoForNode(GoExpressionNode init, GoExpressionNode conditionNode, GoExpressionNode post, GoStatementNode bodyNode) {
    	this.init = init;
    	if(post != null)
    		this.loopNode = Truffle.getRuntime().createLoopNode(new GoForRepeatingNode(conditionNode,post,bodyNode));
    	else
    		this.loopNode = Truffle.getRuntime().createLoopNode(new GoForWhileNode(conditionNode,bodyNode));
    }

    @Override
    public void setSourceSection(SourceSection section) {
        super.setSourceSection(section);
        /* Propagate the SourceSection also to the repeated loop body node. */
        ((GoForRepeatingNode) loopNode.getRepeatingNode()).setSourceSection(section);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
    	
        loopNode.executeLoop(frame);
    }
}
