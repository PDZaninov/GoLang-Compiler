package com.oracle.app.nodes.interop;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.app.nodes.GoTypes;
import com.oracle.runtime.GoContext;
import com.oracle.runtime.GoNull;

/**
 * The node for converting a foreign primitive or boxed primitive value to an SL value.
 */
@TypeSystemReference(GoTypes.class)
public abstract class GoForeignToGoTypeNode extends Node {

    public abstract Object executeConvert(Object value);

    @Specialization
    protected static Object fromObject(Number value) {
        return GoContext.fromForeignValue(value);
    }

    @Specialization
    protected static Object fromString(String value) {
        return value;
    }

    @Specialization
    protected static Object fromBoolean(boolean value) {
        return value;
    }

    @Specialization
    protected static Object fromChar(char value) {
        return String.valueOf(value);
    }

    /*
     * In case the foreign object is a boxed primitive we unbox it using the UNBOX message.
     */
    @Specialization(guards = "isBoxedPrimitive(value)")
    public Object unbox(TruffleObject value) {
        Object unboxed = doUnbox(value);
        return GoContext.fromForeignValue(unboxed);
    }

    @Specialization(guards = "!isBoxedPrimitive(value)")
    public Object fromTruffleObject(TruffleObject value) {
        return value;
    }

    @Child private Node isBoxed;

    protected final boolean isBoxedPrimitive(TruffleObject object) {
        if (isBoxed == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            isBoxed = insert(Message.IS_BOXED.createNode());
        }
        return ForeignAccess.sendIsBoxed(isBoxed, object);
    }

    @Child private Node unbox;

    protected final Object doUnbox(TruffleObject value) {
        if (unbox == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            unbox = insert(Message.UNBOX.createNode());
        }
        try {
            return ForeignAccess.sendUnbox(unbox, value);
        } catch (UnsupportedMessageException e) {
            return GoNull.SINGLETON;
        }
    }

    public static GoForeignToGoTypeNode create() {
        return GoForeignToGoTypeNodeGen.create();
    }
}