package sql.benchmark;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import edu.usc.SQLTesting.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnnecessaryRowRetrieval {

    private int rowSize, fieldSize, querySize;

    private SQLiteDatabase db;
    private String v_Id = "10000";
    private long t1, t2, t3, t4, t5;

    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.appspot.nycbustracker.apk
    CallChain:<com.yongchong.nycbustime.WidgetBusService: void onStart(android.content.Intent,int)>@35->
    <com.yongchong.nycbustime.DBAdapter: org.json.JSONArray getFavoriteBusJson()>
    Location:<com.yongchong.nycbustime.DBAdapter: org.json.JSONArray getFavoriteBusJson()>@240
    Query:[select * from favorite order by rank]
    Query Form:[select from order by]
    Table Form:[[integer, integer, text, text, text, text, text, text]]
     */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        String tableCreate = "create table favorite (_id integer primary key autoincrement, stopId text, stopName text, towards text, rank integer, routeName text, direction text, timing text);";


        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;
        this.db = db;

        MySQLiteHelper.populate(db, tableCreate, rowSize/10, fieldSize, null);
        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("direction", "null");
        MySQLiteHelper.addData(db, tableCreate, (rowSize / 10) * 9, fieldSize, specifyColumn);
        List<List<Long>>time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(String v)
    {
        //System.out.println(v);
    }


    private List<List<Long>>instance1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.db.rawQuery("SELECT * FROM favorite ORDER by rank", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                String direction = cursor.getString(6);
                if (direction != null) {
                    String stopId = cursor.getString(1);
                    String stopName = cursor.getString(2);
                    String routeName = cursor.getString(5);
                    String timing = cursor.getString(7);
                    log(stopId, stopName, routeName, timing);
                }
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private void log(String stopId, String stopName, String routeName, String timing) {

    }

    private List<List<Long>>fix1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.db.rawQuery("SELECT * FROM favorite where direction IS NOT NULL ORDER by rank ", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                String stopId = cursor.getString(1);
                String stopName = cursor.getString(2);
                String routeName = cursor.getString(5);
                String timing = cursor.getString(7);
                log(stopId, stopName, routeName, timing);
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private void log(long count) {
        System.out.println(count);
    }

    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.bluepin.kidsworldforgoogleplay.global.apk
    CallChain:<org.bma5.lib.Cocos2dxLocalStorage: java.lang.String getItem(java.lang.String)>
    Location:<org.bma5.lib.Cocos2dxLocalStorage: java.lang.String getItem(java.lang.String)>@78
    Query:[select value from data where key=?]
    Query Form:[select from where]
    Table Form:[[text, text]]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE IF NOT EXISTS data(key TEXT, value TEXT);";

        Map<String, String> specifyColumn0 = new HashMap<>();
        specifyColumn0.put("key", str);
        MySQLiteHelper.populate(db, tableCreate, rowSize / 10, fieldSize, specifyColumn0);

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("key", str);
        specifyColumn.put("value", "null");
        MySQLiteHelper.addData(db, tableCreate, (rowSize / 10) * 9, fieldSize, specifyColumn);

        List<List<Long>>time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    String str = "wefwfe";
    private List<List<Long>>instance2(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String str2;
        String str3 = null;
        String b = "data";
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("select value from " + b + " where key=?", new String[]{str});
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        do
        {
            if (str3 != null) {
                break;
            }
            str3 = rawQuery.getString(rawQuery.getColumnIndex("value"));
        } while (rawQuery.moveToNext());
        t3 = System.currentTimeMillis();
        rawQuery.close();
        str2 = str3;
        log(str2);

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix2(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String str2;
        String str3;
        String b = "data";
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("select value from " + b + " where key=? AND value IS NOT NULL", new String[]{str});
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        str3 = rawQuery.getString(rawQuery.getColumnIndex("value"));
        t3 = System.currentTimeMillis();
        rawQuery.close();
        str2 = str3;
        log(str2);

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }


    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.djstudio.professionalplayer.apk
    CallChain:<com.djstudio.professionalplayer.MainActivity$25: void onClick(android.content.DialogInterface,int)>@927->
    <com.djstudio.professionalplayer.DatabaseHandler: java.util.ArrayList Get_saveplayList(boolean,java.lang.String)>
    Location:<com.djstudio.professionalplayer.DatabaseHandler: java.util.ArrayList Get_saveplayList(boolean,java.lang.String)>@158
    Query:[select  * from saveplaylist]
    Query Form:[select from]
    Table Form:[[integer, text, text, text]]
     */
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE saveplaylist(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,path TEXT,playlistname TEXT)";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put(KEY_PLAYLISTNAME, name);
        MySQLiteHelper.populate(db, tableCreate, (rowSize / 10) * 9, fieldSize, null);
        MySQLiteHelper.addData(db, tableCreate, (rowSize / 10), fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PATH = "path";
    private static final String KEY_PLAYLISTNAME = "playlistname";
    String name = "webanewaWR";
    private List<List<Long>> instance3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM saveplaylist", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                if (cursor.getString(cursor.getColumnIndex(KEY_PLAYLISTNAME)).equals(name)) {
                    log(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLAYLISTNAME)));
                }
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM saveplaylist where playlistname = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                log(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAYLISTNAME)));
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.puritansoft.biblereadingplan.apk
    CallChain:<com.puritansoft.biblereadingplan.BibleReadingPlanActivity$6: void run()>@730->
    <com.puritansoft.biblereadingplan.BibleReadingPlanActivity: void access$1700(com.puritansoft.biblereadingplan.BibleReadingPlanActivity,com.puritansoft.biblereadingplan.enums.ReadingPlanType)>@126->
    <com.puritansoft.biblereadingplan.BibleReadingPlanActivity: void doReadingPlanUpgrade(com.puritansoft.biblereadingplan.enums.ReadingPlanType)>@1108->
    <com.puritansoft.biblereadingplan.database.DatabaseAdapter: boolean isPlanExistsByName(java.lang.String)>
    Location:<com.puritansoft.biblereadingplan.database.DatabaseAdapter: boolean isPlanExistsByName(java.lang.String)>@299
    Query:[select name from user_plan up, plan p where up.plan_id = p._id and p.name = ?]
    Query Form:[select from where and]
    Table Form:[[integer, integer, text, text]]
     */
    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE user_plan (_id INTEGER NOT NULL PRIMARY KEY autoincrement, plan_id INTEGER NOT NULL, start_date TEXT NOT NULL, end_date TEXT NOT NULL);";
        String tableCreate1 = "CREATE TABLE plan (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL\n, plan_type_id INTEGER NOT NULL, description   TEXT NOT NULL);";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("plan_id", "1");
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);

        Map<String, String> specifyColumn1 = new HashMap<>();
        specifyColumn1.put("name", planName);
        MySQLiteHelper.populate(db, tableCreate1, (rowSize / 10), fieldSize, specifyColumn1);
        MySQLiteHelper.addData(db, tableCreate1, (rowSize / 10) * 9, fieldSize, null);

        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    String planName = "wefwefwfa";
    private List<List<Long>> instance4(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        boolean exist;
        String name = "not found";
        t1 = System.currentTimeMillis();
        Cursor cur = db.rawQuery("SELECT name FROM user_plan up, plan p WHERE up.plan_id = p._id AND p.name = ?", new String[]{planName});
        if(cur.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                name = cur.getString(cur.getColumnIndex("name"));
            } while (cur.moveToNext());

        }
        if (name.equals("not found"))
            exist = false;
        else
            exist = true;
        t3 = System.currentTimeMillis();
        log(exist);
        cur.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private void log(boolean exist) {

    }


    private List<List<Long>> fix4(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        boolean exist = false;
        t1 = System.currentTimeMillis();
        Cursor cur = db.rawQuery("SELECT count(*) FROM user_plan up, plan p WHERE up.plan_id = p._id AND p.name = ?", new String[]{planName});
        if(cur.moveToFirst()) {
            t2 = System.currentTimeMillis();
            int count = cur.getInt(0);
            if(count > 0)
                exist = true;
            t3 = System.currentTimeMillis();
        }
        log(exist);
        cur.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.reticode.decoraciondeunas.apk
    CallChain:<com.mobandme.ada.DataBaseHelper: void onCreate(android.database.sqlite.SQLiteDatabase)>@53->
    <com.mobandme.ada.ObjectContext: void onCreateDataBase(android.database.sqlite.SQLiteDatabase)>@497->
    <com.mobandme.ada.ObjectContext: void onCreate(android.database.sqlite.SQLiteDatabase)>@534->
    <com.mobandme.ada.ObjectContext: void generateDataBase(android.database.sqlite.SQLiteDatabase,int)>@674->
    <com.mobandme.ada.DatabaseMerger: java.lang.String[] getDatabaseCleanScripts()>@124->
    <com.mobandme.ada.DatabaseMerger: java.lang.String[] getTables()>
    Location:<com.mobandme.ada.DatabaseMerger: java.lang.String[] getTables()>@377
    Query:[select distinct tbl_name from sqlite_master]
    Query Form:[select distinct from]
    Table Form:[]
     */
    public void experiment5(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table master (type TEXT, name TEXT, tbl_name TEXT, rootpage INTEGER, sql TEXT);";

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
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT DISTINCT tbl_name FROM master", null);
        rawQuery.moveToLast();
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        if (rawQuery.getCount() > 0) {
            do {
                String string = rawQuery.getString(0);
                if (!(string == null || string.trim() == "" || string.trim().toLowerCase().equals("android_metadata") || string.trim().toLowerCase().equals("sqlite_sequence"))) {
                    log(string);
                }
            } while (rawQuery.moveToNext());
        }
        t3 = System.currentTimeMillis();
        rawQuery.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix5(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT DISTINCT tbl_name FROM master where tbl_name IS NOT NULL AND tbl_name!='android_metadata' AND tbl_name!='sqlite_sequence'", null);
        rawQuery.moveToLast();
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        if (rawQuery.getCount() > 0) {
            do {
                String string = rawQuery.getString(0);
                log(string);
            } while (rawQuery.moveToNext());
        }
        t3 = System.currentTimeMillis();
        rawQuery.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryRowRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.safeway.client.android.albertsons.apk
    CallChain:<com.safeway.client.android.util.k: java.lang.Object loadInBackground()>@33->
    <com.safeway.client.android.util.k: android.database.Cursor a()>@211->
    <com.safeway.client.android.b.i: android.database.Cursor d()>@1726->
    <com.safeway.client.android.b.i: android.database.Cursor a(java.lang.String,android.database.Cursor,java.lang.String)>
    Location:<com.safeway.client.android.b.i: android.database.Cursor a(java.lang.String,android.database.Cursor,java.lang.String)>@1775
    Query:[select distinct 'checked' as category , count(*) as kount from tb_shopping_list where is_deleted = 0  and is_checked =  1]
    Query Form:[select distinct from where and]
    Table Form:[[integer, integer, integer, integer, integer, integer, integer, integer, integer, integer, integer, integer, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]

     */
    public void experimentX(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "";

        Map<String, String> specifyColumn = new HashMap<>();
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instanceX(db);
        else
            time = fixX(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instanceX(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        return time;
    }



    private List<List<Long>> fixX(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        return time;
    }
    /*
    UnnecessaryRowRetrieval
    CallChain:<com.amdroidalarmclock.amdroid.aa$3: boolean a(java.lang.Object)>->
    <com.amdroidalarmclock.amdroid.c: android.database.Cursor h()>
    Location:<com.amdroidalarmclock.amdroid.c: android.database.Cursor h()>@2625
    Query:[select * from settings where inactive = 0]
    */
    private SQLiteDatabase b;
    public void experimenty(SQLiteDatabase db, Environment exp) {

        String tableCreate = "CREATE TABLE IF NOT EXISTS settings (_id INTEGER PRIMARY KEY AUTOINCREMENT, snooze INTEGER, snoozeInterval INTEGER, snoozeMin INTEGER, snoozeMax INTEGER, volumeAlwaysOn INTEGER, volumeSystem INTEGER, volume INTEGER, soundType TEXT, alarm TEXT, ringtone TEXT, music TEXT, loop INTEGER, increaseVolume INTEGER, increaseVolumeInterval INTEGER, snoozeDecrease INTEGER, snoozeDecreaseInterval INTEGER, snoozeMaxCount INTEGER, preAlarm INTEGER, preAlarmInterval INTEGER, preAlarmMin INTEGER, preAlarmMax INTEGER, preAlarmVolume INTEGER, preAlarmLimit INTEGER, postAlarm INTEGER, postAlarmInterval INTEGER, postAlarmLimit INTEGER, shake INTEGER, vibrate INTEGER, vibrateTime INTEGER, vibrateSleep INTEGER, offDays INTEGER, snoozeIncreaseVolume INTEGER, dismiss INTEGER, dismissDifficulty INTEGER, dismissPauseInterval INTEGER, autoTimer INTEGER, autoTimerInterval INTEGER, enableWifi INTEGER, dismissLongPress INTEGER, snoozeLongPress INTEGER, toSkip INTEGER, wifiChallangeSsid TEXT, wifiChallangeRssi INTEGER, wifiChallangeBackup INTEGER, snoozeMaxCountVolume INTEGER, snoozeMaxCountChallenge INTEGER, placesEnabled INTEGER, preAlarmVibrate INTEGER, snoozeAdjustable INTEGER, nfcChallangeBackup INTEGER, alarmBrightnessEnable INTEGER, alarmBrightness INTEGER, preAlarmMusicEnable INTEGER, preAlarmMusic TEXT, preAlarmMusicTitle TEXT, postAlarmMusicEnable INTEGER, postAlarmMusic TEXT, ppostAlarmMusicTitle TEXT, musicTitle TEXT, weather INTEGER, wearShow INTEGER, postAlarmMaxVolume INTEGER, largeDismissText INTEGER, challengeCount INTEGER, challengeRequired INTEGER, snoozeIncreaseChallenge INTEGER, barcodeChallengeBarcode TEXT, barcodeChallangeBackup INTEGER, unlockManual INTEGER, headphonesOnly INTEGER, settingsName TEXT, calendarAllDay INTEGER, calendarInterval INTEGER, calendar INTEGER, calendarNotification INTEGER, vibrateDelay INTEGER, calendarTag TEXT, randomFolder TEXT, inactive INTEGER);";

        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;
        b = db;

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("inactive", "0");

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instancey(db);
        else
            time = fixy(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public final Cursor h() {
        return this.b.rawQuery("SELECT * FROM settings WHERE inactive = 0", null);
    }

    private void log()
    {

    }

    private List<List<Long>> instancey(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();

        long t1 = System.currentTimeMillis();
        Cursor h = h();
        h.moveToFirst();
        long t2 = System.currentTimeMillis();
        do {
            if(h.getInt(h.getColumnIndex("calendar")) == 1)
            {
                log();
                break;
            }
        } while(h.moveToNext());
        long t3 = System.currentTimeMillis();
        h.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private List<List<Long>> fixy(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        Cursor h = db.rawQuery("SELECT * FROM settings WHERE inactive = 0 AND calendar = 1", null);
        boolean exist = h.moveToFirst();
        long t2 = System.currentTimeMillis();
        if(exist)
        {
            log();
        }
        long t3 = System.currentTimeMillis();
        h.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }
}