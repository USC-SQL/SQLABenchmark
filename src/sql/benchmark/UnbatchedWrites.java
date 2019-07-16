package sql.benchmark;

import android.accounts.Account;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;
import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.*;

public class UnbatchedWrites {

    private int rowSize, fieldSize, querySize;


    private long t1, t2, t3, t4, t5, t6;

    private List<List<Long>> time;
    /*
    Antipattern:UnbatchedWrites
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/org.itsbsmaihoefer.rezeptbuch.apk
    CallChain:<org.itsbsmaihoefer.rezeptbuch.MainActivity: void onStart()>@-1->
    <org.itsbsmaihoefer.rezeptbuch.MainActivity: void a(java.lang.String,java.lang.String,int)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>
    Location:<org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>@-1
    Query:[insert into rezept_pos (name, einheit, menge, rezept_id, is_platzhalter) values ('unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string c>!!!', 'unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string d>!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(float)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@78!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@94!!!', '0'), insert into rezept_pos (name, einheit, menge, rezept_id, is_platzhalter) values ('unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string c>!!!', 'unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string d>!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(float)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@78!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@94!!!', '1'), update rezept_pos set name='unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string c>!!!', einheit='unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string d>!!!', menge='unknown@method@<java.lang.string: java.lang.string valueof(float)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@170!!!', rezept_id='unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@192!!!', is_platzhalter='0' where _id='unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@224!!!', update rezept_pos set name='unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string c>!!!', einheit='unknown@field@<org.itsbsmaihoefer.rezeptbuch.fe: java.lang.string d>!!!', menge='unknown@method@<java.lang.string: java.lang.string valueof(float)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@170!!!', rezept_id='unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@192!!!', is_platzhalter='1' where _id='unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(org.itsbsmaihoefer.rezeptbuch.fe)>@-1@224!!!']
    Query Form:[insert into values, update set where]
    Table Form:[[integer, integer, integer, integer, integer, text, text]]
    */
    SQLiteDatabase a;
    public void experiment1(SQLiteDatabase db, Environment exp) {

        String tableCreate = "CREATE TABLE IF NOT EXISTS rezept_pos (_id integer primary key autoincrement, name TEXT, einheit TEXT, menge NUMERIC, rezept_id NUMERIC, sort NUMERIC, is_platzhalter NUMERIC)";
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.a = db;
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    class fe {
        public int a;
        public int b;
        public String c;
        public String d;
        public float e;
        public boolean f;

        public fe(int i, int i2, String str, String str2, float f, boolean z) {
            this.a = i;
            this.b = i2;
            this.c = str;
            this.d = str2;
            this.e = f;
            this.f = z;
        }
    }

    public void c(String str) {
        t1 = System.currentTimeMillis();
        this.a.execSQL(str);
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
    }

    public boolean a(fe feVar) {
        String str = "0";
        if (feVar.f) {
            str = "1";
        }
        c(feVar.a <= 0 ? "INSERT INTO rezept_pos (name, einheit, menge, rezept_id, is_platzhalter) VALUES ('" + feVar.c + "', '" + feVar.d + "', '" + String.valueOf(feVar.e) + "', '" + String.valueOf(feVar.b) + "', '" + str + "'" + ")" : "UPDATE rezept_pos SET name='" + feVar.c + "', einheit='" + feVar.d + "', menge='" + String.valueOf(feVar.e) + "'" + ", rezept_id='" + String.valueOf(feVar.b) + "', is_platzhalter='" + str + "'" + " WHERE _id='" + String.valueOf(feVar.a) + "'");
        return true;
    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();

        List<fe> fes = new ArrayList<>();
        for(int i = 0; i < querySize; i++)
        {
            fes.add(new fe(i,31232,"wefw", "wewbw",11231231,true));
        }
        Iterator it = fes.iterator();
        while (it.hasNext()) {
            fe feVar = (fe) it.next();
            a(feVar);
        }

        return this.time;
    }

    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();

        List<fe> fes = new ArrayList<>();
        for(int i = 0; i < querySize; i++)
        {
            fes.add(new fe(-i,31232,"wefw", "wewbw",11231231,true));
        }
        Iterator it = fes.iterator();
        t3 = System.currentTimeMillis();
        this.a.beginTransaction();
        t4 = System.currentTimeMillis();
        while (it.hasNext()) {
            fe feVar = (fe) it.next();
            a(feVar);
        }
        t5 = System.currentTimeMillis();
        this.a.setTransactionSuccessful();
        this.a.endTransaction();
        t6 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t5, t6, 0);

