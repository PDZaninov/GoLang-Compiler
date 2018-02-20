package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "!=")
public abstract class GoNotEqualNode extends GoBinaryNode {
    @Specialization
    protected boolean equal(long left, long right) {
        return left != right;
    }
    @Specialization
    protected boolean equal(int left, int right) {
        return left != right;
    }

    @Specialization
    @TruffleBoundary
    protected boolean equal(BigInteger left, BigInteger right) {
        return !left.equals(right);
    }

    @Specialization
    protected boolean equal(boolean left, boolean right) {
        return left != right;
    }

    @Specialization
    protected boolean equal(String left, String right) {
        return !left.equals(right);
    }

    @Specialization
    protected boolean equal(GoFunction left, GoFunction right) {
        /*
         * Our function registry maintains one canonical SLFunction object per function name, so we
         * do not need equals().
         */
        return left != right;
    }

    @Specialization
    protected boolean equal(GoNull left, GoNull right) {
        /* There is only the singleton instance of SLNull, so we do not need equals(). */
        return left != right;
    }

}
