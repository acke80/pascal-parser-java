package com.acke80.pascalParser.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Logger{

    private static File outputFile;

    private static boolean loggedOneTimeToFile = false;

    private Logger(){
    }

    public static void log(String log){
        if(outputFile == null){
            System.out.println(log);
        }else{
            try{
                if(!outputFile.exists()) outputFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(outputFile, loggedOneTimeToFile);
                fos.write((log + "\n").getBytes());

                fos.flush();
                fos.close();
            }catch(IOException e){
                e.printStackTrace();
            }

            loggedOneTimeToFile = true;
        }
    }

    public static void setOutputPath(File path){
        outputFile = new File(path.getAbsolutePath() + "\\" + "parse.output");
    }

    public static File getOutputFile(){
        return outputFile;
    }

}
