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
    private static final String COLUMN_SENDER = "SENDER";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_LOCATION = "LOCATION";
    private static final String COLUMN_FROM_DATE = "FROM_DATE";
    private static final String COLUMN_TO_DATE = "TO_DATE";
    private static final String COLUMN_IS_INTERESTED = "IS_INTERESTED";
    private static final String COLUMN_TIME_STAMP = "TIMESTAMP";
    private static final String COLUMN_UNIQUE_ID = "UNIQUE_ID";

    public EventDBHelper(Context context) {
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
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_FROM_DATE + " TEXT, " +
                    COLUMN_TO_DATE + " TEXT," +
                    COLUMN_IS_INTERESTED+ " INTEGER," +
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
        db.execSQL("DROP TABLE IF EXISTS EVENT");
        onCreate(db);
    }

    public boolean insert(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, event.getSender());
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(COLUMN_LOCATION, event.getLocation());
        contentValues.put(COLUMN_FROM_DATE, event.getFromDate());
        contentValues.put(COLUMN_TO_DATE, event.getToDate());
        contentValues.put(COLUMN_IS_INTERESTED, event.getIsInterested());
        contentValues.put(COLUMN_TIME_STAMP, event.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, event.getUniqueId());

        long result = db.insert("EVENT", null, contentValues);
        db.close();
        return (result != -1);
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM EVENT WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, event.getSender());
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(COLUMN_LOCATION, event.getLocation());
        contentValues.put(COLUMN_FROM_DATE, event.getFromDate());
        contentValues.put(COLUMN_TO_DATE, event.getToDate());
        contentValues.put(COLUMN_IS_INTERESTED, event.getIsInterested());
        contentValues.put(COLUMN_TIME_STAMP, event.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, event.getUniqueId());

        long result = db.update("EVENT", contentValues, "SENDER = ? AND UNIQUE_ID = ? AND TIMESTAMP = ?",
                new String[]{event.getSender(), Long.toString(event.getUniqueId()), Long.toString(event.getTs())});
        db.close();

        return (result > 0);
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("EVENT",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Event> getAll() {
        ArrayList<Event> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM EVENT", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Event event = new Event(
                    res.getString(res.getColumnIndex(COLUMN_SENDER)),
                    res.getString(res.getColumnIndex(COLUMN_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)),
                    res.getString(res.getColumnIndex(COLUMN_LOCATION)),
                    res.getString(res.getColumnIndex(COLUMN_FROM_DATE)),
                    res.getString(res.getColumnIndex(COLUMN_TO_DATE)),
                    res.getInt(res.getColumnIndex(COLUMN_IS_INTERESTED)),
                    res.getLong(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getLong(res.getColumnIndex(COLUMN_UNIQUE_ID))
            );
            array_list.add(event);
            res.moveToNext();
        }
        res.close();

        return array_list;
    }
}