package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Constant literal for a arbitrary-precision number that exceeds the range of
 * {@link SLLongLiteralNode}.
 */
@NodeInfo(shortName = "const")
public final class GoBigIntegerLiteralNode extends GoExpressionNode {

    private final BigInteger value;

    public GoBigIntegerLiteralNode(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger executeGeneric(VirtualFrame frame) {
        return value;
    }
}