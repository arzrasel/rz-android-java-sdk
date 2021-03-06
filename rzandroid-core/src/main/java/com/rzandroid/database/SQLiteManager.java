package com.rzandroid.database;

import android.content.Context;

public class SQLiteManager {
    private Context context;
    public SQLiteHelper sqliteHelper = null;
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

    public SQLiteHelper openDatabase() {
        sqliteHelper = new SQLiteHelper(context, dbFileName, dbDirName, dbVersion)
                .setDatabaseVersion(dbVersion);
        if (sqliteHelper != null) {
            sqliteHelper.onOpenDatabase();
        }
        return sqliteHelper;
    }

    public void closeDatabase(SQLiteHelper argSqLiteDBCopyHelper) {
        if (sqliteHelper != null) {
            sqliteHelper.onCloseDatabase();
            sqliteHelper = null;
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
            sqliteHelper.onBackupDb();
        }
    }
}
