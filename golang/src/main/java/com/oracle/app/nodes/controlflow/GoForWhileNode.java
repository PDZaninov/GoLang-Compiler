package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.source.SourceSection;

public class GoForWhileNode extends Node implements RepeatingNode {
	@Child private GoExpressionNode conditionNode;
	
    @Child private GoStatementNode bodyNode;


    private final BranchProfile continueTaken = BranchProfile.create();
    private final BranchProfile breakTaken = BranchProfile.create();

    private SourceSection sourceSection;

    public GoForWhileNode(GoExpressionNode conditionNode, GoStatementNode bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    public void setSourceSection(SourceSection section) {
        assert this.sourceSection == null : "overwriting existing SourceSection";
        this.sourceSection = section;
    }

    @Override
    public boolean executeRepeating(VirtualFrame frame) {
        if (!evaluateCondition(frame)) {
            return false;
        }

        try {
            bodyNode.executeVoid(frame);
            return true;

        } catch (GoContinueException ex) {
            continueTaken.enter();
            
            return true;

        } catch (GoBreakException ex) {
            breakTaken.enter();
            
            return false;
        }
    }

    private boolean evaluateCondition(VirtualFrame frame) {
        try {
            
            return conditionNode.executeBoolean(frame);
        } catch (UnexpectedResultException ex) {
            
            throw new UnsupportedSpecializationException(this, new Node[]{conditionNode}, ex.getResult());
        }
    }

    @Override
    public String toString() {
        return GoStatementNode.formatSourceSection(this);
    }
}
