package edu.usc.SQLTesting;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;


public class Comment {
    private long id;
    private String comment;
    private static Map<Bitmap,Integer> bm;
    public static Map<Bitmap,Integer> getInstance()
    {
        if(bm == null)
            bm = new HashMap<>();
        return bm;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}
