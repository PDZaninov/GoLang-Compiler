package com.oracle.app.builtins;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameInstance.FrameAccess;
import com.oracle.truffle.api.nodes.NodeInfo;


/**
 * This builtin sets the variable named "true" in the caller frame to the boolean false
 */
@NodeInfo(shortName = "FalseEqualsFalse")
public abstract class GoFalseEqualsFalse extends GoBuiltinNode{
    @Specialization
    @TruffleBoundary
    public boolean change() {
        FrameInstance frameInstance = Truffle.getRuntime().getCallerFrame();
        Frame frame = frameInstance.getFrame(FrameAccess.READ_WRITE);
        FrameSlot slot = frame.getFrameDescriptor().findOrAddFrameSlot("false");
        frame.setObject(slot, false);
        return true;
    }
}
