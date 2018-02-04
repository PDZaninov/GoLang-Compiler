package com.oracle.app.nodes.interop;


import com.oracle.app.runtime.GoBigNumber;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.app.nodes.GoTypes;
import java.math.BigInteger;

/**
 * The node for converting a foreign primitive or boxed primitive value to an SL value.
 */
@TypeSystemReference(GoTypes.class)
public abstract class GoTypeToForeignNode extends Node {

    public abstract Object executeConvert(Object value);

    @Specialization
    static long fromLong(long value) {
        return value;
    }

    @Specialization
    static TruffleObject fromObject(BigInteger value) {
        return new GoBigNumber(value);
    }

    @Fallback
    static Object identity(Object value) {
        return value;
    }
}