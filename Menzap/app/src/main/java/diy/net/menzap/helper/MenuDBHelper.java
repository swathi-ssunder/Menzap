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
                        COLUMN_SENDER + " INTEGER, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_SERVED_ON + " TEXT, " +
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
        contentValues.put(COLUMN_TIME_STAMP, menu.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, menu.getUniqueId());

        db.insert("MENU", null, contentValues);

        return true;
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM MENU WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(int id, Menu menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, menu.getSender());
        contentValues.put(COLUMN_NAME, menu.getName());
        contentValues.put(COLUMN_DESCRIPTION, menu.getDescription());
        contentValues.put(COLUMN_CATEGORY, menu.getCategory());
        contentValues.put(COLUMN_SERVED_ON, menu.getServedOn());
        contentValues.put(COLUMN_TIME_STAMP, menu.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, menu.getUniqueId());

        db.update("MENU", contentValues, "ID = ? ", new String[]{Integer.toString(id)});

        return true;
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
        Cursor res = db.rawQuery("SELECT * FROM MENU", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Menu menu = new Menu(
                    res.getInt(res.getColumnIndex(COLUMN_SENDER)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)),
                    res.getInt(res.getColumnIndex(COLUMN_CATEGORY)),
                    res.getString(res.getColumnIndex(COLUMN_SERVED_ON)),
                    res.getInt(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getInt(res.getColumnIndex(COLUMN_UNIQUE_ID))
            );
            array_list.add(menu);
            res.moveToNext();
        }
        res.close();

        return array_list;
    }
}