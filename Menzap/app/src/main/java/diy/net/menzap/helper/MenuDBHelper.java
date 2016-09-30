package diy.net.menzap.helper;

/**
 * Created by viveksethia on 22.09.16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import diy.net.menzap.model.Menu;

public class MenuDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MENZAP";
    private static final String TABLE_NAME = "MENU";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_SENDER = "SENDER";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_CATEGORY = "CATEGORY";
    private static final String COLUMN_IS_LIKED = "IS_LIKED";
    private static final String COLUMN_IS_FAVOURITE= "IS_FAVOURITE";
    private static final String COLUMN_LIKE_COUNT= "LIKE_COUNT";
    private static final String COLUMN_SERVED_ON = "SERVED_ON";
    private static final String COLUMN_TIME_STAMP = "TIMESTAMP";
    private static final String COLUMN_UNIQUE_ID = "UNIQUE_ID";

    public MenuDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SENDER + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_SERVED_ON + " TEXT, " +
                        COLUMN_IS_LIKED + " INTEGER, " +
                        COLUMN_IS_FAVOURITE + " INTEGER, " +
                        COLUMN_LIKE_COUNT + " INTEGER, " +
                        COLUMN_TIME_STAMP + " TEXT, " +
                        COLUMN_UNIQUE_ID + " TEXT, " +
                        "UNIQUE (" + COLUMN_UNIQUE_ID + ", " +
                        COLUMN_TIME_STAMP + ", " + COLUMN_SENDER + ") " +
                        "ON CONFLICT IGNORE" +
                        ");"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MENU");
        onCreate(db);
    }

    public boolean insert(Menu menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, menu.getSender());
        contentValues.put(COLUMN_NAME, menu.getName());
        contentValues.put(COLUMN_DESCRIPTION, menu.getDescription());
        contentValues.put(COLUMN_CATEGORY, menu.getCategory());
        contentValues.put(COLUMN_SERVED_ON, menu.getServedOn());
        contentValues.put(COLUMN_IS_LIKED, menu.getIsLiked());
        contentValues.put(COLUMN_IS_FAVOURITE, menu.getIsFavourite());
        contentValues.put(COLUMN_LIKE_COUNT, menu.getLikeCount());
        contentValues.put(COLUMN_TIME_STAMP, menu.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, menu.getUniqueId());

        long result = db.insert("MENU", null, contentValues);
        db.close();
        return (result != -1);
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM MENU WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(Menu menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, menu.getSender());
        contentValues.put(COLUMN_NAME, menu.getName());
        contentValues.put(COLUMN_DESCRIPTION, menu.getDescription());
        contentValues.put(COLUMN_CATEGORY, menu.getCategory());
        contentValues.put(COLUMN_SERVED_ON, menu.getServedOn());
        contentValues.put(COLUMN_IS_LIKED, menu.getIsLiked());
        contentValues.put(COLUMN_IS_FAVOURITE, menu.getIsFavourite());
        contentValues.put(COLUMN_LIKE_COUNT, menu.getLikeCount());
        contentValues.put(COLUMN_TIME_STAMP, menu.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, menu.getUniqueId());

        long result = db.update("MENU", contentValues, "SENDER = ? AND UNIQUE_ID = ? AND TIMESTAMP = ?",
                new String[]{menu.getSender(), Long.toString(menu.getUniqueId()), Long.toString(menu.getTs())});
        db.close();

        return (result > 0);

    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MENU",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Menu> getAll() {
        ArrayList<Menu> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM MENU ORDER BY " +
                COLUMN_IS_FAVOURITE + " DESC, " + COLUMN_LIKE_COUNT + " DESC", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Menu menu = new Menu(
                    res.getString(res.getColumnIndex(COLUMN_SENDER)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)),
                    res.getInt(res.getColumnIndex(COLUMN_CATEGORY)),
                    res.getString(res.getColumnIndex(COLUMN_SERVED_ON)),
                    res.getInt(res.getColumnIndex(COLUMN_IS_LIKED)),
                    res.getInt(res.getColumnIndex(COLUMN_IS_FAVOURITE)),
                    res.getInt(res.getColumnIndex(COLUMN_LIKE_COUNT)),
                    res.getLong(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getLong(res.getColumnIndex(COLUMN_UNIQUE_ID))
            );
            array_list.add(menu);
            res.moveToNext();
        }
        res.close();

        return array_list;
    }

    public int getCount(Menu review){
        SQLiteDatabase db = this.getReadableDatabase();
        int count;

        Cursor res = db.rawQuery("SELECT COUNT(*) AS RECORDS FROM MENU WHERE NAME = '" + review.getName() + "'" +
                " AND SENDER= '" + review.getSender()
                + "' AND TIMESTAMP=" + review.getTs() + " AND UNIQUE_ID=" + review.getUniqueId() + ";", null);


        res.moveToFirst();

        count = res.getInt(0);
        res.close();
        if (count == 1) {
            db.close();
            return -1;
        } else {
            res = db.rawQuery("SELECT LIKE_COUNT AS RECORDS FROM MENU WHERE NAME = '" + review.getName() + "'" +
                    " AND SENDER= '" + review.getSender()
                    + "' AND TIMESTAMP=" + review.getTs() + " AND UNIQUE_ID=" + review.getUniqueId() + ";", null);
            res.moveToFirst();

            count = res.getInt(0);
            res.close();
            db.close();
            return count;
        }
    }
}