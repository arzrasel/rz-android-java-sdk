package com.rzandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteHelper extends SQLiteOpenHelper {
    //private static String DB_PATH = "";
    private static String SYS_DATABASE_PATH = "";
    private static String ASSETS_DATABASE_PATH = "";
    private static String DATABASE_NAME = "db-name.sqlite3";
    private static int DATABASE_VERSION = 1;
    private Context context;
    private SQLiteDatabase sqliteDatabase;

    //
    /*public SQLiteHelper(Context argContext, String argDatabaseName, String argAssetsPath) {
        super(argContext, argDatabaseName, null, DATABASE_VERSION);
    }*/
    public SQLiteHelper(Context argContext, String argDatabaseName, String argAssetsPath, int argVersion) {
        super(argContext, argDatabaseName, null, argVersion);
        this.context = argContext;
        DATABASE_NAME = argDatabaseName;
        SYS_DATABASE_PATH = argContext.getDatabasePath(DATABASE_NAME).getPath();
        ASSETS_DATABASE_PATH = argDatabaseName;
        DATABASE_VERSION = argVersion;
        //
        if (null != argAssetsPath && !argAssetsPath.isEmpty()) {
            String newAssetsPath = argAssetsPath;
            int startIndex = argAssetsPath.indexOf("/", 0);
            int endIndex = argAssetsPath.lastIndexOf("/");
            if (startIndex == 0) {
                newAssetsPath = argAssetsPath.substring(1);
                endIndex = newAssetsPath.lastIndexOf("/");
            }
            if (endIndex != -1) {
                newAssetsPath =
                        newAssetsPath.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
            }
            ASSETS_DATABASE_PATH = newAssetsPath + "/" + argDatabaseName;
            //String whatyouaresearching = myString.substring(0, myString.lastIndexOf("/"))
            //System.out.println("START_INDEX: " + endIndex);
        }
        //System.out.println(" ASSETS_DATABASE_PATH: " + ASSETS_DATABASE_PATH);
        //System.out.println("SYSTEM_DATABASE_PATH: " + SYS_DATABASE_PATH);
        /*try {
            //Arrays.asList(context.getResources().getAssets().list("")).contains(‌​"myFile");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (!isAssetFileExists(context, ASSETS_DATABASE_PATH)) {
            //System.out.println("File not exists: " + ASSETS_DATABASE_PATH);
            return;
        }
        try {
            onCreateSysDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }

    private boolean isAssetFileExists(Context argContext, String argFielPathInAssetsDir) {
        boolean retVal = false;
        AssetManager assetManager = argContext.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(argFielPathInAssetsDir);
            if (null != inputStream) {
                retVal = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retVal;
    }

    private void onCreateSysDatabase() throws IOException {
        if (!onCheckSysDatabase()) {
            this.getReadableDatabase();
            onCopyToSysDatabase();
            this.close();
        }
    }

    private boolean onCheckSysDatabase() {
        //File DbFile = new File(DB_PATH + DB_NAME);
        File dbFile = new File(SYS_DATABASE_PATH);
        return dbFile.exists();
    }

    public SQLiteHelper setDatabaseVersion(int argVersion) {
        DATABASE_VERSION = argVersion;
        return this;
    }

    public boolean onOpenDatabase() throws SQLException {
        sqliteDatabase = SQLiteDatabase.openDatabase(SYS_DATABASE_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return sqliteDatabase != null;
    }

    /*public void openDatabase() throws SQLException {
        /-*String myPath = DATABASE_PATH + DATABASE_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);*-/
    }*/

    public synchronized void onCloseDatabase() {
        if (sqliteDatabase != null) sqliteDatabase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    private void onCopyToSysDatabase() throws IOException {
        InputStream mInput = context.getAssets().open(ASSETS_DATABASE_PATH);
        String outfileName = SYS_DATABASE_PATH;
        OutputStream mOutput = new FileOutputStream(outfileName);
        byte[] buffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(buffer)) > 0) {
            mOutput.write(buffer, 0, mLength);
        }
        mOutput.flush();
        mInput.close();
        mOutput.close();
        //System.out.println("FILE_COPIED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase argSqLiteDatabase, int argOldVersion, int argNewVersion) {
        int version = argSqLiteDatabase.getVersion();
        if (argNewVersion > argOldVersion) {
            //Log.v("Database Upgrade", "Database version higher than old.");
            onDeleteSysDatabase();
            try {
                onCreateSysDatabase();
            } catch (IOException ioe) {

                throw new Error("Unable to create database");
            }
        }
    }

    public void onDeleteSysDatabase() {
        File file = new File(SYS_DATABASE_PATH);
        if (file.exists()) {
            file.delete();
            //System.out.println("delete database file.");
        }
    }

    public long onInsert(String argTableName, ContentValues argContentValues) {
        long result = sqliteDatabase.insert(argTableName, null, argContentValues);
        //sqLiteDatabase.insert(argTableName, null, argContentValues);
        /*if (result == -1) {
            return false;
        } else {
            return true;
        }*/
        return result;
    }

    public long onUpdate(String argTableName, ContentValues argContentValues, String argWhereClause) {
        long retVal = -1;
        //sqLiteDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        //id = idValue AND name = nameValue
        //NAME + " = ? AND " + LASTNAME + " = ?", new String[]{"Name", "Last name"}
        if (argWhereClause.isEmpty()) {
            retVal = sqliteDatabase.update(argTableName, argContentValues, null, null);
        } else {
            retVal = sqliteDatabase.update(argTableName, argContentValues, argWhereClause, null);
        }
        return retVal;
    }

    public void onDelete(String argTableName, String argWhereClause) {
        //KEY_DATE + "='date' AND " + KEY_GRADE + "='style2' AND " + KEY_STYLE + "='style'"
        int result = -1;
        if (argWhereClause.isEmpty()) {
            result = sqliteDatabase.delete(argTableName, null, null);
        } else {
            result = sqliteDatabase.delete(argTableName, argWhereClause, null);
        }
        //System.out.println("IS_WORK: " + result);
        //return sqLiteDatabase.delete(argTableName, argWhereClause, null);
    }

    public void onBackupDb() {
        try {
            //Uri file = Uri.fromFile(new File(filePath));
            File dbFile = new File(SYS_DATABASE_PATH);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(dbFile.toString());
            String fileFullName = dbFile.getName();
            int onlyFileNameLength = fileFullName.length() - fileExtension.length() - 1;
            String onlyFileName = fileFullName.substring(0, onlyFileNameLength);
            ///
            String outputDirectory = Environment.getExternalStorageDirectory()
                    + "/" + context.getPackageName()
                    + "/database";
            makeDir(outputDirectory);
            ///
            FileInputStream fileInputStream = new FileInputStream(dbFile);

            String outFileName = outputDirectory + "/" + onlyFileName + "-backup." + fileExtension;

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeDir(String argDirectoryPath) {
        File file = new File(argDirectoryPath);
        if (!isDirExists(file)) {
            file.mkdirs();
            //sysLog("File not exists");
        } else {
            System.out.println("File exists");
        }
    }

    private boolean isDirExists(File argFile) {
        return (argFile.exists() && argFile.isDirectory());
    }

    public Cursor getSqlQueryResults(String argSqlQuery) {
        Cursor cursor = sqliteDatabase.rawQuery(argSqlQuery, null);
        return cursor;
    }

    public void onRawSqlQuery(String argSqlQuery) {
        sqliteDatabase.execSQL(argSqlQuery);
    }

    /*public void onRawQuery(String argSqlQuery) {
        sqLiteDatabase.rawQuery(argSqlQuery, null);
    }*/
    public boolean getBoolean(Cursor argCursor, int argColumnIndex) {
        if(argCursor.isNull(argColumnIndex) || argCursor.getShort(argColumnIndex) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getBoolean(Cursor argCursor, String argColumnName) {
        if(argCursor.isNull(argCursor.getColumnIndex(argColumnName)) || argCursor.getShort(argCursor.getColumnIndex(argColumnName)) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public ContentValues getContentValues(String argKey, String argValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(argKey, argValue);
        return contentValues;
    }
}
/*
private SQLiteDBCopyHelper sqLiteDBCopyHelper = null;
private SQLiteDatabase sqLiteDatabase;
//|------------------------------------------------------------|
sqLiteDBCopyHelper = new SQLiteDBCopyHelper(context, "db_cmdss.sqlite3", "/database/");
//|------------------------------------------------------------|
if (sqLiteDBCopyHelper != null) {
    sqLiteDBCopyHelper.onOpenDatabase();
    String sqlQuery = "";
    ContentValues contentValues = new ContentValues();
    contentValues.put("tdm_id", System.currentTimeMillis() / 1000);
    contentValues.put("tdm_subject", "Message Subject");
    contentValues.put("tdm_body", "Message Body");
    contentValues.put("tdm_sender", userSession.getUserMobileNo());
    contentValues.put("tdm_receiver_type", "Test");
    contentValues.put("tdm_receiver", "all");
    sqLiteDBCopyHelper.onInsertData("tbl_draft_message", contentValues);
    sqlQuery = " SELECT * FROM tbl_draft_message ORDER BY tdm_subject ";
    Cursor cursor;
    cursor = sqLiteDBCopyHelper.getSqlQueryResults(sqlQuery);
    cursor.moveToFirst();
    while (cursor.moveToNext()) {
        String msgSubject = cursor.getString(cursor.getColumnIndex("tdm_subject"));
        String msgBody = cursor.getString(cursor.getColumnIndex("tdm_body"));
        System.out.println("MESSAGE: " + msgSubject + " - " + msgBody);
    }
    sqLiteDBCopyHelper.onCloseDatabase();
}
cursor.getString(0);
cursor.getCount()
if(cursor != null && cursor.getCount() > 0)
*/