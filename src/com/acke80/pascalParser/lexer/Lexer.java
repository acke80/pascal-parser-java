package com.acke80.pascalParser.lexer;

import com.acke80.pascalParser.util.Logger;

import static com.acke80.pascalParser.lexer.Token.*;

public class Lexer{

    private final String PROGRAM_TEXT;
    private int programIndex;

    private String lexeme = "";

    public Lexer(String programText){
        if(programText.charAt(programText.length() - 1) != $.NAME.charAt(0)){
            PROGRAM_TEXT = programText + $.NAME;
        }else{
            PROGRAM_TEXT = programText;
        }
    }

    public void printProgram(){
        Logger.log("________________________________________________________");
        Logger.log("THE PROGRAM TEXT");
        Logger.log("________________________________________________________");
        Logger.log(PROGRAM_TEXT);
        Logger.log("________________________________________________________");
    }

    public Token getToken(){
        if(programIndex >= PROGRAM_TEXT.length())
            throw new IndexOutOfBoundsException("Program index is out of Program string bounds.");

        /* Whitespace */
        while(PROGRAM_TEXT.charAt(programIndex) == ' ' ||
                PROGRAM_TEXT.charAt(programIndex) == '\r' || PROGRAM_TEXT.charAt(programIndex) == '\n'){
            programIndex++;
        }

        /* Keyword or ID */
        if(Character.isLetter(PROGRAM_TEXT.charAt(programIndex))){
            StringBuilder lexemeStringBuilder = new StringBuilder();
            do{
                lexemeStringBuilder.append(PROGRAM_TEXT.charAt(programIndex++));
            }while(Character.isLetterOrDigit(PROGRAM_TEXT.charAt(programIndex)));

            lexeme = lexemeStringBuilder.toString();
            return Token.keyToToken(lexeme);
        }

        /* Number */
        if(Character.isDigit(PROGRAM_TEXT.charAt(programIndex))){
            StringBuilder lexemeStringBuilder = new StringBuilder();
            do{
                lexemeStringBuilder.append(PROGRAM_TEXT.charAt(programIndex++));
            }while(Character.isDigit(PROGRAM_TEXT.charAt(programIndex)));

            lexeme = lexemeStringBuilder.toString();
            return number;
        }

        /* Symbol */
        if(!Character.isLetterOrDigit(PROGRAM_TEXT.charAt(programIndex))){
           lexeme = Character.toString(PROGRAM_TEXT.charAt(programIndex++));

           if(programIndex < PROGRAM_TEXT.length() && PROGRAM_TEXT.charAt(programIndex) == '='){
                lexeme += Character.toString(PROGRAM_TEXT.charAt(programIndex++));
           }

           return Token.lexemeToToken(lexeme);
        }

        return error;
    }

    public String getLexeme(){
        return lexeme;
    }
}
