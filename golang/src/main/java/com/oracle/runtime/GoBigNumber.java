package com.oracle.runtime;

import java.math.BigInteger;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = GoBigNumber.class)
public final class GoBigNumber implements TruffleObject {

    private final BigInteger value;

    public GoBigNumber(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public ForeignAccess getForeignAccess() {
        return GoBigNumberForeign.ACCESS;
    }

    static boolean isInstance(TruffleObject obj) {
        return obj instanceof GoBigNumber;
    }

    @Resolve(message = "UNBOX")
    abstract static class UnboxBigNode extends Node {
        Object access(GoBigNumber obj) {
            return obj.value.doubleValue();
        }
    }

    @Resolve(message = "IS_BOXED")
    abstract static class IsBoxedBigNode extends Node {
        @SuppressWarnings("unused")
        Object access(GoBigNumber obj) {
            return true;
        }
    }
}