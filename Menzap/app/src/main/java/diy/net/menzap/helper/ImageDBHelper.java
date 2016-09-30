package diy.net.menzap.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import diy.net.menzap.model.Image;

/**
 * Created by vivek-sethia on 28.09.16.
 */
public class ImageDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MENZAP";
    private static final String TABLE_NAME = "IMAGE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_SENDER = "SENDER";
    private static final String COLUMN_TIME_STAMP = "TIMESTAMP";
    private static final String COLUMN_UNIQUE_ID = "UNIQUE_ID";

    public ImageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SENDER + " TEXT, " +
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
        db.execSQL("DROP TABLE IF EXISTS IMAGE");
        onCreate(db);
    }

    public boolean insert(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, image.getSender());
        contentValues.put(COLUMN_TIME_STAMP, image.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, image.getUniqueId());

        long result = db.insert("IMAGE", null, contentValues);
        db.close();
        return (result != -1);
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM IMAGE WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(int id, Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, image.getSender());
        contentValues.put(COLUMN_TIME_STAMP, image.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, image.getUniqueId());

        db.update("IMAGE", contentValues, "ID = ? ", new String[]{Integer.toString(id)});

        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("IMAGE",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Image> getAll() {
        ArrayList<Image> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM IMAGE", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Image image = new Image(
                    res.getString(res.getColumnIndex(COLUMN_SENDER)),
                    res.getLong(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getLong(res.getColumnIndex(COLUMN_UNIQUE_ID))
            );
            array_list.add(image);
            res.moveToNext();
        }
        res.close();

        return array_list;
    }
}