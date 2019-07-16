package sql.benchmark;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnboundedQuery {

    private int rowSize, fieldSize, querySize;

    private static final int threshold = 1000;

    private long t1, t2, t3, t4;

    private List<List<Long>> time;
    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.omrup.cell.tracker.apk
    CallChain:<com.omrup.cell.tracker.Activity.Signup_activity: void onCreate(android.os.Bundle)>@55->
    <com.omrup.cell.tracker.Data.Ads: void show_full_screen()>@24->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_buy()>
    Location:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_buy()>@148
    Query:[select user_buy from user]
    Query Form:[select from]
    Table Form:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "CREATE TABLE USER(login text,user_id text,user_name text,user_password text,user_email text,user_type text,user_last_latitude text,user_last_logitude text,user_last_date text,user_last_time text,user_last_address text,user_signal_strength text,user_battery text,user_wifi text,user_mobile text,user_speed text,user_speed_limit text,user_profile text,user_buy text)";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log()
    {

    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select user_buy from USER", null);
        cursor.moveToNext();
        cursor.getString(0);
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "USER");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select user_buy from USER", null);
        cursor.moveToNext();
        cursor.getString(0);
        t2 = System.currentTimeMillis();
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.buffalowildwings.bdubsapp.apk
    CallChain:<com.space150.bww.FightForFandom.UI.FanUp.SportsPageView: void <init>(android.content.Context)>@220->
    <com.space150.bww.FightForFandom.UI.FanUp.SportsPageView: void a()>@257->
    <com.space150.bww.FightForFandom.c.au: void <init>(android.content.Context)>@64->
    <com.space150.bww.FightForFandom.b.h: com.space150.bww.FightForFandom.a.g a()>
    Location:<com.space150.bww.FightForFandom.b.h: com.space150.bww.FightForFandom.a.g a()>@43
    Query:[select * from session]
    Query Form:[select from]
    Table Form:[[integer, integer, integer, integer, integer, integer, integer, text, text, text]]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table Session (FacebookId String, FacebookToken String, NextTurfClaim integer, FacebookTokenExpiresAt integer, BWWToken String, BWWTokenExpiresAt integer, WingFavorite integer, BeerFavorite integer, SportFavorite integer, IsAtLocation integer );";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance2(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("select * from Session", null);
        if (rawQuery.getCount() > 0) {
            rawQuery.moveToFirst();
            do {
                log();
            } while (rawQuery.moveToNext());
        }
        t2 = System.currentTimeMillis();
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix2(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "Session");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("select * from Session", null);
        if (rawQuery.getCount() > 0) {
            rawQuery.moveToFirst();
            do {

            } while (rawQuery.moveToNext());
        }
        t2 = System.currentTimeMillis();
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.bestringtonesapps.notificationsounds.apk
    CallChain:<com.bestringtonesapps.notificationsounds.ExampleAppWidgetProvider: void onDeleted(android.content.Context,int[])>@147->
    <com.bestringtonesapps.notificationsounds.DatabaseHandler: java.util.List getAllContacts()>
    Location:<com.bestringtonesapps.notificationsounds.DatabaseHandler: java.util.List getAllContacts()>@102
    Query:[select  * from widgets]
    Query Form:[select from]
    Table Form:[[integer, text, text]]
     */
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE widgets(id INTEGER PRIMARY KEY AUTOINCREMENT,widgetid TEXT,pozicija TEXT)";

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
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM widgets", null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix3(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "widgets");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM widgets", null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        cursor.close();
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.TWCableTV.apk
    CallChain:<com.smithmicro.titan.android.LicenseTracking: void queueConfigFileUpdateQuery()>@2753->
    <com.smithmicro.titan.android.LicenseTracking: com.smithmicro.titan.android.lt_error a(boolean)>@2384->
    <com.smithmicro.titan.android.Titan: com.smithmicro.titan.android.titan_error titan_send(boolean)>@4375->
    <com.smithmicro.titan.android.EventManager: java.lang.String LoadDataToSend()>
    Location:<com.smithmicro.titan.android.EventManager: java.lang.String LoadDataToSend()>@1337
    Query:[select mesg from eventlogs]
    Query Form:[select from]
    Table Form:[[integer, text]]
     */
    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE IF NOT EXISTS " + this.z + " (id INTEGER PRIMARY KEY AUTOINCREMENT, mesg TEXT);";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    String z = "eventlogs";
    private List<List<Long>> instance4(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        String str2 = "SELECT mesg FROM " + this.z;
        Cursor rawQuery = db.rawQuery(str2, null);
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix4(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, this.z);
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        String str2 = "SELECT mesg FROM " + this.z;
        Cursor rawQuery = db.rawQuery(str2, null);
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.carmellimo.limousine.apk
    CallChain:<com.carmellimo.limousine.OnFileCreditCard: void onCreate(android.os.Bundle)>@194->
    <com.carmellimo.limousine.b.m: void a(android.content.Context)>@35->
    <com.carmellimo.limousine.c.d: java.util.ArrayList h()>
    Location:<com.carmellimo.limousine.c.d: java.util.ArrayList h()>@732
    Query:[select * from cardtype]
    Query Form:[select from]
    Table Form:[[text, text]]
     */

    public void experiment5(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE CardType (CardCode VARCHAR DEFAULT null, CardType VARCHAR DEFAULT null)";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance5(db);
        else
            time = fix5(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance5(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select * from CardType", null);
        while (cursor.moveToNext()) {
            cursor.getString(cursor.getColumnIndex("CardType"));
            cursor.getString(cursor.getColumnIndex("CardCode"));
        }
        t2 = System.currentTimeMillis();
        if (cursor != null) {
            cursor.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix5(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "CardType");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select * from CardType", null);
        while (cursor.moveToNext()) {
            cursor.getString(cursor.getColumnIndex("CardType"));
            cursor.getString(cursor.getColumnIndex("CardCode"));
        }
        t2 = System.currentTimeMillis();
        if (cursor != null) {
            cursor.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.incognisys.ethicalhackingpro.apk
    CallChain:<com.incognisys.ethicalhackingpro.FavoriteActivity: void onCreate(android.os.Bundle)>@63->
    <com.incognisys.ethicalhackingpro.DbHandler: java.util.List getFavs()>
    Location:<com.incognisys.ethicalhackingpro.DbHandler: java.util.List getFavs()>@80
    Query:[select  * from tutorialstbl order by id desc]
    Query Form:[select from order by desc]
    Table Form:[[integer, text, text, text]]
     */
    public void experiment6(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "Create TABLE IF NOT EXISTS TutorialsTbl(id INTEGER,title TEXT, category TEXT, isfavorite TEXT);";


        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance6(db);
        else
            time = fix6(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance6(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM TutorialsTbl order by id desc", null);

        if (cursor.moveToFirst()) {
            do {
                cursor.getString(0);
                cursor.getString(1);
                cursor.getString(2);
                cursor.getString(3);

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix6(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "TutorialsTbl");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM TutorialsTbl order by id desc", null);

        if (cursor.moveToFirst()) {
            do {
                cursor.getString(0);
                cursor.getString(1);
                cursor.getString(2);
                cursor.getString(3);

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/org.itsbsmaihoefer.rezeptbuch.apk
    CallChain:<org.itsbsmaihoefer.rezeptbuch.EditRezeptActivity: void onCreate(android.os.Bundle)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: void <init>(android.content.Context)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: java.util.ArrayList i()>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: android.database.Cursor d(java.lang.String)>
    Location:<org.itsbsmaihoefer.rezeptbuch.ey: android.database.Cursor d(java.lang.String)>@-1
    Query:[select name from kategorie order by name asc]
    Query Form:[select from order by asc]
    Table Form:[[integer, integer, integer, text]]
     */
    SQLiteDatabase a;
    public void experiment7(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.a = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS kategorie (_id integer primary key autoincrement, name TEXT, color_id NUMERIC, pic_id NUMERIC)";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance7(db);
        else
            time = fix7(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public Cursor d(String str) {
        return this.a.rawQuery(str, null);
    }

    private List<List<Long>> instance7(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor d = d("SELECT name FROM kategorie ORDER BY name ASC");
        while (d.moveToNext()) {
            d.getString(0);
        }
        t2 = System.currentTimeMillis();
        d.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix7(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "kategorie");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        Cursor d = d("SELECT name FROM kategorie ORDER BY name ASC");
        while (d.moveToNext()) {
            d.getString(0);
        }
        t2 = System.currentTimeMillis();
        d.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }


    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.tractorpal.apk
    CallChain:<com.tractorpal.Home: void onClick(android.view.View)>@371->
    <com.tractorpal.async.SyncParseDataMethods: void syncDataToServer()>@172->
    <com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>
    Location:<com.database.DatabaseHandler: android.database.Cursor getAllMachineTable()>@702
    Query:[select * from  vehicle]
    Query Form:[select from]
    Table Form:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */

    public void experiment8(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE IF NOT EXISTS  Vehicle (  v_Id Text,  v_Name TEXT,  v_image TEXT ,  v_Serial_Number TEXT ,  v_Last_Service TEXT,  v_Purchased_Date TEXT,  v_Purchase_Price TEXT,  v_Purchase_Place TEXT,  v_Original_MilesHours TEXT,  v_Model_Year TEXT,  v_Other TEXT,  v_Notes TEXT,  v_Date TEXT,  thumb_Profile TEXT, date_created TEXT ,  image_changed TEXT );";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance8(db);
        else
            time = fix8(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance8(SQLiteDatabase db) {
        time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor curid = null;
        try {
            curid = db.rawQuery("Select * from  Vehicle", null);
            while (curid.moveToNext()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t2 = System.currentTimeMillis();
        if (curid != null) {
            curid.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private List<List<Long>> fix8(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "Vehicle");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        Cursor curid = null;
        try {
            curid = db.rawQuery("Select * from  Vehicle", null);
            while (curid.moveToNext()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t2 = System.currentTimeMillis();
        if (curid != null) {
            curid.close();
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }

    /*
    Antipattern:UnboundedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.prestigeone.mobile.android.apk
    CallChain:<com.limosys.limosystestmqtt.MQTTServiceDelegate: java.util.List getAllTopicsFromDB(android.content.Context)>@141->
    <com.limosys.database.TopicsDataSource: java.util.List getAllTopics()>
    Location:<com.limosys.database.TopicsDataSource: java.util.List getAllTopics()>@58
    Query:[select * from topics]
    Query Form:[select from]
    Table Form:[[text]]
     */

    public void experiment9(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table topics(topic text not null);";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance9(db);
        else
            time = fix9(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>>instance9(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM topics", null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);

        return time;
    }

    private List<List<Long>> fix9(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        long count = DatabaseUtils.queryNumEntries(db, "topics");
        if(count > threshold)
        {
            log();
        }
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM topics", null);
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;

    }
}