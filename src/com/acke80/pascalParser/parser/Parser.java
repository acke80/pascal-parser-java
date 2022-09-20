package com.acke80.pascalParser.parser;

import com.acke80.pascalParser.lexer.Lexer;
import static com.acke80.pascalParser.lexer.Token.*;

import com.acke80.pascalParser.lexer.Token;
import com.acke80.pascalParser.tables.OperationTable;
import com.acke80.pascalParser.tables.SymbolTable;
import com.acke80.pascalParser.util.Logger;

import java.util.HashMap;

public class Parser{

    private Lexer lexer;

    private SymbolTable symbolTable;

    private Token lookahead;

    private boolean parseSuccessful;

    public Parser(){
    }

    public void parseAll(HashMap<String, String> files){
        for(HashMap.Entry<String, String> file : files.entrySet()){
            parse(file.getKey(), file.getValue());
        }
    }

    public void parse(String programName, String programText){
        parseSuccessful = true;

        Logger.log("\ntesting " + programName + "\n");
        lexer = new Lexer(programText);
        lexer.printProgram();

        lookahead = lexer.getToken();
        if(lookahead == $){
            logError("SYNTAX:\t Input file is empty");
            return;
        }

        symbolTable = new SymbolTable();

        programHeader();
        varPart();
        statPart();
        endPart();

        if(parseSuccessful)
            Logger.log("PARSE SUCCESSFUL!");

        symbolTable.printSymbolTable();
    }

    private void logError(String message){
        parseSuccessful = false;
        Logger.log(message);
    }

    private void match(Token token){
        if(lookahead == token){
            lookahead = lexer.getToken();
        }else{
            if(token == id)
                logError("SYNTAX:\tID expected found " + lexer.getLexeme());
            else
                logError(String.format("SYNTAX:\tSymbol expected %s found %s", token.NAME, lexer.getLexeme()));

        }
    }

    private void programHeader(){

        match(program);

        if(lookahead == id)
            symbolTable.setProgramName(lexer.getLexeme());
        else
            symbolTable.setProgramName("???");

        match(id);
        match(lPar);
        match(input);
        match(comma);
        match(output);
        match(rPar);
        match(semicolon);
    }

    private void varPart(){
        match(var);
        varDecList();
    }

    private void varDecList(){
        varDec();

        if(lookahead == id)
            varDecList();
    }

    private void varDec(){
        idList();
        match(colon);
        type();
        match(semicolon);
    }

    private void idList(){
        if(symbolTable.nameExist(lexer.getLexeme())){
            logError("SEMANTIC: ID already declared: " + lexer.getLexeme());
        }else{
            if(lookahead == id)
                symbolTable.addVariableName(lexer.getLexeme());
        }

        match(id);

        if(lookahead == comma){
            match(comma);
            idList();
        }
    }

    private void type(){
        switch(lookahead){
            case integer:
                match(integer);
                symbolTable.setVariablesType(integer);
                break;
            case real:
                match(real);
                symbolTable.setVariablesType(real);
                break;
            case bool:
                match(bool);
                symbolTable.setVariablesType(bool);
                break;
            default:
                symbolTable.setVariablesType(error);
                logError("SYNTAX:\tType name expected found " + lexer.getLexeme());
        }
    }

    private void statPart(){
        match(begin);
        statList();
        match(end);
        match(dot);
    }

    private void statList(){
        stat();

        if(lookahead == semicolon){
            match(semicolon);
            statList();
        }
    }

    private void stat(){
        assignStat();
    }

    private void assignStat(){
        Token type1 = symbolTable.getType(lexer.getLexeme());

        if(lookahead == id && !symbolTable.nameExist(lexer.getLexeme()))
            logError("SEMANTIC: ID NOT declared: " + lexer.getLexeme());

        match(id);
        match(assign);
        Token type2 = expr();

        if(type1 == error || type1 != type2){
            logError(String.format("SEMANTIC: Assign types: %s := %s", type1.NAME, type2.NAME));
        }
    }

    private Token expr(){
        Token type1 = term();

        if(lookahead == add){
            match(add);

            Token type2 = expr();

            return OperationTable.getOperationType(add, type1, type2);
        }

        return type1;
    }

    private Token term(){
        Token type1 = factor();

        if(lookahead == mul){
            match(mul);

            Token type2 = term();

            return OperationTable.getOperationType(mul, type1, type2);
        }

        return type1;
    }

    private Token factor(){
        if(lookahead == lPar){
            match(lPar);
            Token type = expr();
            match(rPar);

            return type;
        }

        return operand();
    }

    private Token operand(){
        if(lookahead == number){
            match(number);
            return integer;
        }

        Token type = undef;

        if(lookahead == id){
            if(symbolTable.nameExist(lexer.getLexeme()))
                type = symbolTable.getType(lexer.getLexeme());
            else
                logError("SEMANTIC: ID NOT declared: " + lexer.getLexeme());

            match(id);
        }else{
            logError("SYNTAX:\tOperand Expected");
            type = error;
        }

        return type;
    }

    private void endPart(){
        if(lookahead == $) return;

        StringBuilder sb = new StringBuilder();
        while(lookahead != $){
            sb.append(lexer.getLexeme()).append(" ");
            lookahead = lexer.getToken();
        }

        logError("SYNTAX:\tExtra symbols after end of parse!\n\t\t" + sb.toString());
    }
}
