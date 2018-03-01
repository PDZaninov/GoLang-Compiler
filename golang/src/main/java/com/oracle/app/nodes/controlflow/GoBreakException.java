package com.oracle.app.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class GoBreakException extends ControlFlowException {

    public static final GoBreakException SINGLETON = new GoBreakException();

    private static final long serialVersionUID = -91013036379258890L;

    private GoBreakException() {}
}
