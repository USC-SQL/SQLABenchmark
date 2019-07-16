package edu.usc.SQLTesting;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static void addTimeStamp(List<List<Long>> time, long start, long end, int index)
    {
        if(time.isEmpty())
        {
            time.add(new ArrayList<>());
            time.add(new ArrayList<>());
        }
        time.get(index).add(start);
        time.get(index).add(end);
    }
}