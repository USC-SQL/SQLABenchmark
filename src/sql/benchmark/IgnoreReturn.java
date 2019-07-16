package sql.benchmark;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IgnoreReturn {
    private int rowSize, fieldSize, querySize;

    /*
    /home/yingjun/Documents/SQLUsage/appset/1000apps/br.com.easytaxi.apk
    IgnoreReturn
    CallChain:<br.com.easytaxi.db.CreditCardRecord: void b()>
    Location:<br.com.easytaxi.db.CreditCardRecord: void b()>@172
    Query:[delete from creaditcard where card_id = ?]
    Query Form:[delete from where]
    Table Form:[]
    */

    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;

        String tableCreate = "CREATE TABLE creditcard (_id INTEGER PRIMARY KEY AUTOINCREMENT, card_id TEXT, name TEXT, flag TEXT, lastdigits TEXT, image TEXT, favorite INTEGER NOT NULL DEFAULT 0)";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String i = "creditcard";
        String cardId = "abc";
        long t1 = System.currentTimeMillis();
        db.delete(i, "card_id=?", new String[]{String.valueOf(cardId)});
        long t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private void log()
    {

    }


    private List<List<Long>> fix1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();
        String i = "creditcard";
        String cardId = "abc";
        long t1 = System.currentTimeMillis();
        int rowAffected = db.delete(i, "card_id=?", new String[]{String.valueOf(cardId)});
        long t2 = System.currentTimeMillis();

        long t3 = System.currentTimeMillis();
        if(rowAffected <= 0)
        {
            log();
        }
        long t4 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }
}