package com.oracle.app.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;


public class GoNodeFactory {
    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }

    /* State while parsing a source unit. */
    private final Source source;
    private final Map<String, GoRootNode> allFunctions;

    /* State while parsing a function. */
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    private List<GoStatementNode> methodNodes;

    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    private final GoLanguage language;

    public GoNodeFactory(GoLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, GoRootNode> getAllFunctions() {
        return allFunctions;
    }
    
    public void flattenBlocks(Iterable<? extends GoStatementNode> bodyNodes, List<GoStatementNode> flattenedNodes) {
        for (GoStatementNode n : bodyNodes) {
            if (n instanceof GoBlockNode) {
                flattenBlocks(((GoBlockNode) n).getStatements(), flattenedNodes);
            } else {
                flattenedNodes.add(n);
            }
        }
    }
    
    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
    }

//    public void startFunction(Token nameToken, int bodyStartPos) {
//        assert functionStartPos == 0;
//        assert functionName == null;
//        assert functionBodyStartPos == 0;
//        assert parameterCount == 0;
//        assert frameDescriptor == null;
//        assert lexicalScope == null;
//
//        functionStartPos = nameToken.charPos;
//        functionName = nameToken.val;
//        functionBodyStartPos = bodyStartPos;
//        frameDescriptor = new FrameDescriptor();
//        methodNodes = new ArrayList<>();
//        startBlock();
//    }
//
//    public void addFormalParameter(Token nameToken) {
//        /*
//         * Method parameters are assigned to local variables at the beginning of the method. This
//         * ensures that accesses to parameters are specialized the same way as local variables are
//         * specialized.
//         */
//        final GoReadArgumentNode readArg = new GoReadArgumentNode(parameterCount);
//        GoExpressionNode assignment = createAssignment(createStringLiteral(nameToken, false), readArg);
//        methodNodes.add(assignment);
//        parameterCount++;
//    }
//
//    public void finishFunction(GoStatementNode bodyNode) {
//        if (bodyNode == null) {
//            // a state update that would otherwise be performed by finishBlock
//            lexicalScope = lexicalScope.outer;
//        } else {
//            methodNodes.add(bodyNode);
//            final int bodyEndPos = bodyNode.getSourceSection().getCharEndIndex();
//            final SourceSection functionSrc = source.createSection(functionStartPos, bodyEndPos - functionStartPos);
//            final GoStatementNode methodBlock = finishBlock(methodNodes, functionBodyStartPos, bodyEndPos - functionBodyStartPos);
//            assert lexicalScope == null : "Wrong scoping of blocks in parser";
//
//            final GoFunctionBodyNode functionBodyNode = new GoFunctionBodyNode(methodBlock);
//            functionBodyNode.setSourceSection(functionSrc);
//            final GoRootNode rootNode = new GoRootNode(language, frameDescriptor, functionBodyNode, functionSrc, functionName);
//            allFunctions.put(functionName, rootNode);
//        }
//
//        functionStartPos = 0;
//        functionName = null;
//        functionBodyStartPos = 0;
//        parameterCount = 0;
//        frameDescriptor = null;
//        lexicalScope = null;
//    }
//
    
//
//    public GoStatementNode finishBlock(List<GoStatementNode> bodyNodes, int startPos, int length) {
//        lexicalScope = lexicalScope.outer;
//
//        if (containsNull(bodyNodes)) {
//            return null;
//        }
//
//        List<GoStatementNode> flattenedNodes = new ArrayList<>(bodyNodes.size());
//        flattenBlocks(bodyNodes, flattenedNodes);
//        for (GoStatementNode statement : flattenedNodes) {
//            SourceSection sourceSection = statement.getSourceSection();
//            if (sourceSection != null && !isHaltInCondition(statement)) {
//                statement.addStatementTag();
//            }
//        }
//        GoBlockNode blockNode = new GoBlockNode(flattenedNodes.toArray(new GoStatementNode[flattenedNodes.size()]));
//        blockNode.setSourceSection(source.createSection(startPos, length));
//        return blockNode;
//    }
//
//    private static boolean isHaltInCondition(GoStatementNode statement) {
//        return (statement instanceof GoIfNode) || (statement instanceof GoWhileNode);
//    }
//
    
