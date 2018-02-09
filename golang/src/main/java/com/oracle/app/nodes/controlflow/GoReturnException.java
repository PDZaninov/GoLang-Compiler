package com.oracle.app.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class GoReturnException extends ControlFlowException {

    private static final long serialVersionUID = 4073191346281369231L;

    private final Object result;

    public GoReturnException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}