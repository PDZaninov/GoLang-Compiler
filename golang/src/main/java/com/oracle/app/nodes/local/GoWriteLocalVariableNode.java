package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;


@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class GoWriteLocalVariableNode  extends GoExpressionNode{
	/**
	 * Node to write a local variable to a function's {@link VirtualFrame frame}. The Truffle frame API
	 * allows to store primitive values of all Java primitive types, and Object values.
	 */

	    /**
	     * Returns the descriptor of the accessed local variable. The implementation of this method is
	     * created by the Truffle DSL based on the {@link NodeField} annotation on the class.
	     */
	    protected abstract FrameSlot getSlot();
	    
	    
	    
	    @Specialization(guards = "isIntOrIllegal(frame)")
	    protected int writeInt(VirtualFrame frame, int value) {
	        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
	        getSlot().setKind(FrameSlotKind.Int);

	        frame.setInt(getSlot(), value);
	        return value;
	    }

	    /**
	     * Specialized method to write a primitive {@code long} value. This is only possible if the
	     * local variable also has currently the type {@code long} or was never written before,
	     * therefore a Truffle DSL {@link #isLongOrIllegal(VirtualFrame) custom guard} is specified.
	     */
	    @Specialization(guards = "isLongOrIllegal(frame)")
	    protected long writeLong(VirtualFrame frame, long value) {
	        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
	        getSlot().setKind(FrameSlotKind.Long);

	        frame.setLong(getSlot(), value);
	        return value;
	    }

	    @Specialization(guards = "isBooleanOrIllegal(frame)")
	    protected boolean writeBoolean(VirtualFrame frame, boolean value) {
	        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
	        getSlot().setKind(FrameSlotKind.Boolean);

	        frame.setBoolean(getSlot(), value);
	        return value;
	    }

	    /**
	     * Generic write method that works for all possible types.
	     * <p>
	     * Why is this method annotated with {@link Specialization} and not {@link Fallback}? For a
	     * {@link Fallback} method, the Truffle DSL generated code would try all other specializations
	     * first before calling this method. We know that all these specializations would fail their
	     * guards, so there is no point in calling them. Since this method takes a value of type
	     * {@link Object}, it is guaranteed to never fail, i.e., once we are in this specialization the
	     * node will never be re-specialized.
	     */
	    @Specialization(replaces = {"writeInt", "writeLong", "writeBoolean"})
	    protected Object write(VirtualFrame frame, Object value) {
	        /*
	         * Regardless of the type before, the new and final type of the local variable is Object.
	         * Changing the slot kind also discards compiled code, because the variable type is
	         * important when the compiler optimizes a method.
	         *
	         * No-op if kind is already Object.
	         */
	        getSlot().setKind(FrameSlotKind.Object);

	        frame.setObject(getSlot(), value);
	        return value;
	    }

	    /**
	     * Guard function that the local variable has the type {@code long}.
	     *
	     * @param frame The parameter seems unnecessary, but it is required: Without the parameter, the
	     *            Truffle DSL would not check the guard on every execution of the specialization.
	     *            Guards without parameters are assumed to be pure, but our guard depends on the
	     *            slot kind which can change.
	     */
	    
	    protected boolean isIntOrIllegal(VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Int || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
	    protected boolean isLongOrIllegal(VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Long || getSlot().getKind() == FrameSlotKind.Illegal;
	    }

	    protected boolean isBooleanOrIllegal(@SuppressWarnings("unused") VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Boolean || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
}
