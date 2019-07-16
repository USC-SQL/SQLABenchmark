package sql.benchmark;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotUsingParamerizedQuery {

    private int rowSize, fieldSize, querySize;

    private void checkDBConsistency(SQLiteDatabase db, String tableName)
    {
        System.out.println(DatabaseUtils.queryNumEntries(db, tableName));
    }
    /*
    Antipattern:NotUsingParameterizedQueries
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.steam.photoeditor.extra.sticker.free.sixpackabs.apk
    CallChain:<com.gau.go.gostaticsdk.database.DataBaseHelper: void delete(java.util.ArrayList)>
    Location:<com.gau.go.gostaticsdk.database.DataBaseHelper: void delete(java.util.ArrayList)>@278
    Query:[delete from statistics_new where id=unknown@field@<com.gau.go.gostaticsdk.beans.postbean: java.lang.string mid>!!!]
    Query Form:[delete from where]
    Table Form:[]
     */

    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate  = "create table IF NOT EXISTS " + TABLE_STATISTICS_NEW + "(" + "id text, " + "funid numeric, " + "data text, " + "time text, " + "opcode numeric," + "isold boolean, " + "network numeric" + ")";

        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public String TABLE_STATISTICS_NEW = "statistics_new";
    public String TABLE_STATISTICS_COLOUM_ID = "id";

    public List<List<Long>> instance1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < querySize; i++)
        {
            list.add(i);
        }

        if (list != null && !list.isEmpty()) {
            long t1 = System.currentTimeMillis();
            db.beginTransaction();
            int i = 0;
            try {
                while (i < list.size()) {
                        db.execSQL("delete from " + TABLE_STATISTICS_NEW + " where " + TABLE_STATISTICS_COLOUM_ID + "=" + list.get(i));
                        i++;
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            } finally {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            long t2 = System.currentTimeMillis();
            Helper.addTimeStamp(time, t1, t2, 0);
        }

        checkDBConsistency(db, TABLE_STATISTICS_NEW);
        //System.out.println(db.rawQuery("select id from " + TABLE_STATISTICS_NEW, null).getCount());
        return time;
    }

    public List<List<Long>> fix1(SQLiteDatabase db)
    {
        List<List<Long>> time = new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < querySize; i++)
        {
            list.add(i);
        }

        if (list != null && !list.isEmpty()) {
            long t1 = System.currentTimeMillis();
            db.beginTransaction();
            int i = 0;
            SQLiteStatement statement = db.compileStatement("delete from " + TABLE_STATISTICS_NEW + " where " + TABLE_STATISTICS_COLOUM_ID + "= ?");
            try {
                while (i < list.size()) {

                    statement.bindString(1, list.get(i).toString());
                    statement.execute();
                    i++;
                }
            } catch (SQLiteException e) {
                    e.printStackTrace();
            } finally {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            long t2 = System.currentTimeMillis();
            Helper.addTimeStamp(time, t1, t2, 0);
        }
        checkDBConsistency(db, TABLE_STATISTICS_NEW);
        //System.out.println(db.rawQuery("select id from " + TABLE_STATISTICS_NEW, null).getCount());
        return time;
    }



    /*
    /home/yingjun/Documents/SQLUsage/appset/1000apps/com.fairplayer.apk contains NotUsingParameterizedQueries num:1
    Antipattern:NotUsingParameterizedQueries
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.fairplayer.apk
    CallChain:<com.fairplayer.FpServiceRendering: boolean handleMessage(android.os.Message)>@-1->
    <com.fairplayer.x: void a(com.fairplayer.ag)>@-1->
    <com.fairplayer.x: int b()>
    Location:<com.fairplayer.x: int b()>@-1
    Query:[delete from fp_database_listen_counter where type=2 and type_id=unknown@dynamic_var@$r4@<com.fairplayer.x: int b()>@-1@87!!!]
    Query Form:[delete from where and]
    Table Form:[Integer, Integer, Integer]
     */
    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE fp_database_listen_counter (type INTEGER, type_id INTEGER PRIMARY KEY AUTOINCREMENT, listen_count INTEGER);";


        Map<String, String> specifyColumn = new HashMap<>();

        specifyColumn.put("type", "2");
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, specifyColumn);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance2(db);
        else
            time = fix2(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance2(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        long t1 = System.currentTimeMillis();
        db.beginTransaction();
        for(int i = 0; i < querySize; i++)
        {
            db.execSQL("DELETE FROM fp_database_listen_counter WHERE type=2 AND type_id=" + i);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        long t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        checkDBConsistency(db, "fp_database_listen_counter");
        return time;
    }



    private List<List<Long>> fix2(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        SQLiteStatement statement = db.compileStatement("DELETE FROM fp_database_listen_counter WHERE type=2 AND type_id=?");
        db.beginTransaction();
        for(int i = 0; i < querySize; i++)
        {
            statement.bindLong(1, i);
            statement.execute();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        long t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        checkDBConsistency(db, "fp_database_listen_counter");
        return time;
    }

}

