package diy.net.menzap.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ReviewDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MENZAP";
    public static final String TABLE_NAME = "REVIEW";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DISH_NAME = "DISH_NAME";
    public static final String COLUMN_IS_LIKE = "IS_LIKE";

    public ReviewDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE REVIEW (ID INTEGER PRIMARY KEY, DISH_NAME TEXT, IS_LIKE INTEGER DEFAULT 0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS REVIEW");
        onCreate(db);
    }

    public boolean insert(String name, Integer isLike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DISH_NAME", name);
        contentValues.put("IS_LIKE", isLike);
        db.insert("REVIEW", null, contentValues);
        return true;
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM REVIEW WHERE ID=" + id + "", null);
        return res;
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(Integer id, String name, Integer isLike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DISH_NAME", name);
        contentValues.put("IS_LIKE", isLike);
        db.update("REVIEW", contentValues, "ID = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("REVIEW",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAll() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM REVIEW", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_DISH_NAME)));
            array_list.add(res.getString(res.getColumnIndex(COLUMN_IS_LIKE)));
            res.moveToNext();
        }

        return array_list;
    }
}