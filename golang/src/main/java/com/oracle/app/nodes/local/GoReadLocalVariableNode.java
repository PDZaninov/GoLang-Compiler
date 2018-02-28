package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node to read a local variable from a function's {@link VirtualFrame frame}. The Truffle frame API
 * allows to store primitive values of all Java primitive types, and Object values. This means that
 * all SL types that are objects are handled by the {@link #readObject} method.
 * <p>
 * We use the primitive type only when the same primitive type is uses for all writes. If the local
 * variable is type-polymorphic, then the value is always stored as an Object, i.e., primitive
 * values are boxed. Even a mixture of {@code long} and {@code boolean} writes leads to both being
 * stored boxed.
 */
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class GoReadLocalVariableNode extends GoExpressionNode {

    /**
     * Returns the descriptor of the accessed local variable. The implementation of this method is
     * created by the Truffle DSL based on the {@link NodeField} annotation on the class.
     */
    protected abstract FrameSlot getSlot();
    
    @Specialization(guards = "isInt(frame)")
    protected int readInt(VirtualFrame frame){
    	return FrameUtil.getIntSafe(frame, getSlot());
    }
    
    @Specialization(guards = "isFloat(frame)")
    protected float readFloat(VirtualFrame frame){
    	return FrameUtil.getFloatSafe(frame, getSlot());
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        /*
         * When the FrameSlotKind is Long, we know that only primitive long values have ever been
         * written to the local variable. So we do not need to check that the frame really contains
         * a primitive long value.
         */
        return FrameUtil.getLongSafe(frame, getSlot());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, getSlot());
    }
    
    @Specialization(guards = "isString(frame)")
    protected String readString(VirtualFrame frame) {
        return (String) FrameUtil.getObjectSafe(frame, getSlot());
    }

    @Specialization(replaces = {"readInt", "readFloat", "readLong", "readBoolean", "readString"})
    protected Object readObject(VirtualFrame frame) {
        if (!frame.isObject(getSlot())) {
            /*
             * The FrameSlotKind has been set to Object, so from now on all writes to the local
             * variable will be Object writes. However, now we are in a frame that still has an old
             * non-Object value. This is a slow-path operation: we read the non-Object value, and
             * write it immediately as an Object value so that we do not hit this path again
             * multiple times for the same variable of the same frame.
             */
            CompilerDirectives.transferToInterpreter();
            Object result = frame.getValue(getSlot());
            frame.setObject(getSlot(), result);
            return result;
        }

        return FrameUtil.getObjectSafe(frame, getSlot());
    }

    /**
     * Guard function that the local variable has the type {@code long}.
     *
     * @param frame The parameter seems unnecessary, but it is required: Without the parameter, the
     *            Truffle DSL would not check the guard on every execution of the specialization.
     *            Guards without parameters are assumed to be pure, but our guard depends on the
     *            slot kind which can change.
     */
    
    protected boolean isInt(VirtualFrame frame){
    	return getSlot().getKind() == FrameSlotKind.Int;
    }
    
    protected boolean isFloat(VirtualFrame frame){
    	return getSlot().getKind() == FrameSlotKind.Float;
    }
    
    protected boolean isLong(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Long;
    }

    protected boolean isBoolean(@SuppressWarnings("unused") VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }
    
    protected boolean isString(@SuppressWarnings("unused") VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Object;
    }
}