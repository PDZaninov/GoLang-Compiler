package com.oracle.app.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class GoContinueException extends ControlFlowException {

    public static final GoContinueException SINGLETON = new GoContinueException();

    private static final long serialVersionUID = -91013036379258890L;

    private GoContinueException() {}
}
