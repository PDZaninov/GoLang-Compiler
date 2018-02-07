package com.oracle.app.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.interop.GoForeignToGoTypeNode;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import java.math.BigInteger;

@NodeChild("child")
public abstract class GoUnboxNode extends GoExpressionNode {

    @Specialization
    protected long unboxLong(long value) {
        return value;
    }

    @Specialization
    protected BigInteger unboxBigInteger(BigInteger value) {
        return value;
    }

    @Specialization
    protected boolean unboxBoolean(boolean value) {
        return value;
    }

    @Specialization
    protected String unboxString(String value) {
        return value;
    }

    @Specialization
    protected GoFunction unboxFunction(GoFunction value) {
        return value;
    }

    @Specialization
    protected GoNull unboxNull(GoNull value) {
        return value;
    }

    @Specialization(guards = "isBoxedPrimitive(value)")
    protected Object unboxBoxed(
                    Object value,
                    @Cached("create()") GoForeignToGoTypeNode foreignToGo) {
        return foreignToGo.unbox((TruffleObject) value);
    }

    @Specialization(guards = "!isBoxedPrimitive(value)")
    protected Object unboxGeneric(Object value) {
        return value;
    }

    @Child private Node isBoxed;

    protected boolean isBoxedPrimitive(Object value) {
        if (value instanceof TruffleObject) {
            if (isBoxed == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                isBoxed = insert(Message.IS_BOXED.createNode());
            }
            if (ForeignAccess.sendIsBoxed(isBoxed, (TruffleObject) value)) {
                return true;
            }
        }
        return false;
    }
}