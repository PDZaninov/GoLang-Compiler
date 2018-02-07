package com.oracle.app.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

/**
 * Utility base class for operations that take two arguments (per convention called "left" and
 * "right"). For concrete subclasses of this class, the Truffle DSL creates two child fields, and
 * the necessary constructors and logic to set them.
 */
@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
public abstract class GoBinaryNode extends GoExpressionNode {
}
