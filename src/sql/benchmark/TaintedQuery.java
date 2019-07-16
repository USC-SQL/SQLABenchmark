package sql.benchmark;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.TextUtils;
import edu.usc.SQLTesting.*;
import net.sf.jsqlparser.schema.Table;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.SecurityConfiguration;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.reference.DefaultSecurityConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaintedQuery {
    private int rowSize, fieldSize, querySize;

    //SQLite uses the same escaping scheme as Oracle
    Codec sqliteCode = new OracleCodec();

    long t1, t2, t3, t4;
    List<List<Long>> time;
    /*
    Antipattern:TaintedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/org.itsbsmaihoefer.rezeptbuch.apk
    CallChain:<org.itsbsmaihoefer.rezeptbuch.LoginActivity: void onCreate(android.os.Bundle)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: void <init>(android.content.Context)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: boolean a(java.lang.String,int,int)>@-1->
    <org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>
    Location:<org.itsbsmaihoefer.rezeptbuch.ey: void c(java.lang.String)>@-1
    Query:[insert into kategorie (name, color_id, pic_id) values ('', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(java.lang.string,int,int)>@-1@39!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(java.lang.string,int,int)>@-1@53!!!'), insert into kategorie (name, color_id, pic_id) values ('unknown@method@<android.content.context: java.lang.string getstring(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: void <init>(android.content.context)>@-1@117!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(java.lang.string,int,int)>@-1@39!!!', 'unknown@method@<java.lang.string: java.lang.string valueof(int)>@<org.itsbsmaihoefer.rezeptbuch.ey: boolean a(java.lang.string,int,int)>@-1@53!!!')]
    Query Form:[insert into values]
    Table Form:[[integer, integer, integer, text]]
     */

    SQLiteDatabase a;
    public void experiment1(SQLiteDatabase db, Environment exp) {




        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        a = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS kategorie (_id integer primary key autoincrement, name TEXT, color_id NUMERIC, pic_id NUMERIC)";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public void c(String str) {
        this.a.execSQL(str);
    }

    public String e(String str) {
        return str == null ? "" : str.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\|", "").replaceAll(";", "");
    }

    public boolean a(String str, int i, int i2) {
        String e = e(str);
        if (e.length() <= 0) {
            return false;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }

        t1 = System.currentTimeMillis();
        c("INSERT INTO kategorie (name, color_id, pic_id) VALUES ('" + e + "', '" + String.valueOf(i) + "', '" + String.valueOf(i2) + "'" + ")");
        t2 = System.currentTimeMillis();
        return true;
    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();
        a("2131099761", 4, 3);
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    public void cFix(String str, String[] values) {
        this.a.execSQL(str, values);
    }

    public boolean aFix(String str, int i, int i2) {
        String e = e(str);
        if (e.length() <= 0) {
            return false;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }

        String s_i = String.valueOf(i);
        String s_i2 =  String.valueOf(i2);
        t3 = System.currentTimeMillis();
        e = ESAPI.encoder().encodeForSQL(sqliteCode, e);
        s_i = ESAPI.encoder().encodeForSQL(sqliteCode, s_i);
        s_i2 = ESAPI.encoder().encodeForSQL(sqliteCode, s_i2);
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        c("INSERT INTO kategorie (name, color_id, pic_id) VALUES ('" + e + "', '" + s_i + "', '" + s_i2 + "'" + ")");
        t2 = System.currentTimeMillis();
        return true;
    }
    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();
        aFix("2131099761", 4, 3);
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    /*
    Antipattern:TaintedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/mobi.infolife.ezweather.widget.wood_grain.apk
    CallChain:<mobi.infolife.ezweather.widget.common.push.PushIntentReceiver: void onReceive(android.content.Context,android.content.Intent)>
    Location:<mobi.infolife.ezweather.widget.common.push.PushIntentReceiver: void onReceive(android.content.Context,android.content.Intent)>@20
    Query:[update push_table set status = unknown@dynamic_var@$i0@<mobi.infolife.ezweather.widget.common.push.pushintentreceiver: void onreceive(android.content.context,android.content.intent)>@20@43!!! where id = unknown@method@<android.content.intent: java.lang.string getstringextra(java.lang.string)>@<mobi.infolife.ezweather.widget.common.push.pushintentreceiver: void onreceive(android.content.context,android.content.intent)>@17@14!!!]
    Query Form:[update set where]
    Table Form:[[integer, interger]]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE push_table (id INTERGER, status INTEGER)";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("id", id);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }
    public static final String EXTRA_PUSH_ID = "push_id";
    public static final String EXTRA_PUSH_INTENT = "push_intent";
    String id = "10";
    private List<List<Long>> instance2(SQLiteDatabase db) {
        time = new ArrayList<>();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PUSH_ID, id);
        intent.putExtra(EXTRA_PUSH_INTENT, 123124124);
        if (intent != null) {
            String id = intent.getStringExtra(EXTRA_PUSH_ID);
            int status = intent.getIntExtra(EXTRA_PUSH_INTENT, -1);
            if (!TextUtils.isEmpty(id) && status != -1) {
                t1 = System.currentTimeMillis();
                db.execSQL("UPDATE push_table SET status = " + status + " WHERE id = " + id);
                t2 = System.currentTimeMillis();
            }
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        //DatabaseUtils.dumpCursor(db.rawQuery("select * from push_table", null));
        return time;
    }



    private List<List<Long>> fix2(SQLiteDatabase db) {
        time = new ArrayList<>();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PUSH_ID, id);
        intent.putExtra(EXTRA_PUSH_INTENT, 123124124);
        if (intent != null) {
            String id = intent.getStringExtra(EXTRA_PUSH_ID);
            int status = intent.getIntExtra(EXTRA_PUSH_INTENT, -1);
            if (!TextUtils.isEmpty(id) && status != -1) {
                t3 = System.currentTimeMillis();
                String s_status = String.valueOf(status);
                String s_id = String.valueOf(id);
                s_status = ESAPI.encoder().encodeForSQL(sqliteCode, s_status);
                s_id = ESAPI.encoder().encodeForSQL(sqliteCode, s_id);
                t4 = System.currentTimeMillis();
                t1 = System.currentTimeMillis();
                db.execSQL("UPDATE push_table SET status = " + s_status + " WHERE id = " + s_id);
                t2 = System.currentTimeMillis();

            }
        }
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        //DatabaseUtils.dumpCursor(db.rawQuery("select * from push_table", null));
        return time;

    }

    /*
    Antipattern:TaintedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.beauty.makeuycanbbzxxs.apk
    CallChain:<com.aviary.android.feather.cds.CdsDatabaseHelper: void onCreate(android.database.sqlite.SQLiteDatabase)>@241->
    <com.aviary.android.feather.cds.PermissionColumns: void create(android.database.sqlite.SQLiteDatabase)>
    Location:<com.aviary.android.feather.cds.PermissionColumns: void create(android.database.sqlite.SQLiteDatabase)>@36
    Query:[insert or replace into permission_table (perm_value, perm_hash) values ( 'unknown@method@<java.lang.enum: java.lang.string name()>@<com.aviary.android.feather.cds.permissioncolumns: void create(android.database.sqlite.sqlitedatabase)>@36@14!!!,unknown@method@<java.lang.enum: java.lang.string name()>@<com.aviary.android.feather.cds.permissioncolumns: void create(android.database.sqlite.sqlitedatabase)>@36@30!!!', '0' );]
    Query Form:[insert or replace into values]
    Table Form:[[integer, text, text]]
     */

    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE IF NOT EXISTS permission_table (perm_id INTEGER PRIMARY KEY AUTOINCREMENT, perm_value TEXT, perm_hash TEXT NOT NULL DEFAULT 0);";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public enum Permission {
        hires,
        whitelabel,
        kill
    }

    private List<List<Long>> instance3(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        db.execSQL("INSERT OR REPLACE INTO permission_table (perm_value, perm_hash) VALUES ( '" + Permission.hires.name() + "," + Permission.whitelabel.name() + "', '0' );");
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }



    private List<List<Long>> fix3(SQLiteDatabase db) {
        time = new ArrayList<>();
        String hires =  Permission.hires.name();
        String whitelable =  Permission.whitelabel.name();
        t3 = System.currentTimeMillis();
        hires = ESAPI.encoder().encodeForSQL(sqliteCode, hires);
        whitelable = ESAPI.encoder().encodeForSQL(sqliteCode, whitelable);
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        db.execSQL("INSERT OR REPLACE INTO permission_table (perm_value, perm_hash) VALUES ( '" + hires + "," + whitelable + "', '0' );");
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    /*
    Antipattern:TaintedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.hundred.qibla.apk
    CallChain:<com.hundred.qibla.activity.DhikrScreen.DhikrListActivity: void onCreate(android.os.Bundle)>@49->
    <com.hundred.qibla.activity.DhikrScreen.DhikrListActivity: void esmaDhikr()>@202->
    <com.hundred.qibla.data.db.DBHelper: int hasDhikr(java.lang.String)>
    Location:<com.hundred.qibla.data.db.DBHelper: int hasDhikr(java.lang.String)>@259
    Query:[select * from dhikrs where title=\"unknown@method@<android.os.bundle: java.lang.string getstring(java.lang.string)>@<com.hundred.qibla.activity.dhikrscreen.dhikrlistactivity: void esmadhikr()>@202@44!!!\"]
    Query Form:[select from where]
    Table Form:[]
    */
    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "CREATE TABLE dhikrs ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT, date LONG, dhikr_count INTEGER, current_count INTEGER DEFAULT 0, set_count INTEGER DEFAULT 1, rea TEXT, meaning TEXT, arabic TEXT );";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(int id)
    {

    }

    private List<List<Long>> instance4(SQLiteDatabase db) {
        time = new ArrayList<>();

        String title = "abc";

        int dhikrID = -1;
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM dhikrs WHERE title=\"" + title + "\" ", null);
        if (cursor.moveToFirst()) {
            dhikrID = cursor.getInt(cursor.getColumnIndex("id"));
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        log(dhikrID);

        Helper.addTimeStamp(time, t1, t2, 0);

        return time;
    }

    private List<List<Long>> fix4(SQLiteDatabase db) {
        time = new ArrayList<>();
        String title = "abc";

        int dhikrID = -1;
        t3 = System.currentTimeMillis();
        title = ESAPI.encoder().encodeForSQL(sqliteCode, title);
        t4 = System.currentTimeMillis();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM dhikrs WHERE title=\"" + title + "\" ", null);
        if (cursor.moveToFirst()) {
            dhikrID = cursor.getInt(cursor.getColumnIndex("id"));
        }
        t2 = System.currentTimeMillis();
        cursor.close();
        log(dhikrID);
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    SQLiteDatabase db;

    /*
    Antipattern:TaintedQuery
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.tractorpal.apk
    CallChain:<com.tractorpal.Service: void onResume()>@1328->
    <com.tractorpal.Service: void dynamicCall()>@866->
    <com.tractorpal.Service: void saticView()>@357->
    <com.database.DatabaseHandler: android.database.Cursor getEditService(java.lang.String,java.lang.String)>
    Location:<com.database.DatabaseHandler: android.database.Cursor getEditService(java.lang.String,java.lang.String)>@965
    Query:[select * from  editservice where es_machineid ='unknown@method@<android.content.intent: java.lang.string getstringextra(java.lang.string)>@<com.tractorpal.service: void saticview()>@354@16!!!' and es_field_id='0']
    Query Form:[select from where and]
    Table Form:[[text, text, text, text, text, text]]
    */
    public void experiment5(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.db = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS EditService (  es_MachineId TEXT,  es_Field_Id TEXT,  es_Edit_KM TEXT ,  es_EditMonth TEXT ,  es_After_Service_Month TEXT , date_created TEXT );";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("es_MachineId", "123");
        specifyColumn.put("es_Field_Id", "0");
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance5(db);
        else
            time = fix5(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public Cursor getEditService(String es_MachineId, String es_Field_Id) {
        Cursor curid = null;
        try {
            return db.rawQuery("Select * from  EditService where es_MachineId ='" + es_MachineId + "' AND es_Field_Id='" + es_Field_Id + "'", null);
        } catch (Exception e) {
            return curid;
        }
    }

    private List<List<Long>> instance5(SQLiteDatabase db) {
        time = new ArrayList<>();
        Intent intent = new Intent();
        intent.putExtra("Machine_Id", "123");
        try {
            String id = intent.getStringExtra("Machine_Id");
            if (id != null) {
                t1 = System.currentTimeMillis();
                Cursor cate = getEditService(id, "0");
                cate.moveToFirst();
                t2 = System.currentTimeMillis();
                Helper.addTimeStamp(time, t1, t2, 0);
            } else {

            }
        } catch (Exception e) {

        }
        return time;
    }

    private void log(Cursor editService) {

    }

    public Cursor getEditServiceFix(String es_MachineId, String es_Field_Id) {
        Cursor curid = null;
        try {
            return db.rawQuery("Select * from  EditService where es_MachineId ='" + es_MachineId + "' AND es_Field_Id='" + es_Field_Id + "'", null);
        } catch (Exception e) {
            return curid;
        }
    }

    private List<List<Long>> fix5(SQLiteDatabase db) {
        time = new ArrayList<>();
        Intent intent = new Intent();
        intent.putExtra("Machine_Id", "123");
        try {
            String id = intent.getStringExtra("Machine_Id");
            if (id != null) {

                t3 = System.currentTimeMillis();
                id = ESAPI.encoder().encodeForSQL(sqliteCode, id);
                t4 = System.currentTimeMillis();

                t1 = System.currentTimeMillis();
                Cursor cate = getEditService(id, "0");
                cate.moveToFirst();
                t2 = System.currentTimeMillis();


                Helper.addTimeStamp(time, t1, t2, 0);
                Helper.addTimeStamp(time, t3, t4, 1);
            } else {

            }
        } catch (Exception e) {

        }
        return time;
    }

}