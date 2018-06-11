package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

/**
 * A {@link GoExpressionNode} that represents a parenthesized expression; it simply returns the
 * value of the enclosed (child) expression. It is represented separately in the AST for the purpose
 * of correct source attribution; this preserves the lexical relationship between the two
 * parentheses and allows a tool to describe the expression as distinct from its contents.
 * TODO This is probably useless. When parsing through gotruffle return the child instead of this node.
 */
@NodeInfo(description = "A parenthesized expression")
public class GoParenExpressionNode extends GoExpressionNode {

    @Child private GoExpressionNode expression;

    public GoParenExpressionNode(GoExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return expression.executeGeneric(frame);
    }
    
    @Override
    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return expression.executeBoolean(frame);
    }
}