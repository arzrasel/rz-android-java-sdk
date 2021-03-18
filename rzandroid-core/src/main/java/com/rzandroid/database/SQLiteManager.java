package com.rzandroid.database;

import android.content.Context;

public class SQLiteManager {
    private Context context;
    private SQLiteHelper sqLiteHelper = null;
    //private Cursor cursor;
    private String dbFileName = "db-SQLite.sqlite3";
    private String dbDirName = "/database/";
    private int dbVersion = 1;
    private boolean isDebug = false;
    private String debugString = "";

    public SQLiteManager(Context argContext) {
        context = argContext;
        isDebug = false;
        /*if (BuildConfig.DEBUG) {
            isDebug = true;
        }*/
    }

    public SQLiteManager setIsDebugging(boolean argIsDebugging) {
        isDebug = argIsDebugging;
        return this;
    }

    public SQLiteManager setDbFileName(String argDbFileName) {
        dbFileName = argDbFileName;
        return this;
    }

    public SQLiteManager setDbDirectoryName(String argDbDirName) {
        dbDirName = argDbDirName;
        return this;
    }

    public SQLiteManager setDbVersion(int argDbVersion) {
        dbVersion = argDbVersion;
        return this;
    }

    protected SQLiteHelper openDatabase() {
        sqLiteHelper = new SQLiteHelper(context, dbFileName, dbDirName, dbVersion)
                .setDatabaseVersion(dbVersion);
        if (sqLiteHelper != null) {
            sqLiteHelper.onOpenDatabase();
        }
        return sqLiteHelper;
    }

    protected void closeDatabase(SQLiteHelper argSqLiteDBCopyHelper) {
        if (sqLiteHelper != null) {
            sqLiteHelper.onCloseDatabase();
            sqLiteHelper = null;
        }
        if (argSqLiteDBCopyHelper != null) {
            argSqLiteDBCopyHelper = null;
        }
    }

    public void sysLog(String argMsg) {
        if (isDebug) {
            System.out.println();
            System.out.println("|----|--------DEBUG_LOG: " + argMsg);
        }
    }

    public void onBackupDb(boolean argIsForceBackUp) {
        //System.out.println("|----|--------PRINT: DATABASE_BACKUP");
        if (isDebug && !argIsForceBackUp) {
            //System.out.println("|----|--------DEBUG_LOG: DATABASE_BACKUP");
            //SQLiteDBCopyHelper sqLiteDBCopyHelper = new SQLiteDBCopyHelper(context, dbFile, dbDir);
            sqLiteHelper.onBackupDb();
        }
    }
}
