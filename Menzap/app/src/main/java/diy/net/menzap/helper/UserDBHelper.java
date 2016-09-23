package diy.net.menzap.helper;

/**
 * Created by vivek-sethia on 23.09.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

import diy.net.menzap.model.User;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MENZAP";
    private static final String TABLE_NAME = "USER";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_EMAIL_ID = "EMAIL_ID";
    private static final String COLUMN_IS_FRIEND = "IS_FRIEND";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_EMAIL_ID + " TEXT, " +
                        COLUMN_IS_FRIEND + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER");
        onCreate(db);
    }

    public boolean insert(String emailId, int isFriend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL_ID, emailId);
        contentValues.put(COLUMN_IS_FRIEND, isFriend);
        db.insert("USER", null, contentValues);

        return true;
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USER WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }


    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("USER",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<User> getAll() {
        ArrayList<User> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM USER", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            User user = new User(
                    res.getString(res.getColumnIndex(COLUMN_EMAIL_ID)),
                    res.getInt(res.getColumnIndex(COLUMN_IS_FRIEND))
            );
            array_list.add(user);
            res.moveToNext();
        }
        res.close();

        return array_list;
    }

    public int isRegistered() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) AS REC_COUNT FROM USER WHERE IS_FRIEND=0;", null);
        res.moveToFirst();

        count = res.getInt(0);
        res.close();

        return count;
    }

}