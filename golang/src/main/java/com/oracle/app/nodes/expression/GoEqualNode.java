package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;

/**
 * The {@code ==} operator of SL is defined on all types. Therefore, we need a
 * {@link #equal(Object, Object) implementation} that can handle all possible types. But since
 * {@code ==} can only return {@code true} when the type of the left and right operand are the same,
 * the specializations already cover all possible cases that can return {@code true} and the generic
 * case is trivial.
 * <p>
 * Note that we do not need the analogous {@code !=} operator, because we can just
 * {@link SLLogicalNotNode negate} the {@code ==} operator.
 */
@NodeInfo(shortName = "==")
public abstract class GoEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean equal(long left, long right) {
        return left == right;
    }
    @Specialization
    protected boolean equal(int left, int right) {
        return left == right;
    }

//    @Specialization
//    protected boolean equal(double left, double right) {
//        return left == right;
//    }

    @Specialization
    @TruffleBoundary
    protected boolean equal(BigInteger left, BigInteger right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(boolean left, boolean right) {
        return left == right;
    }

    @Specialization
    protected boolean equal(String left, String right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(GoFunction left, GoFunction right) {
        /*
         * Our function registry maintains one canonical SLFunction object per function name, so we
         * do not need equals().
         */
        return left == right;
    }

    @Specialization
    protected boolean equal(GoNull left, GoNull right) {
        /* There is only the singleton instance of SLNull, so we do not need equals(). */
        return left == right;
    }

    /**
     * We covered all the cases that can return true in the type specializations above. If we
     * compare two values with different types, the result is known to be false.
     * <p>
     * Note that the guard is essential for correctness: without the guard, the specialization would
     * also match when the left and right value have the same type. The following scenario would
     * return a wrong value: First, the node is executed with the left value 42 (type long) and the
     * right value "abc" (String). This specialization matches, and since it is the first execution
     * it is also the only specialization. Then, the node is executed with the left value "42" (type
     * long) and the right value "42" (type long). Since this specialization is already present, and
     * (without the guard) also matches (long values can be boxed to Object), it is executed. The
     * wrong return value is "false".
     */
    @Specialization(guards = "left.getClass() != right.getClass()")
    protected boolean equal(Object left, Object right) {
        assert !left.equals(right);
        return false;
    }
}