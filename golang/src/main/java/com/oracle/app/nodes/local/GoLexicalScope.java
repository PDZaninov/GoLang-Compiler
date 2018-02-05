package com.oracle.app.nodes.local;
/*
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameGoot;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.metadata.ScopeProvider.AbstractScope;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeUtil;
import com.oracle.truffle.api.nodes.NodeVisitor;
import com.oracle.truffle.api.nodes.RootNode;

import com.oracle.app.nodes.GoStatementNode;
//import com.oracle.truffle.Go.nodes.controlflow.GoBlockNode;
import com.oracle.app.runtime.GoBigNumber;

/**
 * Simple language lexical scope. There can be a block scope, or function scope.
 *
public final class GoLexicalScope extends AbstractScope {

    private final Node current;
    private final GoBlockNode block;
    private final GoBlockNode parentBlock;
    private final RootNode root;
    private GoLexicalScope parent;
    private Map<String, FrameGoot> varGoots;

    /**
     * Create a new block Go lexical scope.
     * 
     * @param current the current node
     * @param block a nearest block enclosing the current node
     * @param parentBlock a next parent block
     *
    private GoLexicalScope(Node current, GoBlockNode block, GoBlockNode parentBlock) {
        this.current = current;
        this.block = block;
        this.parentBlock = parentBlock;
        this.root = null;
    }

    /**
     * Create a new functional Go lexical scope.
     * 
     * @param current the current node, or <code>null</code> when it would be above the block
     * @param block a nearest block enclosing the current node
     * @param root a functional root node for top-most block
     *
    private GoLexicalScope(Node current, GoBlockNode block, RootNode root) {
        this.current = current;
        this.block = block;
        this.parentBlock = null;
        this.root = root;
    }

    @SuppressWarnings("all") // The parameter node should not be assigned
    public static GoLexicalScope createScope(Node node) {
        GoBlockNode block = getParentBlock(node);
        if (block == null) {
            // We're in the root.
            block = findChildrenBlock(node);
            if (block == null) {
                // Corrupted Go AST, no block was found
                return null;
            }
            node = null; // node is above the block
        }
        // Test if there is a parent block. If not, we're in the root scope.
        GoBlockNode parentBlock = getParentBlock(block);
        if (parentBlock == null) {
            return new GoLexicalScope(node, block, block.getRootNode());
        } else {
            return new GoLexicalScope(node, block, parentBlock);
        }
    }

    private static GoBlockNode getParentBlock(Node node) {
        GoBlockNode block;
        Node parent = node.getParent();
        // Find a nearest block node.
        while (parent != null && !(parent instanceof GoBlockNode)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            block = (GoBlockNode) parent;
        } else {
            block = null;
        }
        return block;
    }

    private static GoBlockNode findChildrenBlock(Node node) {
        GoBlockNode[] blockPtr = new GoBlockNode[1];
        node.accept(new NodeVisitor() {
            @Override
            public boolean visit(Node n) {
                if (n instanceof GoBlockNode) {
                    blockPtr[0] = (GoBlockNode) n;
                    return false;
                } else {
                    return true;
                }
            }
        });
        return blockPtr[0];
    }

    @Override
    protected GoLexicalScope findParent() {
        if (parentBlock == null) {
            // This was a root scope.
            return null;
        }
        if (parent == null) {
            Node node = block;
            GoBlockNode newBlock = parentBlock;
            // Test if there is a next parent block. If not, we're in the root scope.
            GoBlockNode newParentBlock = getParentBlock(newBlock);
            if (newParentBlock == null) {
                parent = new GoLexicalScope(node, newBlock, newBlock.getRootNode());
            } else {
                parent = new GoLexicalScope(node, newBlock, newParentBlock);
            }
        }
        return parent;
    }

    private static Object getInteropValue(Object value) {
        if (value instanceof BigInteger) {
            return new GoBigNumber((BigInteger) value);
        } else {
            return value;
        }
    }

    private static Object getRawValue(Object interopValue, Object oldValue) {
        if (interopValue instanceof GoBigNumber) {
            if (oldValue instanceof BigInteger) {
                return ((GoBigNumber) interopValue).getValue();
            }
        }
        return interopValue;
    }

    /**
     * @return the function name for function scope, "block" otherwise.
     *
    @Override
    public String getName() {
        if (root != null) {
            return root.getName();
        } else {
            return "block";
        }
    }

    /**
     * @return the node representing the scope, the block node for block scopes and the
     *         {@link RootNode} for functional scope.
     *
    @Override
    protected Node getNode() {
        if (root != null) {
            return root;
        } else {
            return block;
        }
    }

    @Override
    public Object getVariables(Frame frame) {
        Map<String, FrameGoot> vars = getVars();
        Object[] args = null;
        // Use arguments when the current node is above the block
        if (current == null) {
            args = (frame != null) ? frame.getArguments() : null;
        }
        return new VariablesMapObject(vars, args, frame);
    }

    @Override
    public Object getArguments(Frame frame) {
        if (root == null) {
            // No arguments for block scope
            return null;
        }
        // The Goots give us names of the arguments:
        Map<String, FrameGoot> argGoots = collectArgs(block);
        // The frame's arguments array give us the argument values:
        Object[] args = (frame != null) ? frame.getArguments() : null;
        // Create a TruffleObject having the arguments as properties:
        return new VariablesMapObject(argGoots, args, frame);
    }

    private Map<String, FrameGoot> getVars() {
        if (varGoots == null) {
            if (current != null) {
                varGoots = collectVars(block, current);
            } else {
                // Provide the arguments only when the current node is above the block
                varGoots = collectArgs(block);
            }
        }
        return varGoots;
    }

    private boolean hasParentVar(String name) {
        GoLexicalScope p = this;
        while ((p = p.findParent()) != null) {
            if (p.getVars().containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, FrameGoot> collectVars(Node varsBlock, Node currentNode) {
        // Variables are Goot-based.
        // To collect declared variables, traverse the block's AST and find Goots associated
        // with GoWriteLocalVariableNode. The traversal stops when we hit the current node.
        Map<String, FrameGoot> Goots = new LinkedHashMap<>(1 << 2);
        NodeUtil.forEachChild(varsBlock, new NodeVisitor() {
            @Override
            public boolean visit(Node node) {
                if (node == currentNode) {
                    return false;
                }
                // Do not enter any nested blocks.
                if (!(node instanceof GoBlockNode)) {
                    boolean all = NodeUtil.forEachChild(node, this);
                    if (!all) {
                        return false;
                    }
                }
                // Write to a variable is a declaration unless it exists already in a parent scope.
                if (node instanceof GoWriteLocalVariableNode) {
                    GoWriteLocalVariableNode wn = (GoWriteLocalVariableNode) node;
                    String name = Objects.toString(wn.getGoot().getIdentifier());
                    if (!hasParentVar(name)) {
                        Goots.put(name, wn.getGoot());
                    }
                }
                return true;
            }
        });
        return Goots;
    }

    private static Map<String, FrameGoot> collectArgs(Node block) {
        // Arguments are pushed to frame Goots at the beginning of the function block.
        // To collect argument Goots, search for GoReadArgumentNode inside of
        // GoWriteLocalVariableNode.
        Map<String, FrameGoot> args = new LinkedHashMap<>(1 << 2);
        NodeUtil.forEachChild(block, new NodeVisitor() {

            private GoWriteLocalVariableNode wn; // The current write node containing a Goot

            @Override
            public boolean visit(Node node) {
                // When there is a write node, search for GoReadArgumentNode among its children:
                if (node instanceof GoWriteLocalVariableNode) {
                    wn = (GoWriteLocalVariableNode) node;
                    boolean all = NodeUtil.forEachChild(node, this);
                    wn = null;
                    return all;
                } else if (wn != null && (node instanceof GoReadArgumentNode)) {
                    FrameGoot Goot = wn.getGoot();
                    String name = Objects.toString(Goot.getIdentifier());
                    assert !args.containsKey(name) : name + " argument exists already.";
                    args.put(name, Goot);
                    return true;
                } else if (wn == null && (node instanceof GoStatementNode)) {
                    // A different Go node - we're done.
                    return false;
                } else {
                    return NodeUtil.forEachChild(node, this);
                }
            }
        });
        return args;
    }

    static final class VariablesMapObject implements TruffleObject {

        final Map<String, ? extends FrameGoot> Goots;
        final Object[] args;
        final Frame frame;

        private VariablesMapObject(Map<String, ? extends FrameGoot> Goots, Object[] args, Frame frame) {
            this.Goots = Goots;
            this.args = args;
            this.frame = frame;
        }

        @Override
        public ForeignAccess getForeignAccess() {
            return VariablesMapMessageResolutionForeign.ACCESS;
        }

        public static boolean isInstance(TruffleObject obj) {
            return obj instanceof VariablesMapObject;
        }

        @MessageResolution(receiverType = VariablesMapObject.class)
        static final class VariablesMapMessageResolution {

            @Resolve(message = "KEYS")
            abstract static class VarsMapKeysNode extends Node {

                @TruffleBoundary
                public Object access(VariablesMapObject varMap) {
                    return new VariableNamesObject(varMap.Goots.keySet());
                }
            }

            @Resolve(message = "READ")
            abstract static class VarsMapReadNode extends Node {

                @TruffleBoundary
                public Object access(VariablesMapObject varMap, String name) {
                    if (varMap.frame == null) {
                        throw UnsupportedMessageException.raise(Message.READ);
                    }
                    FrameGoot Goot = varMap.Goots.get(name);
                    if (Goot == null) {
                        throw UnknownIdentifierException.raise(name);
                    } else {
                        Object value;
                        if (varMap.args != null && varMap.args.length > Goot.getIndex()) {
                            value = varMap.args[Goot.getIndex()];
                        } else {
                            value = varMap.frame.getValue(Goot);
                        }
                        return getInteropValue(value);
                    }
                }
            }

            @Resolve(message = "WRITE")
            abstract static class VarsMapWriteNode extends Node {

                @TruffleBoundary
                public Object access(VariablesMapObject varMap, String name, Object value) {
                    if (varMap.frame == null) {
                        throw UnsupportedMessageException.raise(Message.WRITE);
                    }
                    FrameGoot Goot = varMap.Goots.get(name);
                    if (Goot == null) {
                        throw UnknownIdentifierException.raise(name);
                    } else {
                        if (varMap.args != null && varMap.args.length > Goot.getIndex()) {
                            Object valueOld = varMap.args[Goot.getIndex()];
                            varMap.args[Goot.getIndex()] = getRawValue(value, valueOld);
                        } else {
                            Object valueOld = varMap.frame.getValue(Goot);
                            varMap.frame.setObject(Goot, getRawValue(value, valueOld));
                        }
                        return value;
                    }
                }
            }

        }
    }

    static final class VariableNamesObject implements TruffleObject {

        final List<String> names;

        private VariableNamesObject(Set<String> names) {
            this.names = new ArrayList<>(names);
        }

        @Override
        public ForeignAccess getForeignAccess() {
            return VariableNamesMessageResolutionForeign.ACCESS;
        }

        public static boolean isInstance(TruffleObject obj) {
            return obj instanceof VariableNamesObject;
        }

        @MessageResolution(receiverType = VariableNamesObject.class)
        static final class VariableNamesMessageResolution {

            @Resolve(message = "HAS_SIZE")
            abstract static class VarNamesHasSizeNode extends Node {

                @SuppressWarnings("unused")
                public Object access(VariableNamesObject varNames) {
                    return true;
                }
            }

            @Resolve(message = "GET_SIZE")
            abstract static class VarNamesGetSizeNode extends Node {

                public Object access(VariableNamesObject varNames) {
                    return varNames.names.size();
                }
            }

            @Resolve(message = "READ")
            abstract static class VarNamesReadNode extends Node {

                @TruffleBoundary
                public Object access(VariableNamesObject varNames, int index) {
                    try {
                        return varNames.names.get(index);
                    } catch (IndexOutOfBoundsException ioob) {
                        throw UnknownIdentifierException.raise(Integer.toString(index));
                    }
                }
            }

        }
    }

}*/