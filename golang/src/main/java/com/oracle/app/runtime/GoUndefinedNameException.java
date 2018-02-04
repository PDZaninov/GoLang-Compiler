package com.oracle.app.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.app.GoException;

public final class GoUndefinedNameException extends GoException {

    private static final long serialVersionUID = 1L;

    @TruffleBoundary
    public static GoUndefinedNameException undefinedFunction(Object name) {
        throw new GoUndefinedNameException("Undefined function: " + name);
    }

    @TruffleBoundary
    public static GoUndefinedNameException undefinedProperty(Object name) {
        throw new GoUndefinedNameException("Undefined property: " + name);
    }

    private GoUndefinedNameException(String message) {
        super(message);
    }
}