//
//    /**
//     * Returns an {@link GoDebuggerNode} for the given token.
//     *
//     * @param debuggerToken The token containing the debugger node's info.
//     * @return A GoDebuggerNode for the given token.
//     */
//    GoStatementNode createDebugger(Token debuggerToken) {
//        final GoDebuggerNode debuggerNode = new GoDebuggerNode();
//        srcFromToken(debuggerNode, debuggerToken);
//        return debuggerNode;
//    }
//
//    /**
//     * Returns an {@link GoBreakNode} for the given token.
//     *
//     * @param breakToken The token containing the break node's info.
//     * @return A GoBreakNode for the given token.
//     */
//    public GoStatementNode createBreak(Token breakToken) {
//        final GoBreakNode breakNode = new GoBreakNode();
//        srcFromToken(breakNode, breakToken);
//        return breakNode;
//    }
//
//    /**
//     * Returns an {@link GoContinueNode} for the given token.
//     *
//     * @param continueToken The token containing the continue node's info.
//     * @return A GoContinueNode built using the given token.
//     */
//    public GoStatementNode createContinue(Token continueToken) {
//        final GoContinueNode continueNode = new GoContinueNode();
//        srcFromToken(continueNode, continueToken);
//        return continueNode;
//    }
//
//    /**
//     * Returns an {@link GoWhileNode} for the given parameters.
//     *
//     * @param whileToken The token containing the while node's info
//     * @param conditionNode The conditional node for this while loop
//     * @param bodyNode The body of the while loop
//     * @return A GoWhileNode built using the given parameters. null if either conditionNode or
//     *         bodyNode is null.
//     */
//    public GoStatementNode createWhile(Token whileToken, GoExpressionNode conditionNode, GoStatementNode bodyNode) {
//        if (conditionNode == null || bodyNode == null) {
//            return null;
//        }
//
//        conditionNode.addStatementTag();
//        final int start = whileToken.charPos;
//        final int end = bodyNode.getSourceSection().getCharEndIndex();
//        final GoWhileNode whileNode = new GoWhileNode(conditionNode, bodyNode);
//        whileNode.setSourceSection(source.createSection(start, end - start));
//        return whileNode;
//    }
//
//    /**
//     * Returns an {@link GoIfNode} for the given parameters.
//     *
//     * @param ifToken The token containing the if node's info
//     * @param conditionNode The condition node of this if statement
//     * @param thenPartNode The then part of the if
//     * @param elsePartNode The else part of the if (null if no else part)
//     * @return An GoIfNode for the given parameters. null if either conditionNode or thenPartNode is
//     *         null.
//     */
//    public GoStatementNode createIf(Token ifToken, GoExpressionNode conditionNode, GoStatementNode thenPartNode, GoStatementNode elsePartNode) {
//        if (conditionNode == null || thenPartNode == null) {
//            return null;
//        }
//
//        conditionNode.addStatementTag();
//        final int start = ifToken.charPos;
//        final int end = elsePartNode == null ? thenPartNode.getSourceSection().getCharEndIndex() : elsePartNode.getSourceSection().getCharEndIndex();
//        final GoIfNode ifNode = new GoIfNode(conditionNode, thenPartNode, elsePartNode);
//        ifNode.setSourceSection(source.createSection(start, end - start));
//        return ifNode;
//    }
//
//    /**
//     * Returns an {@link GoReturnNode} for the given parameters.
//     *
//     * @param t The token containing the return node's info
//     * @param valueNode The value of the return (null if not returning a value)
//     * @return An GoReturnNode for the given parameters.
//     */
//    public GoStatementNode createReturn(Token t, GoExpressionNode valueNode) {
//        final int start = t.charPos;
//        final int length = valueNode == null ? t.val.length() : valueNode.getSourceSection().getCharEndIndex() - start;
//        final GoReturnNode returnNode = new GoReturnNode(valueNode);
//        returnNode.setSourceSection(source.createSection(start, length));
//        return returnNode;
//    }
//
//    /**
//     * Returns the corresponding subclass of {@link GoExpressionNode} for binary expressions. </br>
//     * These nodes are currently not instrumented.
//     *
//     * @param opToken The operator of the binary expression
//     * @param leftNode The left node of the expression
//     * @param rightNode The right node of the expression
//     * @return A subclass of GoExpressionNode using the given parameters based on the given opToken.
//     *         null if either leftNode or rightNode is null.
//     */
//    public GoExpressionNode createBinary(Token opToken, GoExpressionNode leftNode, GoExpressionNode rightNode) {
//        if (leftNode == null || rightNode == null) {
//            return null;
//        }
//
//        final GoExpressionNode result;
//        switch (opToken.val) {
//            case "+":
//                result = GoAddNodeGen.create(leftNode, rightNode);
//                break;
//            case "*":
//                result = GoMulNodeGen.create(leftNode, rightNode);
//                break;
//            case "/":
//                result = GoDivNodeGen.create(leftNode, rightNode);
//                break;
//            case "-":
//                result = GoSubNodeGen.create(leftNode, rightNode);
//                break;
//            case "<":
//                result = GoLessThanNodeGen.create(leftNode, rightNode);
//                break;
//            case "<=":
//                result = GoLessOrEqualNodeGen.create(leftNode, rightNode);
//                break;
//            case ">":
//                result = GoLogicalNotNodeGen.create(GoLessOrEqualNodeGen.create(leftNode, rightNode));
//                break;
//            case ">=":
//                result = GoLogicalNotNodeGen.create(GoLessThanNodeGen.create(leftNode, rightNode));
//                break;
//            case "==":
//                result = GoEqualNodeGen.create(leftNode, rightNode);
//                break;
//            case "!=":
//                result = GoLogicalNotNodeGen.create(GoEqualNodeGen.create(leftNode, rightNode));
//                break;
//            case "&&":
//                result = new GoLogicalAndNode(leftNode, rightNode);
//                break;
//            case "||":
//                result = new GoLogicalOrNode(leftNode, rightNode);
//                break;
//            default:
//                throw new RuntimeException("unexpected operation: " + opToken.val);
//        }
//
//        int start = leftNode.getSourceSection().getCharIndex();
//        int length = rightNode.getSourceSection().getCharEndIndex() - start;
//        result.setSourceSection(source.createSection(start, length));
//
//        return result;
//    }
//
//    /**
//     * Returns an {@link GoInvokeNode} for the given parameters.
//     *
//     * @param functionNode The function being called
//     * @param parameterNodes The parameters of the function call
//     * @param finalToken A token used to determine the end of the sourceSelection for this call
//     * @return An GoInvokeNode for the given parameters. null if functionNode or any of the
//     *         parameterNodes are null.
//     */
//    public GoExpressionNode createCall(GoExpressionNode functionNode, List<GoExpressionNode> parameterNodes, Token finalToken) {
//        if (functionNode == null || containsNull(parameterNodes)) {
//            return null;
//        }
//
//        final GoExpressionNode result = new GoInvokeNode(functionNode, parameterNodes.toArray(new GoExpressionNode[parameterNodes.size()]));
//
//        final int startPos = functionNode.getSourceSection().getCharIndex();
//        final int endPos = finalToken.charPos + finalToken.val.length();
//        result.setSourceSection(source.createSection(startPos, endPos - startPos));
//
//        return result;
//    }
//
//    /**
//     * Returns an {@link GoWriteLocalVariableNode} for the given parameters.
//     *
//     * @param nameNode The name of the variable being assigned
//     * @param valueNode The value to be assigned
//     * @return An GoExpressionNode for the given parameters. null if nameNode or valueNode is null.
//     */
//    public GoExpressionNode createAssignment(GoExpressionNode nameNode, GoExpressionNode valueNode) {
//        if (nameNode == null || valueNode == null) {
//            return null;
//        }
//
//        String name = ((GoStringLiteralNode) nameNode).executeGeneric(null);
//        FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
//        lexicalScope.locals.put(name, frameSlot);
//        final GoExpressionNode result = GoWriteLocalVariableNodeGen.create(valueNode, frameSlot);
//
//        if (valueNode.getSourceSection() != null) {
//            final int start = nameNode.getSourceSection().getCharIndex();
//            final int length = valueNode.getSourceSection().getCharEndIndex() - start;
//            result.setSourceSection(source.createSection(start, length));
//        }
//
//        return result;
//    }
//
//    /**
//     * Returns a {@link GoReadLocalVariableNode} if this read is a local variable or a
//     * {@link GoFunctionLiteralNode} if this read is global. In Go, the only global names are
//     * functions.
//     *
//     * @param nameNode The name of the variable/function being read
//     * @return either:
//     *         <ul>
//     *         <li>A GoReadLocalVariableNode representing the local variable being read.</li>
//     *         <li>A GoFunctionLiteralNode representing the function definition.</li>
//     *         <li>null if nameNode is null.</li>
//     *         </ul>
//     */
//    public GoExpressionNode createRead(GoExpressionNode nameNode) {
//        if (nameNode == null) {
//            return null;
//        }
//
//        String name = ((GoStringLiteralNode) nameNode).executeGeneric(null);
//        final GoExpressionNode result;
//        final FrameSlot frameSlot = lexicalScope.locals.get(name);
//        if (frameSlot != null) {
//            /* Read of a local variable. */
//            result = GoReadLocalVariableNodeGen.create(frameSlot);
//        } else {
//            /* Read of a global name. In our language, the only global names are functions. */
//            result = new GoFunctionLiteralNode(language, name);
//        }
//        result.setSourceSection(nameNode.getSourceSection());
//        return result;
//    }
//
//    public GoExpressionNode createStringLiteral(Token literalToken, boolean removeQuotes) {
//        /* Remove the trailing and ending " */
//        String literal = literalToken.val;
//        if (removeQuotes) {
//            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
//            literal = literal.substring(1, literal.length() - 1);
//        }
//
//        final GoStringLiteralNode result = new GoStringLiteralNode(literal.intern());
//        srcFromToken(result, literalToken);
//        return result;
//    }
//
//    public GoExpressionNode createNumericLiteral(Token literalToken) {
//        GoExpressionNode result;
//        try {
//            /* Try if the literal is small enough to fit into a long value. */
//            result = new GoLongLiteralNode(Long.parseLong(literalToken.val));
//        } catch (NumberFormatException ex) {
//            /* Overflow of long value, so fall back to BigInteger. */
//            result = new GoBigIntegerLiteralNode(new BigInteger(literalToken.val));
//        }
//        srcFromToken(result, literalToken);
//        return result;
//    }
//
//    public GoExpressionNode createParenExpression(GoExpressionNode expressionNode, int start, int length) {
//        if (expressionNode == null) {
//            return null;
//        }
//
//        final GoParenExpressionNode result = new GoParenExpressionNode(expressionNode);
//        result.setSourceSection(source.createSection(start, length));
//        return result;
//    }
//
//    /**
//     * Returns an {@link GoReadPropertyNode} for the given parameters.
//     *
//     * @param receiverNode The receiver of the property access
//     * @param nameNode The name of the property being accessed
//     * @return An GoExpressionNode for the given parameters. null if receiverNode or nameNode is
//     *         null.
//     */
//    public GoExpressionNode createReadProperty(GoExpressionNode receiverNode, GoExpressionNode nameNode) {
//        if (receiverNode == null || nameNode == null) {
//            return null;
//        }
//
//        final GoExpressionNode result = GoReadPropertyNodeGen.create(receiverNode, nameNode);
//
//        final int startPos = receiverNode.getSourceSection().getCharIndex();
//        final int endPos = nameNode.getSourceSection().getCharEndIndex();
//        result.setSourceSection(source.createSection(startPos, endPos - startPos));
//
//        return result;
//    }
//
//    /**
//     * Returns an {@link GoWritePropertyNode} for the given parameters.
//     *
//     * @param receiverNode The receiver object of the property assignment
//     * @param nameNode The name of the property being assigned
//     * @param valueNode The value to be assigned
//     * @return An GoExpressionNode for the given parameters. null if receiverNode, nameNode or
//     *         valueNode is null.
//     */
//    public GoExpressionNode createWriteProperty(GoExpressionNode receiverNode, GoExpressionNode nameNode, GoExpressionNode valueNode) {
//        if (receiverNode == null || nameNode == null || valueNode == null) {
//            return null;
//        }
//
//        final GoExpressionNode result = GoWritePropertyNodeGen.create(receiverNode, nameNode, valueNode);
//
//        final int start = receiverNode.getSourceSection().getCharIndex();
//        final int length = valueNode.getSourceSection().getCharEndIndex() - start;
//        result.setSourceSection(source.createSection(start, length));
//
//        return result;
//    }
//
//    /**
//     * Creates source description of a single token.
//     */
//    private void srcFromToken(GoStatementNode node, Token token) {
//        node.setSourceSection(source.createSection(token.charPos, token.val.length()));
//    }

    /**
     * Checks whether a list contains a null.
     */
    private static boolean containsNull(List<?> list) {
        for (Object e : list) {
            if (e == null) {
                return true;
            }
        }
        return false;
    }

}