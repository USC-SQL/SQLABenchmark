package sql.benchmark;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotMergingProjectionPredicates {

    private static int rowSize, fieldSize, querySize;

    private long t1, t2, t3, t4;

    private List<List<Long>> time;
    /*
    Antipattern:NotMergingProjectionPredicates
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.omrup.cell.tracker.apk
    CallChain1:<com.omrup.cell.tracker.receiver.GpsReceiver: void onReceive(android.content.Context,android.content.Intent)>@63->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_name()>
    Location1:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_name()>@340
    Query1:[select user_name from user]
    Query Form1:[select from]
    Table Form1:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
    CallChain2:<com.omrup.cell.tracker.receiver.GpsReceiver: void onReceive(android.content.Context,android.content.Intent)>@61->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_id()>
    Location2:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_user_id()>@331
    Query2:[select user_id from user]
    Query Form2:[select from]
    Table Form2:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
    */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        sqldb = db;
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

    SQLiteDatabase sqldb;
    public String get_user_id() {
        String str = "";
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select user_id from USER", null);
        cursor.moveToNext();
        t2= System.currentTimeMillis();
        str = cursor.getString(0);
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return str;
    }

    public String get_user_name() {
        String str = "";
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select user_name from USER", null);
        cursor.moveToNext();
        t2= System.currentTimeMillis();
        str = cursor.getString(0);
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return str;
    }

    public void log(String a, String b)
    {
        //System.out.println(a + " " + b);
    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        time = new ArrayList<>();
        log(get_user_id(), get_user_name());
        return time;
    }

    private List<List<Long>> fix1(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select user_id, user_name from USER", null);
        cursor.moveToNext();
        t2 = System.currentTimeMillis();
        log(cursor.getString(0), cursor.getString(1));
        t3 = System.currentTimeMillis();
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }


    /*
    Antipattern:NotMergingProjectionPredicates
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.omrup.cell.tracker.apk
    CallChain1:<com.omrup.cell.tracker.Fragment.Setting_fragment: android.view.View onCreateView(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)>@66->
    <com.omrup.cell.tracker.Fragment.Setting_fragment: void init()>@239->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_map_type()>
    Location1:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_map_type()>@174
    Query1:[select map_type from setting]
    Query Form1:[select from]
    Table Form1:[[text, text, text]]
    CallChain2:<com.omrup.cell.tracker.Fragment.Setting_fragment: android.view.View onCreateView(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)>@66->
    <com.omrup.cell.tracker.Fragment.Setting_fragment: void init()>@264->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_love_alaram_cancellation_time()>
    Location2:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_love_alaram_cancellation_time()>@189
    Query2:[select love_alaram_cancellation_time from setting]
    Query Form2:[select from]
    Table Form2:[[text, text, text]]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        sqldb = db;
        String tableCreate = "CREATE TABLE SETTING(notify_me_when text,map_type text,love_alaram_cancellation_time text)";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public String get_map_type() {
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select map_type from SETTING", null);
        cursor.moveToNext();
        t2= System.currentTimeMillis();
        String str = cursor.getString(0);
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return str;
    }
    public String get_love_alaram_cancellation_time() {
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select love_alaram_cancellation_time from SETTING", null);
        cursor.moveToNext();
        t2= System.currentTimeMillis();
        String str = cursor.getString(0);
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return str;
    }
    private List<List<Long>> instance2(SQLiteDatabase db) {
        time = new ArrayList<>();
        log(get_map_type(), get_love_alaram_cancellation_time());
        return time;
    }



    private List<List<Long>> fix2(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select map_type, love_alaram_cancellation_time from SETTING", null);
        cursor.moveToNext();
        t2 = System.currentTimeMillis();
        log(cursor.getString(0), cursor.getString(1));
        t3 = System.currentTimeMillis();
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

    /*
    Antipattern:NotMergingProjectionPredicates
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.wearablelab.fitnessmate.apk
    CallChain1:<com.wearablelab.fitnessmate.MainActivity: void onActivityResult(int,int,android.content.Intent)>@557->
    <com.wearablelab.fitnessmate.MainActivity: void updateFragment(int,int,boolean)>@355->
    <com.wearablelab.fitnessmate.SummaryFragment: void updateUI(boolean)>@293->
    <com.wearablelab.fitnessmate.WorkoutItemDBUtil: java.util.List getWeeklyView(int,int)>
    Location1:<com.wearablelab.fitnessmate.WorkoutItemDBUtil: java.util.List getWeeklyView(int,int)>@146
    Query1:[select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@134@307!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 and fitnessid != 9 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@99@60!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@110@125!!!, sum(duration) from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@122@197!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid == 7 group by weekday]
    Query Form1:[select from where and and and and group by, select from where and and group by, select from where and and and group by]
    Table Form1:[[datetime, integer, integer, integer, integer, integer, integer, text]]
    CallChain2:<com.wearablelab.fitnessmate.MainActivity: void onActivityResult(int,int,android.content.Intent)>@559->
    <com.wearablelab.fitnessmate.MainActivity: void updateFragment(int,int,boolean)>@355->
    <com.wearablelab.fitnessmate.SummaryFragment: void updateUI(boolean)>@293->
    <com.wearablelab.fitnessmate.WorkoutItemDBUtil: java.util.List getWeeklyView(int,int)>
    Location2:<com.wearablelab.fitnessmate.WorkoutItemDBUtil: java.util.List getWeeklyView(int,int)>@146
    Query2:[select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@134@307!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 and fitnessid != 9 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@99@60!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@110@125!!!, sum(duration) from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid >= 7 and fitnessid <= 12 group by weekday, select strftime('%w', datetime(starttime, 'unixepoch', 'localtime')) as weekday, unknown@dynamic_var@nodef@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@122@197!!! from workoutitem where date(starttime, 'unixepoch', 'localtime') >= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@95@14!!! days', 'weekday 0') and date(starttime, 'unixepoch', 'localtime') <= date('now', 'localtime', 'unknown@dynamic_var@unknown@<com.wearablelab.fitnessmate.workoutitemdbutil: java.util.list getweeklyview(int,int)>@96@37!!! days', 'weekday 6') and fitnessid == 7 group by weekday]
    Query Form2:[select from where and and and and group by, select from where and and group by, select from where and and and group by]
    Table Form2:[[datetime, integer, integer, integer, integer, integer, integer, text]]
     */
    SQLiteDatabase database;
    int currentWeek = 0;
    public void experiment3(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        this.database = db;

        String tableCreate = "create table if not exists Workoutitem(_id integer primary key AUTOINCREMENT, fitnessID integer, startTime datetime, duration integer, count integer, distance real, calorie real, meta string);";


        Map<String, String> specifyColumn = new HashMap<>();


        specifyColumn.put("startTime", String.valueOf(System.currentTimeMillis()/1000));
        specifyColumn.put("fitnessID", "10");
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance3(db);
        else
            time = fix3(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    String[] columns = new String[]{"Sum(calorie)", "Sum(duration)", "Sum(count)", "Sum(distance)", "Count(*)"};
    String TABLE_NAME = "Workoutitem";


    private void getWeeklyView(int currentWeek, int columnIdx)
    {
        String startDate = "DATE('now', 'localtime', '" + ((currentWeek * 7) - 6) + " days', 'weekday 0')";
        String endDate = "DATE('now', 'localtime', '" + (currentWeek * 7) + " days', 'weekday 6')";
        String sql = "SELECT strftime('%w', datetime(startTime, 'unixepoch', 'localtime')) AS weekday, " + this.columns[columnIdx] + " from " + TABLE_NAME + " where date(startTime, 'unixepoch', 'localtime') >= " + startDate + " AND date(startTime, 'unixepoch', 'localtime') <= " + endDate + " AND fitnessID >= 7 AND fitnessID <= 12" + " Group by weekday";
        if(columnIdx == 0)
            sql = "SELECT strftime('%w', datetime(startTime, 'unixepoch', 'localtime')) AS weekday, " + this.columns[columnIdx] + ", Sum(duration)" + " from " + TABLE_NAME + " where date(startTime, 'unixepoch', 'localtime') >= " + startDate + " AND date(startTime, 'unixepoch', 'localtime') <= " + endDate + " AND fitnessID >= 7 AND fitnessID <= 12" + " Group by weekday";

        t1 = System.currentTimeMillis();
        Cursor cursor = this.database.rawQuery(sql, null);
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        while (!cursor.isAfterLast()) {
            log(cursor.getInt(0), cursor.getFloat(1));
            cursor.moveToNext();
        }
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
    }

    private void log(int anInt, float aFloat) {
        //System.out.println(anInt + " " + aFloat );
    }

    private List<List<Long>> instance3(SQLiteDatabase db) {
        time = new ArrayList<>();

        getWeeklyView(this.currentWeek, 0);
        getWeeklyView(this.currentWeek, 1);


        return time;
    }



    private List<List<Long>> fix3(SQLiteDatabase db) {
        time = new ArrayList<>();

        String startDate = "DATE('now', 'localtime', '" + ((currentWeek * 7) - 6) + " days', 'weekday 0')";
        String endDate = "DATE('now', 'localtime', '" + (currentWeek * 7) + " days', 'weekday 6')";
        String sql = "SELECT strftime('%w', datetime(startTime, 'unixepoch', 'localtime')) AS weekday, Sum(calorie), Sum(duration)" + " from " + TABLE_NAME + " where date(startTime, 'unixepoch', 'localtime') >= " + startDate + " AND date(startTime, 'unixepoch', 'localtime') <= " + endDate + " AND fitnessID >= 7 AND fitnessID <= 12" + " Group by weekday";

        t1 = System.currentTimeMillis();
        Cursor cursor = this.database.rawQuery(sql, null);
        cursor.moveToFirst();
        t2 = System.currentTimeMillis();
        while (!cursor.isAfterLast()) {
            log(cursor.getInt(0), cursor.getFloat(1));
            log(cursor.getInt(0), cursor.getFloat(2));
            cursor.moveToNext();
        }
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return time;
    }

    /*
    Antipattern:NotMergingProjectionPredicates
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.omrup.cell.tracker.apk
    CallChain1:<com.omrup.cell.tracker.Firebase.FirebaseDataReceiver: void onReceive(android.content.Context,android.content.Intent)>@64->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_member_name(java.lang.String)>
    Location1:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_member_name(java.lang.String)>@524
    Query1:[select member_name from member where member_id='unknown@method@<java.lang.object: java.lang.string tostring()>@<com.omrup.cell.tracker.firebase.firebasedatareceiver: void onreceive(android.content.context,android.content.intent)>@52@117!!!']
    Query Form1:[select from where]
    Table Form1:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
    CallChain2:<com.omrup.cell.tracker.Firebase.FirebaseDataReceiver: void onReceive(android.content.Context,android.content.Intent)>@66->
    <com.omrup.cell.tracker.Data.DataBase: java.lang.String get_member_token(java.lang.String)>
    Location2:<com.omrup.cell.tracker.Data.DataBase: java.lang.String get_member_token(java.lang.String)>@538
    Query2:[select member_token from member where member_id='unknown@method@<java.lang.object: java.lang.string tostring()>@<com.omrup.cell.tracker.firebase.firebasedatareceiver: void onreceive(android.content.context,android.content.intent)>@52@117!!!']
    Query Form2:[select from where]
    Table Form2:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */
    String member_id = "100000";
    public void experiment4(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        sqldb = db;
        String tableCreate = "CREATE TABLE MEMBER(member_id text,member_name text,member_email text,member_type text,member_last_latitude text,member_last_logitude text,member_last_date text,member_last_time text,member_last_address text,member_token text,member_signal_strength text,member_battery text,member_wifi text,member_mobile  text,member_speed text,member_speed_limit text,member_profile text)";


        Map<String, String> specifyColumn = new HashMap<>();
        specifyColumn.put("member_id", member_id);
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance4(db);
        else
            time = fix4(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public String get_member_name(String member_id) {
        String str = "";
        try {
            t1 = System.currentTimeMillis();
            Cursor cursor = this.sqldb.rawQuery("select member_name from MEMBER where member_id='" + member_id + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                t2= System.currentTimeMillis();
                str = cursor.getString(0);
                t3 = System.currentTimeMillis();
                Helper.addTimeStamp(time, t1, t2, 0);
                Helper.addTimeStamp(time, t2, t3, 1);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String get_member_token(String member_id) {
        String str = "";
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select member_token from MEMBER where member_id='" + member_id + "'", null);
        cursor.moveToNext();
        t2= System.currentTimeMillis();
        str = cursor.getString(0);
        t3 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        cursor.close();
        return str;
    }

    private List<List<Long>> instance4(SQLiteDatabase db) {
        time = new ArrayList<>();
        log(get_member_name(member_id), get_member_token(member_id));
        return time;
    }



    private List<List<Long>> fix4(SQLiteDatabase db) {
        time = new ArrayList<>();
        t1 = System.currentTimeMillis();
        Cursor cursor = this.sqldb.rawQuery("select member_name, member_token from MEMBER where member_id='" + member_id + "'", null);
        cursor.moveToNext();
        t2 = System.currentTimeMillis();
        log(cursor.getString(0), cursor.getString(1));
        t3 = System.currentTimeMillis();
        cursor.close();

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t2, t3, 1);
        return time;
    }

}