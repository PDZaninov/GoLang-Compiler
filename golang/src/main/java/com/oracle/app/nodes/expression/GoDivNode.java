package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.nodes.GoBinaryNode;

/**
 * This class is similar to the extensively documented {@link SLAddNode}. Divisions by 0 throw the
 * same {@link ArithmeticException exception} as in Java, SL has no special handling for it to keep
 * the code simple.
 */
@NodeInfo(shortName = "/")
public abstract class GoDivNode extends GoBinaryNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long div(long left, long right) throws ArithmeticException {
        long result = left / right;
        /*
         * The division overflows if left is Long.MIN_VALUE and right is -1.
         */
        if ((left & right & result) < 0) {
            throw new ArithmeticException("long overflow");
        }
        return result;
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger div(BigInteger left, BigInteger right) {
        return left.divide(right);
    }
}