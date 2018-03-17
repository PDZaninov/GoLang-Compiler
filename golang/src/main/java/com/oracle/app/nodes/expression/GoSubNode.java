package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * This class is similar to the extensively documented {@link SLAddNode}.
 */
@NodeInfo(shortName = "-")
public abstract class GoSubNode extends GoBinaryNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long sub(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected int sub(int left, int right) {
        return Math.subtractExact(left, right);
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float sub(float left, float right) {
        return left - right;
    }
    
    @Specialization
    @TruffleBoundary
    protected BigInteger sub(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }
}