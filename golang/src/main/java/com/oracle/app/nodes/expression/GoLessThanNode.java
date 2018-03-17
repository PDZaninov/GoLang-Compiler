package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;


/**
 * This class is similar to the {@link SLLessThanNode}.
 */
@NodeInfo(shortName = "<")
public abstract class GoLessThanNode extends GoBinaryNode {

    @Specialization
    protected boolean lessThan(long left, long right) {
        return left < right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean  lessThan(BigInteger left, BigInteger right) {
        return left.compareTo(right) < 0;
    }
    
    @Specialization
    protected boolean  lessThan(int left, int right) {
        return left < right;
    }
    
    @Specialization
    protected boolean  lessThan(float left, float right) {
        return left < right;
    }
    
    @Specialization
    protected boolean  lessThan(String left, String right) {
        return left.compareTo(right) < 0;
    }
}