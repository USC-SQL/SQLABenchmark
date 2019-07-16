package edu.usc.SQLTesting;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.Arrays;
import java.util.Map;


public class MySQLiteHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "sqlbench.db";
    private static final int DATABASE_VERSION = 1;

    /*
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_COMMENT
            + " text);";
     */

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void dropTable(SQLiteDatabase db, String tableCreate)
    {
        try {
            Statement statement = CCJSqlParserUtil.parse(tableCreate);
            if (statement instanceof CreateTable) {
                CreateTable createTable = (CreateTable) statement;
                Table table = createTable.getTable();
                String tableName = table.getName();
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            }
            else
            {
                throw new Exception("Not a table creation statement");
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }
    public static void addData(SQLiteDatabase db, String tableCreate, int rowSize, int entrySize, Map<String, String> specifyColumnValue)
    {
        try {
            Statement statement = CCJSqlParserUtil.parse(tableCreate);
            if(statement instanceof CreateTable)
            {

                CreateTable createTable = (CreateTable) statement;
                Table table = createTable.getTable();
                String tableName = table.getName();
                //System.out.println(tableName);


                ContentValues cv = new ContentValues();
                for(ColumnDefinition column : createTable.getColumnDefinitions())
                {
                    String columnName = column.getColumnName();
                    ColDataType colDataType = column.getColDataType();
                    String type = colDataType.getDataType().toUpperCase();

                    //System.out.println(columnName + " " + type);
                    boolean isPrimaryKey = false;
                    if(column.getColumnSpecStrings() != null)
                    {

                        isPrimaryKey = column.getColumnSpecStrings().toString().toUpperCase().contains("PRIMARY, KEY");
                        if(isPrimaryKey && !column.getColumnSpecStrings().toString().toUpperCase().contains("AUTOINCREMENT"))
                            throw new Exception("Primary key " + columnName + " should be AUTOINCREMENT");

                    }

                    if(specifyColumnValue != null && specifyColumnValue.containsKey(columnName))
                    {
                        String value = specifyColumnValue.get(columnName);
                        //is integer
                        if(value.matches("-?\\d+"))
                            cv.put(columnName, Integer.parseInt(value));
                        else  if(value.equals("null"))
                            ;
                        else
                            cv.put(columnName, specifyColumnValue.get(columnName));
                        continue;
                    }
                    switch(type)
                    {
                        case "BLOB":
                        {
                            byte[] value = new byte[entrySize];
                            Arrays.fill(value, (byte) 1);
                            cv.put(columnName, value);
                            break;
                        }
                        case "REAL":
                        case "FLOAT":
                        {
                            cv.put(columnName, 3.1415926);
                            break;
                        }
                        case "NUMBER":
                        case "NUMERIC":
                        case "LONG":
                        case "INTEGER":
                        {
                            if(!isPrimaryKey)
                                cv.put(columnName, 31415926L);
                            break;
                        }
                        case "STRING":
                        case "VARCHAR":
                        case "TEXT":
                        {
                            char[] value = new char[entrySize];
                            Arrays.fill(value, 'a');
                            cv.put(columnName, new String(value));
                            break;
                        }

                        case "BOOLEAN":
                        {
                            cv.put(columnName, true);
                            break;
                        }

                        default:
                        {
                            System.err.println("handle auto populating type" + type);
                        }
                    }
                }

                db.beginTransaction();
                for(int i = 0; i < rowSize; i++)
                {
                    db.insert(tableName, null, cv);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            else
            {
                throw new Exception("Not a table creation statement");
            }
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }
    //the key of specifyColumnValue is case sensitive
    public static void populate(SQLiteDatabase db, String tableCreate, int rowSize, int entrySize, Map<String, String> specifyColumnValue)
    {
        try {
            Statement statement = CCJSqlParserUtil.parse(tableCreate);
            if (statement instanceof CreateTable) {


                CreateTable createTable = (CreateTable) statement;
                Table table = createTable.getTable();
                String tableName = table.getName();
                //System.out.println(tableName);

                //create table
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
                db.execSQL(tableCreate);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        addData(db, tableCreate, rowSize, entrySize, specifyColumnValue);

    }
}
