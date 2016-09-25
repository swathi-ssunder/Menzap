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
    private static final String COLUMN_SENDER = "SENDER";
    private static final String COLUMN_EMAIL_ID = "EMAIL_ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_IS_FRIEND = "IS_FRIEND";
    private static final String COLUMN_TIME_STAMP = "TIMESTAMP";
    private static final String COLUMN_UNIQUE_ID = "UNIQUE_ID";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SENDER + " TEXT, " +
                        COLUMN_EMAIL_ID + " TEXT, " +
                        COLUMN_NAME+ " TEXT, " +
                        COLUMN_IS_FRIEND + " INTEGER, " +
                        COLUMN_TIME_STAMP + " INTEGER, " +
                        COLUMN_UNIQUE_ID + " INTEGER, " +
                        "CONSTRAINT unq UNIQUE (" + COLUMN_TIME_STAMP + ", " +
                        COLUMN_TIME_STAMP + ") " +
                        "ON CONFLICT IGNORE" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USER");
        onCreate(db);
    }

    public boolean insert(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, user.getSender());
        contentValues.put(COLUMN_EMAIL_ID, user.getEmailId());
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_IS_FRIEND, user.getIsFriend());
        contentValues.put(COLUMN_TIME_STAMP, user.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, user.getUniqueId());
        long result = db.insert("USER", null, contentValues);
        db.close();

        return (result != -1);
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USER WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(int id, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, user.getSender());
        contentValues.put(COLUMN_EMAIL_ID, user.getEmailId());
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_IS_FRIEND, user.getIsFriend());
        contentValues.put(COLUMN_TIME_STAMP, user.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, user.getUniqueId());

        db.update("USER", contentValues, "ID = ? ", new String[]{Integer.toString(id)});
        db.close();

        return true;
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
        Cursor res = db.rawQuery("SELECT * FROM USER WHERE IS_FRIEND IN (0,1)", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            User user = new User(
                    res.getString(res.getColumnIndex(COLUMN_SENDER)),
                    res.getString(res.getColumnIndex(COLUMN_EMAIL_ID)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_IS_FRIEND)),
                    res.getInt(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getInt(res.getColumnIndex(COLUMN_UNIQUE_ID))
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
        Cursor res = db.rawQuery("SELECT COUNT(*) AS REC_COUNT FROM USER WHERE IS_FRIEND=-1;", null);
        res.moveToFirst();

        count = res.getInt(0);
        res.close();

        return count;
    }

}