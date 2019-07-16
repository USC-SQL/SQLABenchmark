package sql.benchmark;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnnecessaryColumnRetrieval {

    private int rowSize, fieldSize, querySize;

    /*
    covered query form: select from where
    covered table form: [text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]
     */


    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.tractorpal.apk
    CallChain:<com.tractorpal.EditService: void onClick(android.view.View)>@291->
    <com.database.DatabaseHandler: android.database.Cursor getCategoryTable(java.lang.String)>
    Location:<com.database.DatabaseHandler: android.database.Cursor getCategoryTable(java.lang.String)>@396
    Query:[select * from  vehicle where v_id='unknown@method@<android.content.sharedpreferences: java.lang.string getstring(java.lang.string,java.lang.string)>@<com.tractorpal.editservice: void onclick(android.view.view)>@291@481!!!']
    Query Form:[select from where]
    Table Form:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */
    private SQLiteDatabase db;
    private String v_Id = "10000";
    private long t1, t2, t3, t4, t5;

    public void experiment1(SQLiteDatabase db, Environment exp) {
        String tableCreate = "CREATE TABLE IF NOT EXISTS  Vehicle (  v_Id Text,  v_Name TEXT,  v_image TEXT ,  v_Serial_Number TEXT ,  v_Last_Service TEXT,  v_Purchased_Date TEXT,  v_Purchase_Price TEXT,  v_Purchase_Place TEXT,  v_Original_MilesHours TEXT,  v_Model_Year TEXT,  v_Other TEXT,  v_Notes TEXT,  v_Date TEXT,  thumb_Profile TEXT, date_created TEXT ,  image_changed TEXT );";


        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;
        this.db = db;

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("v_Id", v_Id);

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
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

    public Cursor getCategoryTable(String v_id) {
        Cursor curid = null;
        try {
            t1 = System.currentTimeMillis();
            curid = db.rawQuery("Select * from  Vehicle where v_id='" + v_id + "'", null);
            t2 = System.currentTimeMillis();
            return curid;
        } catch (Exception e) {
            return curid;
        }
    }

    private List<List<Long>> instance1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();


        Cursor createmachinedate_month = getCategoryTable(v_Id);

        t3 = System.currentTimeMillis();
        if (createmachinedate_month.getCount() > 0) {
            if (createmachinedate_month.moveToFirst()) {
                t4 = System.currentTimeMillis();
                do {
                    log(createmachinedate_month.getString(12));
                } while (createmachinedate_month.moveToNext());
                t5 = System.currentTimeMillis();
            }
        }
        createmachinedate_month.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t4, t5, 1);
        return time;
    }

    public Cursor getCategoryTableFix(String v_id) {
        Cursor curid = null;
        try {
            t1 = System.currentTimeMillis();
            curid = db.rawQuery("Select v_Date from  Vehicle where v_id='" + v_id + "'", null);
            t2 = System.currentTimeMillis();
            return curid;
        } catch (Exception e) {
            return curid;
        }
    }
    private List<List<Long>> fix1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        Cursor createmachinedate_month = getCategoryTableFix(v_Id);

        t3 = System.currentTimeMillis();
        if (createmachinedate_month.getCount() > 0) {
            if (createmachinedate_month.moveToFirst()) {
                t4 = System.currentTimeMillis();
                do {
                    log(createmachinedate_month.getString(0));
                } while (createmachinedate_month.moveToNext());
                t5 = System.currentTimeMillis();
            }
        }
        createmachinedate_month.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 0);
        Helper.addTimeStamp(time, t4, t5, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.endless.dinnerrecipes.apk
    CallChain:<com.endless.dinnerrecipes.SitemapCategoriesFragment$GetData: void onPostExecute(java.lang.Object)>@183->
    <com.endless.dinnerrecipes.SitemapCategoriesFragment$GetData: void onPostExecute(java.lang.String)>@262->
    <com.endless.dinnerrecipes.DatabaseHandler: int getResponseCount(java.lang.String)>
    Location:<com.endless.dinnerrecipes.DatabaseHandler: int getResponseCount(java.lang.String)>@360
    Query:[select  * from offlinestore where url = ?]
    Query Form:[select from where]
    Table Form:[[text, text]]
    */
    String url = "sdkfjksldflsjflw";
    public void experiment2(SQLiteDatabase db, Environment exp) {
        String tableCreate = "CREATE TABLE IF NOT EXISTS offlinestore(url TEXT,response TEXT)";
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("url", url);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(int i)
    {
        //System.out.println(i);
    }

    private List<List<Long>> instance2(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM offlinestore WHERE url = ?", new String[]{url});
        log(cursor.getCount());
        long t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        cursor.close();
        return time;
    }

    private List<List<Long>> fix2(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM offlinestore WHERE url = ?", new String[]{url});
        cursor.moveToFirst();
        log(cursor.getInt(0));
        long t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        cursor.close();
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.binarytoys.speedometer.apk
    CallChain:<com.binarytoys.speedometer.UlysseBackupAgent: void onCreate()>@34->
    <com.binarytoys.core.preferences.ProfileManager: void init(android.content.Context)>@147->
    <com.binarytoys.core.preferences.ProfileManager: void init(android.content.Context,boolean)>@74->
    <com.binarytoys.core.preferences.ProfileManager: void init(boolean)>@111->
    <com.binarytoys.core.preferences.AppPreferences: void <init>(com.binarytoys.core.preferences.db.PreferencesDBHelper,long)>@69->
    <com.binarytoys.core.preferences.db.PreferencesDBHelper: java.util.Map getMapStringSet(long)>
    Location:<com.binarytoys.core.preferences.db.PreferencesDBHelper: java.util.Map getMapStringSet(long)>@1463
    Query:[select  * from string_set where profile = unknown@dynamic_var@unknown@<com.binarytoys.core.preferences.profilemanager: void init(boolean)>@111@222!!!]
    Query Form:[select from where]
    Table Form:[[integer, integer, text, text]]
     */

    private String profileId = "1000";
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "CREATE TABLE string_set(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,value TEXT,profile INTEGER)";

        Map<String, String> specifyColumn = new HashMap<>();

        specifyColumn.put("profile", profileId);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(int a, String b, String c)
    {
        //System.out.println(a + b + c);
    }


    private List<List<Long>> instance3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        String selectQuery = "SELECT  * FROM string_set WHERE profile = " + profileId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            t2 = System.currentTimeMillis();
            int colId = c.getColumnIndex("id");
            int colName = c.getColumnIndex("name");
            int colValue = c.getColumnIndex("value");
            do {
                log(c.getInt(colId), c.getString(colName), c.getString(colValue));
            } while (c.moveToNext());
            t3 = System.currentTimeMillis();
        }

        c.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix3(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        String selectQuery = "SELECT id, name, value FROM string_set WHERE profile = " + profileId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            t2 = System.currentTimeMillis();
            int colId = c.getColumnIndex("id");
            int colName = c.getColumnIndex("name");
            int colValue = c.getColumnIndex("value");
            do {
                log(c.getInt(colId), c.getString(colName), c.getString(colValue));
            } while (c.moveToNext());
            t3 = System.currentTimeMillis();
        }

        c.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.ecare.pregnancycalculator.apk
    CallChain:<com.ecare.pregnancycalculator.f: void b(int)>@150->
    <com.ecare.pregnancycalculator.DayCalendarPageActivity: void b(com.ecare.pregnancycalculator.DayCalendarPageActivity,int)>@22->
    <com.ecare.pregnancycalculator.e: java.lang.String b(java.util.Calendar)>
    Location:<com.ecare.pregnancycalculator.e: java.lang.String b(java.util.Calendar)>@146
    Query:[select _id, date, note from notes where date = unknown@method@<java.lang.string: java.lang.string valueof(long)>@<com.ecare.pregnancycalculator.e: java.lang.string b(java.util.calendar)>@146@28!!!]
    Query Form:[select from where]
    Table Form:[[integer, integer, text]]
     */

    String date = "20190201";
    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT,date INTEGER,note STRING);";

        Map<String, String> specifyColumn = new HashMap<>();

        specifyColumn.put("date", date);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance4(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT _id, date, note FROM notes WHERE date = " + date, null);
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        if (rawQuery == null || rawQuery.isAfterLast()) {
            rawQuery.close();
        }
        String string = rawQuery.getString(rawQuery.getColumnIndex("note"));
        t3 = System.currentTimeMillis();
        log(string);
        rawQuery.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private List<List<Long>> fix4(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT note FROM notes WHERE date = " + date, null);
        rawQuery.moveToFirst();
        t2 = System.currentTimeMillis();
        if (rawQuery == null || rawQuery.isAfterLast()) {
            rawQuery.close();
        }
        String string = rawQuery.getString(rawQuery.getColumnIndex("note"));
        t3 = System.currentTimeMillis();
        log(string);
        rawQuery.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.ecare.pregnancycalculator.apk
    CallChain:<com.ecare.pregnancycalculator.f: void b(int)>@164->
    <com.ecare.pregnancycalculator.DayCalendarPageActivity: void b(com.ecare.pregnancycalculator.DayCalendarPageActivity,int)>@22->
    <com.ecare.pregnancycalculator.e: java.lang.String b(java.util.Calendar)>
    Location:<com.ecare.pregnancycalculator.e: java.lang.String b(java.util.Calendar)>@146
    Query:[select _id, date, note from notes where date = unknown@method@<java.lang.string: java.lang.string valueof(long)>@<com.ecare.pregnancycalculator.e: java.lang.string b(java.util.calendar)>@146@28!!!]
    Query Form:[select from where]
    Table Form:[[integer, integer, text]]
     */
    String str = "william";
    public void experiment5(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table props (_id INTEGER PRIMARY KEY AUTOINCREMENT,_name TEXT,_value TEXT,_created NUMERIC NOT NULL,_modified NUMERIC)";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("_name", str);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance5(db);
        else
            time = fix5(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    String KEY_VALUE = "_value";
    public static String getString(Cursor cursor, String str) {
        return getString(cursor, str, null);
    }

    public static String getString(Cursor cursor, String str, String str2) {
        int columnIndex = cursor.getColumnIndex(str);
        return (columnIndex == -1 || cursor.isNull(columnIndex)) ? str2 : cursor.getString(columnIndex);
    }

    private List<List<Long>> instance5(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String str2 = null;
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT a.* FROM props a WHERE a._name= ?", new String[]{str});
        if (rawQuery != null) {
            if (rawQuery.moveToFirst()) {
                t2 = System.currentTimeMillis();
                str2 = getString(rawQuery, KEY_VALUE);
                t3 = System.currentTimeMillis();
            }
            rawQuery.close();
        }
        log(str2);

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix5(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String str2 = null;
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT a._value FROM props a WHERE a._name= ?", new String[]{str});
        if (rawQuery != null) {
            if (rawQuery.moveToFirst()) {
                t2 = System.currentTimeMillis();
                str2 = getString(rawQuery, KEY_VALUE);
                t3 = System.currentTimeMillis();
            }
            rawQuery.close();
        }
        log(str2);

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }


    /*
    /home/yingjun/Documents/SQLUsage/appset/1000apps/com.andromo.dev231870.app482676.apk contains UnnecessaryColumnRetrieval num:2
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.andromo.dev231870.app482676.apk
    CallChain:<com.andromo.dev231870.app482676.RSSFeedProvider: boolean onCreate()>@-1->
    <com.andromo.dev231870.app482676.dc: void <init>(android.content.Context)>@-1->
    <com.andromo.dev231870.app482676.dc: void b()>
    Location:<com.andromo.dev231870.app482676.dc: void b()>@-1
    Query:[select * from rssfeed_channel]
    Query Form:[select from]
    Table Form:[[integer, text, text, text]]
     */
    public void experiment6(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE rssfeed_channel (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,link TEXT,last_update TEXT);";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance6(db);
        else
            time = fix6(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(long i)
    {

    }

    private List<List<Long>> instance6(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT * FROM rssfeed_channel", null);
        if (rawQuery != null && rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                long j = rawQuery.getLong(0);
                log(j);
            } while (rawQuery.moveToNext());
            t3 = System.currentTimeMillis();
        }
        rawQuery.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix6(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT _id FROM rssfeed_channel", null);
        if (rawQuery != null && rawQuery.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                long j = rawQuery.getLong(0);
                log(j);
            } while (rawQuery.moveToNext());
            t3 = System.currentTimeMillis();
        }
        rawQuery.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.SkyDivers.asteroids3d.apk
    CallChain:<com.SkyDivers.asteroids3d.ui.NewDetailActivity: boolean onOptionsItemSelected(android.view.MenuItem)>@-1->
    <com.SkyDivers.asteroids3d.ui.NewDetailActivity: boolean c(int)>@-1->
    <com.SkyDivers.asteroids3d.ui.a.a: int c()>
    Location:<com.SkyDivers.asteroids3d.ui.a.a: int c()>@-1
    Query:[select  * from installeditem]
    Query Form:[select from]
    Table Form:[[integer, text, text, text, text, text, text, text, text]]
     */

    public void experiment7(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE installedItem(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,author TEXT, author_link TEXT, description TEXT, filename TEXT, packagename TEXT, packagelink TEXT, item_id TEXT)";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance7(db);
        else
            time = fix7(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance7(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT * FROM installedItem", null);
        int count = rawQuery.getCount();
        t2 = System.currentTimeMillis();

        log(count);
        rawQuery.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }



    private List<List<Long>> fix7(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT count(*) FROM installedItem", null);
        rawQuery.moveToFirst();
        int count = rawQuery.getInt(0);
        t2 = System.currentTimeMillis();

        log(count);
        rawQuery.close();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.carmellimo.limousine.apk
    CallChain:<com.carmellimo.limousine.SelectCarAndBook$b: void onPostExecute(java.lang.Object)>@675->
    <com.carmellimo.limousine.SelectCarAndBook$b: void a(java.lang.String)>@703->
    <com.carmellimo.limousine.SelectCarAndBook: void b(com.carmellimo.limousine.SelectCarAndBook,android.view.View)>@46->
    <com.carmellimo.limousine.SelectCarAndBook: void b(android.view.View)>@378->
    <com.carmellimo.limousine.SelectCarAndBook: void a(java.lang.String)>@403->
    <com.carmellimo.limousine.b.m: void b(android.content.Context)>@68->
    <com.carmellimo.limousine.c.d: java.util.ArrayList b(java.lang.String)>
    Location:<com.carmellimo.limousine.c.d: java.util.ArrayList b(java.lang.String)>@633
    Query:[select * from carddetails where cardseq=unknown@field@<com.carmellimo.limousine.b.m: java.lang.string o>!!!, select * from carddetails where cardseq=2, select * from carddetails where cardseq=1, select * from carddetails where cardseq=]
    Query Form:[select from where]
    Table Form:[[blob, blob, integer, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */
    String CardSeq = "0323220";
    public void experiment8(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE CardDetails (_id integer primary key autoincrement,AutoCharge VARCHAR, BillingAddr VARCHAR, BillingCity VARCHAR, BillingState VARCHAR, BillingZip VARCHAR, CardCvvCode VARCHAR, CardExp VARCHAR, CardHolder VARCHAR, CardNumber BLOB, CardSeq VARCHAR, CardType VARCHAR, OnFile VARCHAR, CardNumberblob BLOB, cardPassword VARCHAR, BillingCountry VARCHAR, PrepayType VARCHAR)";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("CardSeq", CardSeq);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance8(db);
        else
            time = fix8(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }
    private void log(String autoCharge, String billingAddr, String billingCity, String billingState, String billingZip, String cardExp, String cardHolder, byte[] cardNumbers, String cardSeq, String cardType, String billingCountry, String prepayType, String onFile) {

    }

    private List<List<Long>> instance8(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select * from CardDetails where CardSeq=" + CardSeq, null);
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        do {
            log(
                cursor.getString(cursor.getColumnIndex("AutoCharge")),
                cursor.getString(cursor.getColumnIndex("BillingAddr")),
                cursor.getString(cursor.getColumnIndex("BillingCity")),
                cursor.getString(cursor.getColumnIndex("BillingState")),
                cursor.getString(cursor.getColumnIndex("BillingZip")),
                cursor.getString(cursor.getColumnIndex("CardExp")),
                cursor.getString(cursor.getColumnIndex("CardHolder")),
                cursor.getBlob(cursor.getColumnIndex("CardNumber")),
                cursor.getString(cursor.getColumnIndex("CardSeq")),
                cursor.getString(cursor.getColumnIndex("CardType")),
                cursor.getString(cursor.getColumnIndex("BillingCountry")),
                cursor.getString(cursor.getColumnIndex("PrepayType")),
                cursor.getString(cursor.getColumnIndex("OnFile"))
            );
        } while (cursor.moveToNext());
        t3 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return time;
    }




    private List<List<Long>> fix8(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select AutoCharge, BillingAddr, BillingCity, BillingState, BillingZip, CardExp, CardHolder, CardNumber, CardSeq, CardType, BillingCountry, PrepayType, OnFile from CardDetails where CardSeq=" + CardSeq, null);
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        do {
            log(
                    cursor.getString(cursor.getColumnIndex("AutoCharge")),
                    cursor.getString(cursor.getColumnIndex("BillingAddr")),
                    cursor.getString(cursor.getColumnIndex("BillingCity")),
                    cursor.getString(cursor.getColumnIndex("BillingState")),
                    cursor.getString(cursor.getColumnIndex("BillingZip")),
                    cursor.getString(cursor.getColumnIndex("CardExp")),
                    cursor.getString(cursor.getColumnIndex("CardHolder")),
                    cursor.getBlob(cursor.getColumnIndex("CardNumber")),
                    cursor.getString(cursor.getColumnIndex("CardSeq")),
                    cursor.getString(cursor.getColumnIndex("CardType")),
                    cursor.getString(cursor.getColumnIndex("BillingCountry")),
                    cursor.getString(cursor.getColumnIndex("PrepayType")),
                    cursor.getString(cursor.getColumnIndex("OnFile"))
            );
        } while (cursor.moveToNext());
        t3 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);

        cursor.close();
        return time;
    }


    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/vault.gallery.lock.apk
    CallChain:<com.safebrowser.SafeBrowseActivity$7: void onReceivedTitle(android.webkit.WebView,java.lang.String)>@-1->
    <com.safebrowser.a: boolean a(java.lang.String,java.lang.String)>
    Location:<com.safebrowser.a: boolean a(java.lang.String,java.lang.String)>@-1
    Query:[select * from tblhistory where url=?]
    Query Form:[select from where]
    Table Form:[[integer, text, text]]
     */

    String c = "tblHistory";
    String d = "name";
    String e = "url";
    String str2 = "www.asbsssew.com";
    public void experiment9(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table " + this.c + " (id integer primary key autoincrement," + this.d + " text not null," + this.e + " text not null)";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("url", str2);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance9(db);
        else
            time = fix9(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance9(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT * FROM " + this.c + " WHERE " + this.e + "=?", new String[]{str2});
        if (rawQuery.getCount() > 0) {
            rawQuery.moveToFirst();
            log(rawQuery.getCount());
        }
        t2 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t1, t2, 0);

        rawQuery.close();
        return time;
    }



    private List<List<Long>> fix9(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor rawQuery = db.rawQuery("SELECT count(*) FROM " + this.c + " WHERE " + this.e + "=?", new String[]{str2});
        rawQuery.moveToFirst();
        int count = rawQuery.getInt(0);
        if (count > 0) {
            log(count);
        }
        t2 = System.currentTimeMillis();

        Helper.addTimeStamp(time, t1, t2, 0);

        rawQuery.close();
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.ml.TXSC.apk
    CallChain:<com.mobiloud.activity.CategoryActivity: void onCreate(android.os.Bundle)>@130->
    <com.mobiloud.sqlite.CategoriesTable: java.util.List getAll()>
    Location:<com.mobiloud.sqlite.CategoriesTable: java.util.List getAll()>@105
    Query:[select * from tb_category;]
    Query Form:[select from]
    Table Form:[[integer, integer, text, text, text, text]]
     */
    public void experiment10(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE TB_Category (id INTEGER PRIMARY KEY AUTOINCREMENT, id_category INTEGER, name TEXT NOT NULL, slug TEXT, image_url TEXT, list_type TEXT);";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance10(db);
        else
            time = fix10(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }
    class Category
    {
        public Category(Cursor cursor)
        {
            if (cursor.getCount() > 0) {
                log(cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
            }
        }
    }

    private void log(int anInt, String string, String string1, String string2, String string3) {
    }

    private void cursorToObjets(Cursor c) {
        if (c.getCount() == 0) {
            return;
        }

        while (!c.isAfterLast()) {
            new Category(c);
            c.moveToNext();
        }
    }

    private List<List<Long>> instance10(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor c = db.rawQuery("SELECT * FROM TB_Category;", null);
        c.moveToFirst();
        t2 = System.currentTimeMillis();
        cursorToObjets(c);
        t3 = System.currentTimeMillis();

        c.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    class CategoryFix
    {
        public CategoryFix(Cursor cursor)
        {
            if (cursor.getCount() > 0) {
                log(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
            }
        }
    }

    private void cursorToObjetsFix(Cursor c) {
        if (c.getCount() == 0) {
            return;
        }

        while (!c.isAfterLast()) {
            new CategoryFix(c);
            c.moveToNext();
        }
    }

    private List<List<Long>> fix10(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor c = db.rawQuery("SELECT id_category, name, slug, image_url, list_type FROM TB_Category;", null);
        c.moveToFirst();
        t2 = System.currentTimeMillis();
        cursorToObjetsFix(c);
        t3 = System.currentTimeMillis();

        c.close();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.appspot.nycbustracker.apk
    CallChain:<com.yongchong.nycbustime.WidgetStopService: void onStart(android.content.Intent,int)>@35->
    <com.yongchong.nycbustime.DBAdapter: org.json.JSONArray getFavoriteStopJson()>
    Location:<com.yongchong.nycbustime.DBAdapter: org.json.JSONArray getFavoriteStopJson()>@341
    Query:[select * from favorite order by rank]
    Query Form:[select from order by]
    Table Form:[[integer, integer, text, text, text, text, text, text]]
     */
    public void experiment11(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table favorite (_id integer primary key autoincrement, stopId text, stopName text, towards text, rank integer, routeName text, direction text, timing text);";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("routeName", "null");
        specifyColumn.put("direction", "null");
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance11(db);
        else
            time = fix11(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private void log(String a, String b, String c)
    {
        //System.out.println(a);
    }

    private List<List<Long>> instance11(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM favorite ORDER by rank", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                if (cursor.getString(6) == null && cursor.getString(5) == null) {
                    String stopId = cursor.getString(1);
                    String stopName = cursor.getString(2);
                    String towards = cursor.getString(3);
                    log(stopId, stopName, towards);
                }
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    private List<List<Long>> fix11(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT stopId, stopName, towards, routeName, direction FROM favorite ORDER by rank", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                if (cursor.getString(3) == null && cursor.getString(4) == null) {
                    String stopId = cursor.getString(0);
                    String stopName = cursor.getString(1);
                    String towards = cursor.getString(2);
                    log(stopId, stopName, towards);
                }
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }


    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.tractorpal.apk
    CallChain:<com.tractorpal.ServiceDetail: void onClick(android.view.View)>@637->
    <com.database.DatabaseHandler: android.database.Cursor getEditService(java.lang.String,java.lang.String)>
    Location:<com.database.DatabaseHandler: android.database.Cursor getEditService(java.lang.String,java.lang.String)>@965
    Query:[select * from  editservice where es_machineid ='unknown@method@<android.content.sharedpreferences: java.lang.string getstring(java.lang.string,java.lang.string)>@<com.tractorpal.servicedetail: void onclick(android.view.view)>@637@875!!!' and es_field_id='unknown@method@<android.content.sharedpreferences: java.lang.string getstring(java.lang.string,java.lang.string)>@<com.tractorpal.servicedetail: void onclick(android.view.view)>@637@887!!!']
    Query Form:[select from where and]
    Table Form:[[text, text, text, text, text, text]]
     */

    String es_MachineId = "EQ@2123123q@@";
    String es_Field_Id= "WEEW212";
    public void experiment12(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        this.db = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS EditService (  es_MachineId TEXT,  es_Field_Id TEXT,  es_Edit_KM TEXT ,  es_EditMonth TEXT ,  es_After_Service_Month TEXT , date_created TEXT );";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("es_MachineId", es_MachineId);
        specifyColumn.put("es_Field_Id", es_Field_Id);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance12(db);
        else
            time = fix12(db);
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

    private List<List<Long>> instance12(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        Cursor servicelastdate_month = getEditService(es_MachineId, es_Field_Id);
        if (servicelastdate_month.getCount() > 0) {
            if (servicelastdate_month.moveToFirst()) {
                t2 = System.currentTimeMillis();
                do {
                    log(servicelastdate_month.getString(3));
                } while (servicelastdate_month.moveToNext());
                t3 = System.currentTimeMillis();
            }
        }
        servicelastdate_month.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    public Cursor getEditServiceFix(String es_MachineId, String es_Field_Id) {
        Cursor curid = null;
        try {
            return db.rawQuery("Select es_EditMonth from  EditService where es_MachineId ='" + es_MachineId + "' AND es_Field_Id='" + es_Field_Id + "'", null);
        } catch (Exception e) {
            return curid;
        }
    }

    private List<List<Long>> fix12(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor servicelastdate_month = getEditServiceFix(es_MachineId, es_Field_Id);
        if (servicelastdate_month.getCount() > 0) {
            if (servicelastdate_month.moveToFirst()) {
                t2 = System.currentTimeMillis();
                do {
                    log(servicelastdate_month.getString(0));
                } while (servicelastdate_month.moveToNext());
                t3 = System.currentTimeMillis();
            }
        }
        servicelastdate_month.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }


    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.appspot.nycsubwaytimes.apk
    CallChain:<com.appspot.HelloListView.AdapterStop$1: void onClick(android.view.View)>@96->
    <com.appspot.HelloListView.DBAdapter: void insertFavorite(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)>
    Location:<com.appspot.HelloListView.DBAdapter: void insertFavorite(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)>@110
    Query:[select * from favorite where lineid = ? and stopid = ? and directionid = ?]
    Query Form:[select from where and and]
    Table Form:[[integer, integer, text, text, text, text, text, text]]
     */

    String lineId = "123";
    String stopId = "12124";
    String direction = "123123231";

    public void experiment13(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "create table favorite (_id integer primary key autoincrement, lineId text not null, stopId text not null, stopName text not null, direction text not null, timing text, rank integer, directionId text not null);";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("lineId", lineId);
        specifyColumn.put("stopId", stopId);
        specifyColumn.put("directionId", direction);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance13(db);
        else
            time = fix13(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }


    private List<List<Long>> instance13(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String[] strArr = new String[3];
        strArr[0] = lineId;
        strArr[1] = stopId;
        strArr[2] = direction;

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM favorite where lineId = ? and stopId = ? and directionId = ?", strArr);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            cursor.close();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }



    private List<List<Long>> fix13(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String[] strArr = new String[3];
        strArr[0] = lineId;
        strArr[1] = stopId;
        strArr[2] = direction;

        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT _id FROM favorite where lineId = ? and stopId = ? and directionId = ?", strArr);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            cursor.close();
        }
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    /*
    Antipattern:UnnecessaryColumnRetrieval
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.cardsapp.android.apk
    CallChain:<com.cardsapp.android.views.ImageViewer: void onCreate(android.os.Bundle)>@41->
    <com.cardsapp.android.managers.DataManager: void markMessageAsRead(com.cardsapp.android.bo.CAMessage,boolean)>@225->
    <com.cardsapp.android.managers.DataManager: void updateUnreadInfo()>@281->
    <com.cardsapp.android.managers.DataManager: int getUnreadCouponsForBrandUID(java.lang.String,android.content.Context)>@169->
    <com.cardsapp.android.dto.TableUserInfo: int unreadCouponsForBrandUID(java.lang.String)>
    Location:<com.cardsapp.android.dto.TableUserInfo: int unreadCouponsForBrandUID(java.lang.String)>@292
     */

    String brandUID = "1231231";
    String type = "32";
    String read = "0";

    public void experiment14(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE IF NOT EXISTS messages(messageid text, brandid text,brandname text, messagecontent text, type integer, read integer,  credits integer,iscoupon integer,isrich integer,sentdate text,used integer,picurl text);";

        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("brandid", brandUID);
        specifyColumn.put("type", type);
        specifyColumn.put("read", read);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance14(db);
        else
            time = fix14(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance14(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        int i = 0;
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT  * FROM messages WHERE brandid='" + brandUID + "'" + " AND (type='16' OR type='32') AND read='0'", null);
        if (cursor.moveToFirst()) {
            t2 = System.currentTimeMillis();
            do {
                i++;
            } while (cursor.moveToNext());
            t3 = System.currentTimeMillis();
        }
        cursor.close();
        log(i);


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }



    private List<List<Long>> fix14(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM messages WHERE brandid='" + brandUID + "'" + " AND (type='16' OR type='32') AND read='0'", null);
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        int i = cursor.getInt(0);
        t3 = System.currentTimeMillis();


        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        log(i);
        return time;
    }

}