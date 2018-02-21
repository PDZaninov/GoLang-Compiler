package com.oracle.app.builtins;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameInstance.FrameAccess;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * This builtin sets the variable named "hello" in the caller frame to the string "world".
 */
@NodeInfo(shortName = "true")
public abstract class GoTrueValueBuiltin extends GoBuiltinNode{
    @Specialization
    @TruffleBoundary
    public boolean change() {
        FrameInstance frameInstance = Truffle.getRuntime().getCallerFrame();
        Frame frame = frameInstance.getFrame(FrameAccess.READ_WRITE);
        FrameSlot slot = frame.getFrameDescriptor().findOrAddFrameSlot(true);
        frame.setObject(slot, true);
        return true;
    }
}


