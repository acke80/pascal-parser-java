package com.acke80.pascalParser;

import com.acke80.pascalParser.parser.Parser;
import com.acke80.pascalParser.reader.Reader;
import com.acke80.pascalParser.util.Logger;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;


public class PascalParserApp extends JPanel{

    private boolean running;

    private Reader reader;
    private Parser parser;

    private File pathToFiles;

    public void printMenu(){
        System.out.println("\n-----------------------Menu----------------------");
        System.out.println("Selected path: " + pathToFiles);
        System.out.println("Selected output: " +
                (Logger.getOutputFile() != null ? Logger.getOutputFile() : "console") + "\n");
        System.out.println("*\tType 's' to select path of file(s) to parse");
        System.out.println("*\tType 'o' to select path of output file");
        System.out.println("*\tType 'p' to parse file(s) in selected path");
        System.out.println("-------------------------------------------------");
        System.out.print(">> ");
    }

    private File showFileChooser(){
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select folder");
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showDialog(PascalParserApp.this, "Select");

        return returnVal == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
    }

    public void run(){
        running = true;
        Scanner scanner = new Scanner(System.in);
        String input;

        while(running){
            printMenu();
            input = scanner.nextLine();

            switch(input){
                case "s":
                    pathToFiles = showFileChooser();
                    break;
                case "o":
                    File path = showFileChooser();
                    if(path != null)
                        Logger.setOutputPath(path);
                    break;
                case "p":
                    if(pathToFiles == null){
                        System.out.println("Please specify path first");
                    }else{
                        reader = new Reader(pathToFiles);
                        parser = new Parser();
                        parser.parseAll(reader.getFileMap());
                    }
                    break;
            }
        }
    }



    public static void main(String[] args){
        PascalParserApp ppa = new PascalParserApp();
        ppa.run();
    }
}
