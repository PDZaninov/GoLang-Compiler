package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
/**
 * This class is similar to the {@link SLLessThanNode}.
 */
@NodeInfo(shortName = "<=")
public abstract class GoLessOrEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean lessOrEqual(long left, long right) {
        return left <= right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean lessOrEqual(BigInteger left, BigInteger right) {
        return left.compareTo(right) <= 0;
    }
    
    @Specialization
    protected boolean lessOrEqual(int left, int right) {
        return left <= right;
    }
    
    @Specialization
    protected boolean lessOrEqual(float left, float right) {
        return left <= right;
    }

    @Specialization
    protected boolean lessOrEqual(double left, double right) {
        return left <= right;
    }
    
    @Specialization
    protected boolean lessOrEqual(String left, String right) {
        return left.compareTo(right) <= 0;
    }
}