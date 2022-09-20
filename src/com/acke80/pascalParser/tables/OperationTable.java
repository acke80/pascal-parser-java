package com.acke80.pascalParser.tables;

import com.acke80.pascalParser.lexer.Token;
import static com.acke80.pascalParser.lexer.Token.*;

public class OperationTable{

    private static Tuple[] rows = {
            new Tuple(add,  integer,    integer,    integer),
            new Tuple(add,  real,       real,       real),
            new Tuple(add,  integer,    real,       real),
            new Tuple(add,  real,       integer,    real),
            new Tuple(mul,  integer,    integer,    integer),
            new Tuple(mul,  real,       real,       real),
            new Tuple(mul,  integer,    real,       real),
            new Tuple(mul,  real,       integer,    real),
            new Tuple($,    undef,      undef,      undef)
    };

    private OperationTable(){}

    public static Token getOperationType(Token operator, Token arg1, Token arg2){
        for(Tuple t : rows){
            if(t.OPERATOR != operator) continue;

            if(t.ARG_1 == arg1 && t.ARG_2 == arg2){
                return t.RESULT;
            }
        }

        return undef;
    }

    private static class Tuple{
        public final Token OPERATOR;
        public final Token ARG_1, ARG_2;
        public final Token RESULT;

        public Tuple(Token operator, Token arg1, Token arg2, Token result){
            OPERATOR = operator;
            ARG_1 = arg1;
            ARG_2 = arg2;
            RESULT = result;
        }
    }
}
