package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "==")
public abstract class GoEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean equal(int left, int right) {
        return left == right;
    }

    @Specialization
    protected boolean equal(double left, double right) {
        return left == right;
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
         * TODO Check if we compare function pointers???
         */
        return left == right;
    }

    @Specialization
    protected boolean equal(GoNull left, GoNull right) {
        return left == right;
    }

    @Specialization(guards = "left.getClass() != right.getClass()")
    protected boolean equal(Object left, Object right) {
        assert !left.equals(right);
        return false;
    }
}