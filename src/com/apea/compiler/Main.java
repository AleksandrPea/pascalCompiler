package com.apea.compiler;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        List<String> argList = Arrays.asList(args);
        Reader reader = null;
        Writer writer = null;

        if (args.length == 0) {
            reader = new InputStreamReader(System.in, "UTF-8");
            writer = new OutputStreamWriter(System.out, "UTF-8");
        } else if (args.length == 1) {
            reader = new FileReader(args[0]);
            writer = new OutputStreamWriter(System.out, "UTF-8");
        } else if (args.length == 2) {
            reader = new FileReader(args[0]);
            writer = new FileWriter(args[1]);
        } else {
            System.exit(1);
        }

        Compiler.compile(reader, writer);

        writer.close();
        reader.close();
    }
}
