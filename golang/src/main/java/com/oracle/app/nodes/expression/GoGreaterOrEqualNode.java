package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">=")
public abstract class GoGreaterOrEqualNode extends GoBinaryNode {
	
    @Specialization
    protected boolean greaterOrEqual(long left, long right) {
        return left >= right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean greaterOrEqual(BigInteger left, BigInteger right) {
        return left.compareTo(right) >= 0;
    }
    
    @Specialization
    protected boolean greaterOrEqual(int left, int right) {
        return left >= right;
    }
    
    @Specialization
    protected boolean greaterOrEqual(float left, float right) {
        return left >= right;
    }
    
    @Specialization
    protected boolean greaterOrEqual(String left, String right) {
        return left.compareTo(right) >= 0;
    }


}
