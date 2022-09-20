package com.acke80.pascalParser.tables;

import com.acke80.pascalParser.lexer.Token;
import com.acke80.pascalParser.util.Logger;

import static com.acke80.pascalParser.lexer.Token.*;

import java.util.ArrayList;


public class SymbolTable{

    private ArrayList<Tuple> rows = new ArrayList<>();

    private int idListStartIndex;
    private int idListCounter;

    private int addressPtr;
    private int startPtr;

    public SymbolTable(){
        rows.add(new Tuple(predef.NAME, type, predef, 0, 0));
        rows.add(new Tuple(undef.NAME, type, predef, 0, 0));
        rows.add(new Tuple(error.NAME, type, predef, 0, 0));

        rows.add(new Tuple(integer.NAME, type, predef, 4, 0));
        rows.add(new Tuple(bool.NAME, type, predef, 4, 0));
        rows.add(new Tuple(real.NAME, type, predef, 8, 0));
    }

    public void setProgramName(String name){
        rows.add(new Tuple(name, program, program, 0, 0));
        startPtr = rows.size() - 1;
    }

    private int getTypeSize(Token type){
        for(int i = 0; i < startPtr; i++){
            Tuple row = rows.get(i);

            if(row.name.equals(type.NAME))
                return row.size;
        }
        return 0;
    }

    public void printSymbolTable(){
        Logger.log("_____________________________________________________");
        Logger.log("_____________________________________________________");
        Logger.log(" THE SYMBOL TABLE");
        Logger.log("_____________________________________________________");
        Logger.log("\tNAME\tROLE\tTYPE\tSIZE\tADDR");
        Logger.log("_____________________________________________________");

        for(int i = startPtr; i < rows.size(); i++){
            Tuple row = rows.get(i);
            Logger.log(String.format("\t%s\t%s\t%s\t%d\t%d",
                    row.name, row.role.NAME, row.type.NAME, row.size, row.address));
        }

        Logger.log("_____________________________________________________");
        Logger.log(String.format("\tSTATIC STORAGE REQUIRED is %d BYTES", addressPtr));
        Logger.log("_____________________________________________________");
    }

    public void addVariableName(String name){
        rows.add(new Tuple(name, var, undef, 0, 0));

        if(idListCounter == 0)
            idListStartIndex = rows.size() - 1;

        idListCounter++;
    }

    public boolean nameExist(String name){
        for(Tuple t : rows){
            if(t.name.equals(name))
                return true;
        }
        return false;
    }

    public void setVariablesType(Token type){
        for(int i = idListStartIndex; i < idListStartIndex + idListCounter; i++){
            Tuple row = rows.get(i);

            row.type = type;
            row.size = getTypeSize(row.type);
            row.address = addressPtr;
            addressPtr += row.size;
        }

        rows.get(startPtr).size = addressPtr; /* Set program size  */
        idListCounter = 0;
    }

    public Token getType(String name){
        for(Tuple t : rows){
            if(t.name.equals(name))
                return t.type;
        }
        return error;
    }

    /* Defines a row in the Symbol table */
    private static class Tuple{
        public String name;
        public Token role;
        public Token type;
        public int size;
        public int address;

        public Tuple(String name, Token role, Token type, int size, int address){
            this.name = name;
            this.role = role;
            this.type = type;
            this.size = size;
            this.address = address;
        }
    }
}
