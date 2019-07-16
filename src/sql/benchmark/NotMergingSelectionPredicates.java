package sql.benchmark;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.lang.reflect.Method;
import java.util.*;

public class NotMergingSelectionPredicates {

    private int rowSize, fieldSize, querySize;

    /*
    NotMergingSelectionPredicates
    CallChain1:<com.excelatlife.happiness.Data: void onUpgrade(android.database.sqlite.SQLiteDatabase,int,int)>@50->
    <com.excelatlife.happiness.Data: void onCreate(android.database.sqlite.SQLiteDatabase)>@260->
    <com.excelatlife.happiness.Data: void addScaleToTest(android.database.sqlite.SQLiteDatabase,java.lang.String,java.lang.String)>@295->
    <com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>
    Location1:<com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>@349
    Query1:[select _id from scaletable where name = \"caring\"]
    CallChain2:<com.excelatlife.happiness.Data: void onUpgrade(android.database.sqlite.SQLiteDatabase,int,int)>@50->
    <com.excelatlife.happiness.Data: void onCreate(android.database.sqlite.SQLiteDatabase)>@266->
    <com.excelatlife.happiness.Data: void addScaleToTest(android.database.sqlite.SQLiteDatabase,java.lang.String,java.lang.String)>@295->
    <com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>
    Location2:<com.excelatlife.happiness.Data: int getScaleID(android.database.sqlite.SQLiteDatabase,java.lang.String)>@349
    Query2:[select _id from scaletable where name = \"assertiveness\"]
    */
    long t1, t2, t3, t4;
    List<List<Long>> time;
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

    private void addScaleToTest(SQLiteDatabase db, String testName, String scaleName) {
        int scaleID = getScaleID(db, scaleName);
        log(scaleID);
    }

    private void log(int i)
    {
        //System.out.println(i);
    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();
        addScaleToTest(db, "Your Happiness Assessment", "Optimism");
        addScaleToTest(db, "Your Happiness Assessment", "Immoderation");
        addScaleToTest(db, "Your Happiness Assessment", "Locus of Control");
        addScaleToTest(db, "Your Happiness Assessment", "Caring");
        addScaleToTest(db, "Your Happiness Assessment", "Trusting");
        addScaleToTest(db, "Your Happiness Assessment", "Likes People");
        addScaleToTest(db, "Your Happiness Assessment", "Agreeableness");
        addScaleToTest(db, "Your Happiness Assessment", "Emotional Stability");
        addScaleToTest(db, "Your Happiness Assessment", "Conscientiousness");
        addScaleToTest(db, "Your Happiness Assessment", "Assertiveness");
        addScaleToTest(db, "Your Happiness Assessment", "Self-Confidence");
        addScaleToTest(db, "Your Happiness Assessment", "Gratefulness");
        addScaleToTest(db, "Your Happiness Assessment", "Conformity");
        addScaleToTest(db, "Your Happiness Assessment", "Playfulness");
        return time;
    }