        return time;
    }


    /*
    Antipattern:UnbatchedWrites
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.cresoltech.trackidz.apk
    CallChain:<com.cresoltech.trackidz.GcmIntentService: void onHandleIntent(android.content.Intent)>@186->
    <com.cresoltech.trackidz.GcmIntentService: void setDayApptime(org.json.JSONObject)>
    Location:<com.cresoltech.trackidz.GcmIntentService: void setDayApptime(org.json.JSONObject)>@338
    Query:[insert or replace into all_package_names values('daytime','unknown@method@<org.json.jsonarray: java.lang.string getstring(int)>@<com.cresoltech.trackidz.gcmintentservice: void setdayapptime(org.json.jsonobject)>@333@324!!!','unknown@dynamic_var@unknown@<com.cresoltech.trackidz.gcmintentservice: void setdayapptime(org.json.jsonobject)>@338@469!!!','0','false','true')]
    Query Form:[insert or replace into]
    Table Form:[[text, text, text, text, text]]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {

        String tableCreate = "create table if not exists all_package_names(app_name varchar,pkg_name varchar,time_limit varchar,total_time varchar,notification varchar,isDailyTime varchar)";
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

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
        String pkgname = "pkg";
        for (int i = 0; i < querySize; i++) {
            t1 = System.currentTimeMillis();
            db.execSQL("insert or replace into all_package_names values('daytime','" + pkgname + "','" + 3600 + "','0','false','true')");
            t2 = System.currentTimeMillis();
            Helper.addTimeStamp(time, t1, t2, 0);
        }

        return time;
    }

    private List<List<Long>> fix2(SQLiteDatabase db) {
        time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        db.beginTransaction();
        t4 = System.currentTimeMillis();
        String pkgname = "pkg";
        for (int i = 0; i < querySize; i++) {
            t1 = System.currentTimeMillis();
            db.execSQL("insert or replace into all_package_names values('daytime','" + pkgname + "','" + 3600 + "','0','false','true')");
            t2 = System.currentTimeMillis();
            Helper.addTimeStamp(time, t1, t2, 0);
        }

        t5 = System.currentTimeMillis();
        db.setTransactionSuccessful();
        db.endTransaction();
        t6 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t5, t6, 0);
        return time;
    }

    /*
    Antipattern:UnbatchedWrites
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/org.itsbsmaihoefer.rezeptbuch.apk
    CallChain:<org.itsbsmaihoefer.rezeptbuch.HerdActivity: void onCreate(android.os.Bundle)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.HerdActivity: void g()>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: java.util.ArrayList e()>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: java.lang.String a(int,boolean)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>
    Location:<org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>@-1
    Query:[insert into admin (var, value) values ('herdunknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: java.lang.string a(int,boolean)>@-1@20!!!text', ''), insert into admin (var, value) values ('herdunknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: java.lang.string a(int,boolean)>@-1@20!!!', '')]
    Query Form:[insert into values]
    Table Form:[[text, text]]
     */
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.a = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS admin (var TEXT, value TEXT)";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public void a(int i, boolean z) {

        String str2 = "herd" + String.valueOf(i);
        c("INSERT INTO admin (var, value) VALUES ('" + str2 + "', '')");
    }

    private List<List<Long>> instance3(SQLiteDatabase db) {
        time = new ArrayList<>();
        for (int i = 0; i <= querySize; i++) {
            a(i, true);
        }
        return time;
    }



    private List<List<Long>> fix3(SQLiteDatabase db) {
        time = new ArrayList<>();
        t3 = System.currentTimeMillis();
        this.a.beginTransaction();
        t4 = System.currentTimeMillis();
        for (int i = 0; i <= querySize; i++) {
            a(i, true);
        }
        t5 = System.currentTimeMillis();
        this.a.setTransactionSuccessful();
        this.a.endTransaction();
        t6 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t5, t6, 0);

        return time;
    }


    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE poi_filters (name text, id integer primary key autoincrement, filterbyname text);";


        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    protected boolean deleteFilter(SQLiteDatabase db, String p) {
        if (db == null) {
            return false;
        }
        t1 = System.currentTimeMillis();
        db.execSQL("DELETE FROM poi_filters WHERE id = ?", new Object[]{p});
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        return true;
    }


    private List<List<Long>> instance4(SQLiteDatabase db) {
        time = new ArrayList<>();

        for(int i = 0; i < querySize; i++)
        {
            deleteFilter(db, String.valueOf(i));
        }
        return time;
    }



    private List<List<Long>> fix4(SQLiteDatabase db) {
        time = new ArrayList<>();
        t3 = System.currentTimeMillis();
        db.beginTransaction();
        t4 = System.currentTimeMillis();
        for(int i = 0; i < querySize; i++)
        {
            deleteFilter(db, String.valueOf(i));
        }
        t5 = System.currentTimeMillis();
        db.setTransactionSuccessful();
        db.endTransaction();
        t6 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t5, t6, 0);
        return time;
    }

}