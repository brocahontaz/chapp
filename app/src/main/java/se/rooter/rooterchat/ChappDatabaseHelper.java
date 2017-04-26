package se.rooter.rooterchat;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class ChappDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Chapp.db";
    private static final String USERS_TABLE_NAME = "users";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_IMGBLOB ="imgblob";
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + USERS_TABLE_NAME + "("+USERS_COLUMN_ID+" STRING PRIMARY KEY, "+USERS_COLUMN_IMGBLOB+" BLOB)";
    private static final String IMG_QUERY = "SELECT * FROM "+USERS_TABLE_NAME+" WHERE "+USERS_COLUMN_ID+" = '";

    public ChappDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    public void insertData(String userID, Bitmap img) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERS_COLUMN_ID, userID);
        cv.put(USERS_COLUMN_IMGBLOB, DbBitmapUtility.getBytes(img));
        database.insert(USERS_TABLE_NAME, null, cv);
    }

    public Bitmap getData(String userID) throws SQLiteException {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from users where id = '"+userID+"'", null);
        Bitmap img;
        byte[] arr;
        cursor.moveToFirst();
        arr = cursor.getBlob(cursor.getColumnIndex(USERS_COLUMN_IMGBLOB));
        img = DbBitmapUtility.getImage(arr);
        cursor.close();
        return img;
    }

    public boolean checkIfRecordExists(String userID) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from "+USERS_TABLE_NAME+" where "+USERS_COLUMN_ID+" = '"+userID+"'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
