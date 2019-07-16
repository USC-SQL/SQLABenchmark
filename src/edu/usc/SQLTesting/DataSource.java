package edu.usc.SQLTesting;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import sql.benchmark.*;


public class DataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private List<Comment> comments = new ArrayList<>();


    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);


        //UnbatchedWrites.runExperiment(db);
        //UnnecessaryColumnRetrieval.runExperiment(db);

        //LoopToJoin.runExperiment(dbHelper.getWritableDatabase());
        //NotCaching.runExperiment(db);
        //NotUsingParamerizedQuery.runExperiment(db);
    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

    }

    public SQLiteDatabase getSQLiteDatabase()
    {
        return dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public void setDatabase(SQLiteDatabase sql)
    {
        this.database = sql;
    }


}
