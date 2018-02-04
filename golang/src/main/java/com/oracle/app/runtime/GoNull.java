package com.oracle.app.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public final class GoNull implements TruffleObject {

    /**
     * The canonical value to represent {@code null} in SL.
     */
    public static final GoNull SINGLETON = new GoNull();

    /**
     * Disallow instantiation from outside to ensure that the {@link #SINGLETON} is the only
     * instance.
     */
    private GoNull() {
    }

    /**
     * This method is, e.g., called when using the {@code null} value in a string concatenation. So
     * changing it has an effect on SL programs.
     */
    @Override
    public String toString() {
        return "null";
    }

    /**
     * In case you want some of your objects to co-operate with other languages, you need to make
     * them implement {@link TruffleObject} and provide additional {@link SLNullMessageResolution
     * foreign access implementation}.
     */
    @Override
    public ForeignAccess getForeignAccess() {
        return GoNullMessageResolutionForeign.ACCESS;
    }
}