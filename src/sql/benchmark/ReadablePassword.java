package sql.benchmark;

import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import edu.usc.SQLTesting.Environment;
import edu.usc.SQLTesting.Helper;
import edu.usc.SQLTesting.MySQLiteHelper;
import edu.usc.SQLTesting.Reporter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.*;

public class ReadablePassword {
    private static int rowSize, fieldSize, querySize;


    SQLiteDatabase db;
    long t1, t2, t3 ,t4;

    /*
    Antipattern:ReadablePassword
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.bsro.tp.apk
    CallChain:<com.bsro.tp.DriverInfoAccountActivity$6: void onPostExecute(java.lang.Object)>@1->
    <com.bsro.tp.DriverInfoAccountActivity$6: void onPostExecute(java.lang.String)>@232->
    <com.bsro.tp.util.loginRegUtil: boolean setRegValue(com.bsro.tp.database.DBAccessor,java.lang.String,java.lang.String)>@228->
    <com.bsro.tp.database.DBAccessor: boolean write(java.lang.String)>
    Location:<com.bsro.tp.database.DBAccessor: boolean write(java.lang.String)>@250
    Query:[update reg set password = \"unknown@method@<java.lang.object: java.lang.string tostring()>@<com.bsro.tp.driverinfoaccountactivity$6: void onpostexecute(java.lang.string)>@234@71!!!\"]
    Query Form:[update set]
    Table Form:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
    */
    public void experiment1(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;
        this.db = db;
        String tableCreate = "CREATE TABLE IF NOT EXISTS REG(USERID TEXT DEFAULT '',EMAIL TEXT DEFAULT '',PREV_EMAIL TEXT DEFAULT '',PASSWORD TEXT DEFAULT '',APP_TYPE TEXT DEFAULT '',REG_DATE TEXT DEFAULT '',LAST_BACKUP_DATE TEXT DEFAULT '',LAST_MODIFIED_DESC TEXT DEFAULT '')";
        MySQLiteHelper.populate(db, tableCreate, rowSize, fieldSize, null);
        List<List<Long>> time;
        if(!exp.isFixed)
            time = instance1(db);
        else
            time = fix1(db);
        MySQLiteHelper.dropTable(db, tableCreate);
        Reporter.print(exp, time);

    }

    public boolean setRegValue(String strColumnName, String strNewValue) {
        return write("UPDATE REG SET " + strColumnName + " = \"" + strNewValue + "\"");
    }

    public boolean write(String sql) {
        boolean bRet = false;
        SQLiteDatabase database = db;

        t1 = System.currentTimeMillis();
        database.beginTransaction();
        try {
            database.execSQL(sql);
            database.setTransactionSuccessful();
            bRet = true;
        } catch (Exception exception) {
            Log.e("MyTiresplus:DBAccessor", exception.toString());
        } finally {
            database.endTransaction();
        }
        t2 = System.currentTimeMillis();
        return bRet;
    }

    private void log()
    {

    }

    private List<List<Long>> instance1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        char[] value = new char[fieldSize];
        Arrays.fill(value, 'a');
        String pwd = new String(value);

        boolean saveRegPwdNew = setRegValue("password", pwd);

        if(saveRegPwdNew)
        {
            log();
        }

        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }

    private String encrypt(String password)
    {
        try {
            SecretKeySpec key = generateKey(password);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal("abc".getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            return encryptedValue;
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
            return password;
        }
    }

    private SecretKeySpec generateKey(String password)  throws  Exception
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    private List<List<Long>> fix1(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        char[] value = new char[fieldSize];
        Arrays.fill(value, 'a');
        String pwd = new String(value);

        t3 = System.currentTimeMillis();
        String encrypted = encrypt(pwd);
        t4 = System.currentTimeMillis();
        boolean saveRegPwdNew = setRegValue("password", encrypted);


        if(saveRegPwdNew)
        {
            log();
        }

        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }

    /*
    Antipattern:ReadablePassword
    App:/home/yingjun/Documents/SQLUsage/appset/1000apps/com.omrup.cell.tracker.apk
    CallChain:<com.omrup.cell.tracker.Data.DataBase: void onCreate(android.database.sqlite.SQLiteDatabase)>
    Location:<com.omrup.cell.tracker.Data.DataBase: void onCreate(android.database.sqlite.SQLiteDatabase)>@59
    Query:[insert into user(login,user_id,user_name,user_password,user_email,user_type,user_last_latitude,user_last_logitude,user_last_date,user_last_time,user_last_address,user_signal_strength,user_battery,user_wifi,user_mobile,user_speed,user_buy)values('false','','','','','','','','','','','','','','','','false')]
    Query Form:[insert into values]
    Table Form:[[text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text, text]]
     */

    public void experiment2(SQLiteDatabase db, Environment exp) {
        rowSize = exp.rowSize;
        fieldSize = exp.fieldSize;
        querySize = exp.querySize;


        String tableCreate = "CREATE TABLE USER(login text,user_id text,user_name text,user_password text,user_email text,user_type text,user_last_latitude text,user_last_logitude text,user_last_date text,user_last_time text,user_last_address text,user_signal_strength text,user_battery text,user_wifi text,user_mobile text,user_speed text,user_speed_limit text,user_profile text,user_buy text)";

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
        List<List<Long>> time = new ArrayList<>();

        t1 = System.currentTimeMillis();
        db.execSQL("INSERT INTO USER(login,user_id,user_name,user_password,user_email,user_type,user_last_latitude,user_last_logitude,user_last_date,user_last_time,user_last_address,user_signal_strength,user_battery,user_wifi,user_mobile,user_speed,user_buy)values('false','','','','','','','','','','','','','','','','false')");
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        return time;
    }



    private List<List<Long>> fix2(SQLiteDatabase db) {
        List<List<Long>> time = new ArrayList<>();

        t3 = System.currentTimeMillis();
        String encrypted = encrypt("");
        t4 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        db.execSQL("INSERT INTO USER(login,user_id,user_name,user_password,user_email,user_type,user_last_latitude,user_last_logitude,user_last_date,user_last_time,user_last_address,user_signal_strength,user_battery,user_wifi,user_mobile,user_speed,user_buy)values('false','', ? ,'','','','','','','','','','','','','','false')", new String[]{encrypted});
        t2 = System.currentTimeMillis();
        Helper.addTimeStamp(time, t1, t2, 0);
        Helper.addTimeStamp(time, t3, t4, 1);
        return time;
    }
}