package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">")
public abstract class GoGreaterThanNode extends GoBinaryNode{
    @Specialization
    protected boolean greaterThan(long left, long right) {
        return left > right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean  greaterThan(BigInteger left, BigInteger right) {
        return left.compareTo(right) > 0;
    }
    
    @Specialization
    protected boolean  greaterThan(int left, int right) {
        return left > right;
    }
    
    @Specialization
    protected boolean  greaterThan(float left, float right) {
        return left > right;
    }

    @Specialization
    protected boolean  greaterThan(double left, double right) {
        return left > right;
    }
    
    @Specialization
    protected boolean  greaterThan(String left, String right) {
        return left.compareTo(right) > 0;
    }
}
