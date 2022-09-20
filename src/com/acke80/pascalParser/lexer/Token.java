package com.acke80.pascalParser.lexer;

import java.util.HashMap;

public enum Token{

    id(false),
    number(false),
    assign(":=", false),
    undef(false),
    predef(false),
    tempty(false),
    error(false),
    type("typ", false),
    $(false),
    lPar("(", false),
    rPar(")", false),
    mul("*", false),
    add("+", false),
    comma(",", false),
    minus("-", false),
    dot(".", false),
    fwdSlash("/", false),
    colon(":", false),
    semicolon(";", false),
    equal("=", false),

    program(true),
    input(true),
    output(true),
    var(true),
    begin(true),
    end(true),
    bool("boolean", true),
    integer(true),
    real(true);

    public final String NAME;

    private final boolean IS_KEYWORD;

    private static final HashMap<String, Token> keywordMap = new HashMap<>();
    private static final HashMap<String, Token>  tokenMao = new HashMap<>();

    static {
        for(Token t : Token.values())
            if(t.IS_KEYWORD)
                keywordMap.put(t.NAME, t);
            else
                tokenMao.put(t.NAME, t);
    }

    Token(String name, boolean isKeyword){
        NAME = name;
        IS_KEYWORD = isKeyword;
    }

    Token(boolean isKeyword){
        NAME = name();
        IS_KEYWORD = isKeyword;
    }

    public static Token lexemeToToken(String name){
        return tokenMao.getOrDefault(name, keyToToken(name));
    }

    public static Token keyToToken(String name){
        return keywordMap.getOrDefault(name, id);
    }

}
