package sql.benchmark;

import android.content.ComponentName;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotCaching {

    private int rowSize, fieldSize, querySize;



    long t1, t2, t3, t4;
    List<List<Long>> time;

    /*
    Antipattern:NotCaching
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.excelatlife.happiness.apk
    CallChain1:<com.excelatlife.happiness.Data: void onUpgrade(android.database.sqlite.SQLiteDatabase,int,int)>@50->
    <com.excelatlife.happiness.Data: void onCreate(android.database.sqlite.SQLiteDatabase)>@164->
    <com.excelatlife.happiness.Data: void createQuestions(android.database.sqlite.SQLiteDatabase,java.lang.String,boolean,java.lang.String)>@316->
    <com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>
    Location1:<com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>@349
    Query1:[select _id from scaletable where name = \"agreeableness\"]
    Query Form1:[select from where]
    Table Form1:[[integer, text]]
    CallChain2:<com.excelatlife.happiness.Data: void onUpgrade(android.database.sqlite.SQLiteDatabase,int,int)>@50->
    <com.excelatlife.happiness.Data: void onCreate(android.database.sqlite.SQLiteDatabase)>@165->
    <com.excelatlife.happiness.Data: void createQuestions(android.database.sqlite.SQLiteDatabase,java.lang.String,boolean,java.lang.String)>@316->
    <com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>
    Location2:<com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>@349
    Query2:[select _id from scaletable where name = \"agreeableness\"]
    Query Form2:[select from where]
    Table Form2:[[integer, text]]
     */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "Create TABLE ScaleTable ( _id integer primary key autoincrement, Name STRING)";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        createScale(db, "Optimism");
        createScale(db, "Immoderation");
        createScale(db, "Locus of Control");
        createScale(db, "Caring");
        createScale(db, "Trusting");
        createScale(db, "Likes People");
        createScale(db, "Agreeableness");
        createScale(db, "Emotional Stability");
        createScale(db, "Conscientiousness");
        createScale(db, "Assertiveness");
        createScale(db, "Self-Confidence");
        createScale(db, "Gratefulness");
        createScale(db, "Conformity");
        createScale(db, "Playfulness");

        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public void createScale(SQLiteDatabase db, String name) {
        try {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("Name", name);
            db.insertOrThrow("ScaleTable", null, contentvalues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScaleID(SQLiteDatabase db, String scaleName) {
        int scaleID = 0;

        try {
            t1 = System.currentTimeMillis();
            Cursor c = db.rawQuery("select _id from ScaleTable where Name = \"" + scaleName + "\"", null);
            boolean exist = c.moveToNext();
            t2 = System.currentTimeMillis();
            if (exist) {
                t3 = System.currentTimeMillis();
                scaleID = c.getInt(0);
                t4 = System.currentTimeMillis();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return scaleID;
    }

    private  List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();

        log(getScaleID(db, "Agreeableness"));
        log(getScaleID(db, "Agreeableness"));

        return time;
    }

    private void log(int i) {
    }


    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();

        int id = getScaleID(db, "Agreeableness");
        log(id);
        log(id);
        return time;
    }
    /*
    Antipattern:NotCaching
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.tractorpal.apk
    CallChain1:<com.tractorpal.MachineInfo: void onActivityResult(int,int,android.content.Intent)>@1593->
    <com.tractorpal.async.SyncParseDataMethods: void syncDataToServer()>@70->
    <com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>
    Location1:<com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>@702
    Query1:[select * from  vehicle]
    Query Form1:[select from]
    Table Form1:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
    CallChain2:<com.tractorpal.MachineInfo: void onActivityResult(int,int,android.content.Intent)>@1593->
    <com.tractorpal.async.SyncParseDataMethods: void syncDataToServer()>@172->
    <com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>
    Location2:<com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>@702
    Query2:[select * from  vehicle]
    Query Form2:[select from]
    Table Form2:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */
    SQLiteDatabase db;
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.db = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS  Vehicle (  v_Id Text,  v_Name TEXT,  v_image TEXT ,  v_Serial_Number TEXT ,  v_Last_Service TEXT,  v_Purchased_Date TEXT,  v_Purchase_Price TEXT,  v_Purchase_Place TEXT,  v_Original_MilesHours TEXT,  v_Model_Year TEXT,  v_Other TEXT,  v_Notes TEXT,  v_Date TEXT,  thumb_Profile TEXT, date_created TEXT ,  image_changed TEXT );";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public Cursor getAllMachineTable() {
        Cursor curid = null;
        try {
            curid = db.rawQuery("Select * from  Vehicle", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curid;
    }

    private List<List<Long>> instance2(SQLiteDatabase db) {
        time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor cursor = getAllMachineTable();
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();

        t3 = System.currentTimeMillis();
        do {
            log(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15));
        } while (cursor.moveToNext());
        t4 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        t1 = System.currentTimeMillis();
        cursor = getAllMachineTable();
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        t3 = System.currentTimeMillis();
        do {
            log(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15));
        } while (cursor.moveToNext());
        t4 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        return time;
    }

    private void log(String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15) {

    }


    private List<List<Long>> fix2(SQLiteDatabase d4) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = getAllMachineTable();
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();

        t3 = System.currentTimeMillis();
        do {
            log(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15));
        } while (cursor.moveToNext());
        t4 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        t1 = System.currentTimeMillis();
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        t3 = System.currentTimeMillis();
        do {
            log(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15));
        } while (cursor.moveToNext());
        t4 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        cursor.close();
        return time;
    }

    /*
    Antipattern:NotCaching
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.vansteinengroentjes.apps.ddfive.apk
    CallChain1:<com.vansteinengroentjes.apps.ddfive.f: android.view.View a(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void c(int)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void a(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void a(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean,boolean)>@-1->
    <com.vansteinengroentjes.apps.ddfive.content.d: java.lang.String[] R()>
    Location1:<com.vansteinengroentjes.apps.ddfive.content.d: java.lang.String[] R()>@-1
    Query1:[select distinct name from skill order by name asc]
    Query Form1:[select distinct from order by asc]
    Table Form1:[]
    CallChain2:<com.vansteinengroentjes.apps.ddfive.f: android.view.View a(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void c(int)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void a(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean)>@-1->
    <com.vansteinengroentjes.apps.ddfive.f: void a(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean,boolean)>@-1->
    <com.vansteinengroentjes.apps.ddfive.content.d: java.lang.String[] R()>
    Location2:<com.vansteinengroentjes.apps.ddfive.content.d: java.lang.String[] R()>@-1
    Query2:[select distinct name from skill order by name asc]
    Query Form2:[select distinct from order by asc]
    Table Form2:[]
     */
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table skill (Name String, FacebookToken String, NextTurfClaim integer, FacebookTokenExpiresAt integer, BWWToken String, BWWTokenExpiresAt integer, WingFavorite integer, BeerFavorite integer, SportFavorite integer, IsAtLocation integer );";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT DISTINCT Name FROM skill ORDER BY name ASC", null);
        int columnIndex = rawQuery.getColumnIndex("Name");
        if (rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            t3 = System.currentTimeMillis();
            do {
                log(rawQuery.getString(columnIndex));
            } while (rawQuery.moveToNext());
            t4 = System.currentTimeMillis();
            rawQuery.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        t1 = System.currentTimeMillis();
        rawQuery = db.rawQuery("SELECT DISTINCT Name FROM skill ORDER BY name ASC", null);
        columnIndex = rawQuery.getColumnIndex("Name");
        if (rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            t3 = System.currentTimeMillis();
            do {
                log(rawQuery.getString(columnIndex));
            } while (rawQuery.moveToNext());
            t4 = System.currentTimeMillis();
            rawQuery.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    private void log(String string) {

    }


    private List<List<Long>> fix3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT DISTINCT Name FROM skill ORDER BY name ASC", null);
        int columnIndex = rawQuery.getColumnIndex("Name");
        if (rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            t3 = System.currentTimeMillis();
            do {
                log(rawQuery.getString(columnIndex));
            } while (rawQuery.moveToNext());
            t4 = System.currentTimeMillis();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);

        t1 = System.currentTimeMillis();
        if (rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            t3 = System.currentTimeMillis();
            do {
                log(rawQuery.getString(columnIndex));
            } while (rawQuery.moveToNext());
            t4 = System.currentTimeMillis();
        }
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

}