package com.acke80.pascalParser.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Reader{

    /* Contains the filename as key and file content as value. */
    private LinkedHashMap<String, String> fileMap = new LinkedHashMap<>();

    public Reader(File dir){
        File[] files = dir.listFiles();
        if(files == null){
            System.out.println("Selected directory is empty!");
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".pas")) {
                try{
                    // JAVA 11
                    //fileMap.put(file.getName(), Files.readString(Path.of(file.getAbsolutePath())));

                    // JAVA 8
                    fileMap.put(file.getName(), new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))));
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public HashMap<String, String> getFileMap(){
        return fileMap;
    }

}
