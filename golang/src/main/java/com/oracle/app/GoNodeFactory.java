package com.oracle.app.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.GoLanguage;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;


public class GoNodeFactory {
/*
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

    /* State while parsing a source unit. *
    private final Source source;
    private final Map<String, GoRootNode> allFunctions;

    /* State while parsing a function. *
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    private List<GoStatementNode> methodNodes;

    /* State while parsing a block. *
    private LexicalScope lexicalScope;
    private final GoLanguage language; */

	public GoNodeFactory(GoLanguage language, Source source) {
		this.language = language;
		this.source = source;
		this.allFunctions = new Hashmap<>();
	}
}