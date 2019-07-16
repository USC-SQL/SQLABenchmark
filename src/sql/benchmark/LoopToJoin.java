package sql.benchmark;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoopToJoin {


    private int rowSize, fieldSize, querySize;

    /*
    /home/yingjun/Documents/SQLUsage/appset/1000apps/com.andromo.dev231870.app482676.apk contains LoopToJoin num:1
    Antipattern:LoopToJoin
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.andromo.dev231870.app482676.apk
    CallChain1:<com.andromo.dev231870.app482676.RSSFeedProvider: boolean onCreate()>@-1->
    <com.andromo.dev231870.app482676.dc: void <init>(android.content.Context)>@-1->
    <com.andromo.dev231870.app482676.dc: void b()>
    Location1:<com.andromo.dev231870.app482676.dc: void b()>@-1
    Query1:[select * from rssfeed_channel]
    Query Form1:[select from]
    Table Form1:[[integer, text, text, text]]
    CallChain2:<com.andromo.dev231870.app482676.RSSFeedProvider: boolean onCreate()>@-1->
    <com.andromo.dev231870.app482676.dc: void <init>(android.content.Context)>@-1->
    <com.andromo.dev231870.app482676.dc: void b()>
    Location2:<com.andromo.dev231870.app482676.dc: void b()>@-1
    Query2:[select * from rssfeed_entry where _id in( select _id from rssfeed_entry where channel_id=unknown@dynamic_var@$l0@<com.andromo.dev231870.app482676.dc: void b()>@-1@61!!! order by pubdate desc limit -1 offset 100)]
    Query Form2:[select from where select from where order by desc limit offset]
    Table Form2:[]
     */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate1 = "CREATE TABLE rssfeed_channel (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,link TEXT,last_update TEXT);";
        String tableCreate2 = "CREATE TABLE rssfeed_entry (_id INTEGER PRIMARY KEY AUTOINCREMENT, channel_id INTEGER,title TEXT,description TEXT,summary TEXT,content TEXT,link TEXT,pubdate TEXT,updated TEXT,author TEXT,thumb TEXT,has_been_read boolean,media_link TEXT,guid TEXT,last_update TEXT,_data TEXT);";

        MySQLiteHelper.populate(db, tableCreate1, rowSize, fieldSize, null);

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("channel_id", "2");
        MySQLiteHelper.populate(db, tableCreate2, rowSize, fieldSize, specifyColumn);

        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate1);
        MySQLiteHelper.dropTable(db, tableCreate2);
        Reporter.print(exp, time);

    }


    private List<List<Long>> instance1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        Cursor cursor = null;
        long t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT * FROM rssfeed_channel", null);
        rawQuery.moveToFirst();
        long t2 = System.currentTimeMillis();

        long t3 = System.currentTimeMillis();
        Cursor cursor2 = null;
        do {
            long j = rawQuery.getLong(0);
            cursor2 = db.rawQuery("SELECT * FROM rssfeed_entry WHERE _id in( SELECT _id FROM rssfeed_entry WHERE channel_id=" + j + " ORDER BY pubdate DESC LIMIT -1 OFFSET 100)", null);
            boolean exists = cursor2.moveToFirst();
            if(exists) {
                do {
                    int columnIndex = cursor2.getColumnIndex("_data");
                    if (columnIndex != -1) {
                        cursor2.getString(columnIndex);
                    }
                } while (cursor2.moveToNext());
            }
            cursor2.close();

        } while (rawQuery.moveToNext());
        long t4 = System.currentTimeMillis();
        cursor = cursor2;

        if (rawQuery != null) {
            rawQuery.close();
        }
        if (cursor != null) {
            cursor.close();
        }


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }


    private  List<List<Long>> fix1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        Cursor cursor = null;
        Cursor cursor2 = null;

        long t1 = System.currentTimeMillis();

        cursor2 = db.rawQuery("SELECT * FROM rssfeed_entry WHERE _id in( SELECT rssfeed_entry._id FROM rssfeed_entry INNER JOIN rssfeed_channel ON rssfeed_entry.channel_id = rssfeed_channel._id ORDER BY pubdate DESC LIMIT -1 OFFSET 100)", null);
        boolean exists = cursor2.moveToFirst();
        long t2 = System.currentTimeMillis();

        long t3 = System.currentTimeMillis();
        if(exists) {
            do {
                int columnIndex = cursor2.getColumnIndex("_data");
                if (columnIndex != -1) {
                    cursor2.getString(columnIndex);
                }
            } while (cursor2.moveToNext());
        }
        long t4 = System.currentTimeMillis();

        cursor = cursor2;

        if (cursor != null) {
            cursor.close();
        }

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

}