package diy.net.menzap.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import diy.net.menzap.model.Event;

public class EventDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MENZAP";
    private static final String TABLE_NAME = "EVENT";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_LOCATION = "LOCATION";
    private static final String COLUMN_FROM_DATE = "FROM_DATE";
    private static final String COLUMN_TO_DATE = "TO_DATE";

    public EventDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE EVENT (ID INTEGER PRIMARY KEY, NAME TEXT, DESCRIPTION TEXT, LOCATION TEXT, FROM_DATE TEXT, TO_DATE TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EVENT");
        onCreate(db);
    }

    public boolean insert(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(COLUMN_LOCATION, event.getLocation());
        contentValues.put(COLUMN_FROM_DATE, event.getFromDate());
        contentValues.put(COLUMN_TO_DATE, event.getToDate());

        db.insert("EVENT", null, contentValues);

        return true;
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM EVENT WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(int id, String eventName, String eventDesc, String location, String fromDate, String toDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, eventName);
        contentValues.put(COLUMN_DESCRIPTION, eventDesc);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_FROM_DATE, fromDate);
        contentValues.put(COLUMN_TO_DATE, toDate);

        db.update("EVENT", contentValues, "ID = ? ", new String[]{Integer.toString(id)});

        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("EVENT",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAll() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM EVENT", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            array_list.add(res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)));
            array_list.add(res.getString(res.getColumnIndex(COLUMN_LOCATION)));
            array_list.add(res.getString(res.getColumnIndex(COLUMN_FROM_DATE)));
            array_list.add(res.getString(res.getColumnIndex(COLUMN_TO_DATE)));
            res.moveToNext();
        }
        res.close();

        return array_list;
    }
}