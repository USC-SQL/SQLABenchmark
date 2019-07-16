package edu.usc.SQLTesting;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Reporter {

    static PrintWriter pwValue;

    static {
        try {
            pwValue = new PrintWriter("/sdcard/value.txt");
            pwValue.print("");
            pwValue.close();
            pwValue = new PrintWriter(new FileWriter("/sdcard/value.txt",true));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void print(Environment exp, List<List<Long>> time)
    {

        StringBuilder sb = new StringBuilder();
        sb.append("Pattern:" + exp.className + " ");
        sb.append("Code_id:" + exp.methodName + " ");
        sb.append("Row_size:" + exp.rowSize + " ");
        sb.append("Field_size:" + exp.fieldSize+ " ");
        sb.append("Query_size:" + exp.querySize + " ");
        sb.append("Is_fixed:" + exp.isFixed + "\n");
        sb.append("Retrieve:" + time.get(0) + "\n");
        sb.append("Iterate:" + time.get(1) + "\n");

        String output = sb.toString();
        System.out.println(output);
        pwValue.println(output);
        pwValue.flush();
    }

    public static void close()
    {
        pwValue.close();
    }

}