    public int getScaleIDFix(Cursor c, String scaleName) {
        int scaleID = 0;
        try {
            t3 = System.currentTimeMillis();
            c.moveToFirst();
            do{
                int id = c.getInt(0);
                String Name = c.getString(1);
                if(Name.equals(scaleName))
                {
                    scaleID = id;
                    break;
                }
            } while(c.moveToNext());
            t4 = System.currentTimeMillis();
            Helper.addTimeStamp(time, t3, t4, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scaleID;
    }

    private void addScaleToTestFix(Cursor c, String testName, String scaleName) {
        int scaleID = getScaleIDFix(c, scaleName);
        log(scaleID);
    }

    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor c = db.rawQuery("select _id, Name from ScaleTable where Name in ('Optimism', 'Immoderation', 'Locus of Control', 'Caring', 'Trusting', 'Likes People', 'Agreeableness', 'Emotional Stability', 'Conscientiousness', 'Assertiveness', 'Self-Confidence', 'Gratefulness', 'Conformity', 'Playfulness')", null);
        c.moveToFirst();
        t2 = System.currentTimeMillis();
        addScaleToTestFix(c, "Your Happiness Assessment", "Optimism");
        addScaleToTestFix(c, "Your Happiness Assessment", "Immoderation");
        addScaleToTestFix(c, "Your Happiness Assessment", "Locus of Control");
        addScaleToTestFix(c, "Your Happiness Assessment", "Caring");
        addScaleToTestFix(c, "Your Happiness Assessment", "Trusting");
        addScaleToTestFix(c, "Your Happiness Assessment", "Likes People");
        addScaleToTestFix(c, "Your Happiness Assessment", "Agreeableness");
        addScaleToTestFix(c, "Your Happiness Assessment", "Emotional Stability");
        addScaleToTestFix(c, "Your Happiness Assessment", "Conscientiousness");
        addScaleToTestFix(c, "Your Happiness Assessment", "Assertiveness");
        addScaleToTestFix(c, "Your Happiness Assessment", "Self-Confidence");
        addScaleToTestFix(c, "Your Happiness Assessment", "Gratefulness");
        addScaleToTestFix(c, "Your Happiness Assessment", "Conformity");
        addScaleToTestFix(c, "Your Happiness Assessment", "Playfulness");
        c.close();
        Helper.addTimeStamp(time, t1, t2, 0);

        return time;
    }

    /*
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.carmellimo.limousine.apk
    CallChain1:<com.carmellimo.limousine.BookTrip$i: void onPostExecute(java.lang.Object)>@2830->
    <com.carmellimo.limousine.BookTrip$i: void a(java.lang.String)>@2856->
    <com.carmellimo.limousine.BookTrip: void e(com.carmellimo.limousine.BookTrip)>@124->
    <com.carmellimo.limousine.BookTrip: void O()>@2689->
    <com.carmellimo.limousine.BookTrip: void A()>@1233->
    <com.carmellimo.limousine.BookTrip: void M()>@2625->
    <com.carmellimo.limousine.d.a: java.util.ArrayList a(android.content.Context,java.lang.String)>@18->
    <com.carmellimo.limousine.b.b: java.util.ArrayList a(android.content.Context,java.lang.String)>@28->
    <com.carmellimo.limousine.c.d: java.util.ArrayList c(java.lang.String)>
    Location1:<com.carmellimo.limousine.c.d: java.util.ArrayList c(java.lang.String)>@887
    Query1:[select airlinecode, description from airlinecodes where airportcode='ewr', select airlinecode, description from airlinecodes where airportcode='???']
    Query Form1:[select from where]
    Table Form1:[[text, text, text, text]]
    CallChain2:<com.carmellimo.limousine.BookTrip$i: void onPostExecute(java.lang.Object)>@2830->
    <com.carmellimo.limousine.BookTrip$i: void a(java.lang.String)>@2856->
    <com.carmellimo.limousine.BookTrip: void e(com.carmellimo.limousine.BookTrip)>@124->
    <com.carmellimo.limousine.BookTrip: void O()>@2689->
    <com.carmellimo.limousine.BookTrip: void A()>@1266->
    <com.carmellimo.limousine.BookTrip: void C()>@1295->
    <com.carmellimo.limousine.BookTrip: void M()>@2623->
    <com.carmellimo.limousine.d.a: java.util.ArrayList a(android.content.Context,java.lang.String)>@18->
    <com.carmellimo.limousine.b.b: java.util.ArrayList a(android.content.Context,java.lang.String)>@28->
    <com.carmellimo.limousine.c.d: java.util.ArrayList c(java.lang.String)>
    Location2:<com.carmellimo.limousine.c.d: java.util.ArrayList c(java.lang.String)>@887
    Query2:[select airlinecode, description from airlinecodes where airportcode='???', select airlinecode, description from airlinecodes where airportcode='jfk']
    Query Form2:[select from where]
    Table Form2:[[text, text, text, text]]
     */
    SQLiteDatabase c;

    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.c = db;
        String tableCreate = "CREATE TABLE AirlineCodes (airlinecode VARCHAR DEFAULT null, description VARCHAR DEFAULT null, intVal VARCHAR, airportcode VARCHAR)";


        MySQLiteHelper.populate(db, tableCreate, rowSize / 2, fieldSize, null);

        char[] value = new char[fieldSize];
        Arrays.fill(value, 'a');
        ContentValues cv1 = new ContentValues();
        cv1.put("description", new String(value));
        cv1.put("intVal", new String(value));
        cv1.put("airlinecode", new String(value));
        cv1.put("airportcode", "jfk");
        ContentValues cv2 = new ContentValues();
        cv2.put("description", new String(value));
        cv2.put("intVal", new String(value));
        cv2.put("airlinecode", new String(value));
        cv2.put("airportcode", "ewr");
        db.beginTransaction();
        for(int i = 0; i < rowSize/10; i++)
        {
            db.insert("AirlineCodes", null, cv1);
            db.insert("AirlineCodes", null, cv2);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }
    public void c(String str) {
        Cursor cursor = null;

        try {
            t1 = System.currentTimeMillis();
            cursor = this.c.rawQuery("select airlinecode, description from AirlineCodes where airportcode='" + str + "'", null);
            if (cursor.moveToFirst()) {
                t2 = System.currentTimeMillis();
                do {
                     log(cursor.getString(cursor.getColumnIndex("airlinecode")), cursor.getString(cursor.getColumnIndex("description")));
                } while (cursor.moveToNext());
                t3 = System.currentTimeMillis();
                Helper.addTimeStamp(time, t1, t2, 0);
                Helper.addTimeStamp(time, t2, t3, 1);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

            if (cursor != null) {
                cursor.close();
            }

        }
    }

    private List<List<Long>> instance2(SQLiteDatabase db) {
        time = new ArrayList<>();
        c("ewr");
        c("jfk");
        return time;
    }

    private void log(String a, String b) {
        //System.out.println(a + " " + b);
    }


    private List<List<Long>> fix2(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.c.rawQuery("select airportcode, airlinecode, description from AirlineCodes where airportcode in ('jfk','ewr')", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                String airportcode = cursor.getString(cursor.getColumnIndex("airportcode"));
                if(airportcode.equals("jfk"))
                {
                    log(cursor.getString(cursor.getColumnIndex("airlinecode")), cursor.getString(cursor.getColumnIndex("description")));
                }
                else if(airportcode.equals("ewr"))
                {
                    log(cursor.getString(cursor.getColumnIndex("airlinecode")), cursor.getString(cursor.getColumnIndex("description")));
                }
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }
}