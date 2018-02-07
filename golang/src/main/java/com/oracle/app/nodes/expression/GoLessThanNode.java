package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.nodes.GoBinaryNode;

/**
 * This class is similar to the {@link SLLessThanNode}.
 */
@NodeInfo(shortName = "<=")
public abstract class GoLessThanNode extends GoBinaryNode {

    @Specialization
    protected boolean lessOrEqual(long left, long right) {
        return left <= right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean lessOrEqual(BigInteger left, BigInteger right) {
        return left.compareTo(right) <= 0;
    }
}