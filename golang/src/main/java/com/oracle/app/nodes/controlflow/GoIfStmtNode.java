package com.oracle.app.nodes.controlflow;

import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.expression.GoUnboxNodeGen;

@NodeInfo(shortName = "if", description = "The node implementing a conditional statement")
public final class GoIfStmtNode extends GoStatementNode {

    @Child private GoExpressionNode conditionNode;

    @Child private GoStatementNode thenNode;

    @Child private GoStatementNode elseNode;

    /**
     * Profiling information, collected by the interpreter, capturing the profiling information of
     * the condition. This allows the compiler to generate better code for conditions that are
     * always true or always false. Additionally the {@link CountingConditionProfile} implementation
     * (as opposed to {@link BinaryConditionProfile} implementation) transmits the probability of
     * the condition to be true to the compiler.
     */
    private final ConditionProfile condition = ConditionProfile.createCountingProfile();

    public GoIfStmtNode(GoExpressionNode conditionNode, GoStatementNode thenNode, GoStatementNode elseNode) {
        this.conditionNode = GoUnboxNodeGen.create(conditionNode);
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (condition.profile(evaluateCondition(frame))) {
            thenNode.executeVoid(frame);
        } else {
            if (elseNode != null) {
                elseNode.executeVoid(frame);
            }
        }
    }

    private boolean evaluateCondition(VirtualFrame frame) {
        try {
            return conditionNode.executeBoolean(frame);
        } catch (UnexpectedResultException ex) {
            throw new UnsupportedSpecializationException(this, new Node[]{conditionNode}, ex.getResult());
        }
    }
}