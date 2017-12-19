package diy.net.menzap.helper;

/**
 * Created by swathissunder on 27/09/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import diy.net.menzap.model.Tracking;

public class TrackingDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MENZAP";
    private static final String TABLE_NAME = "TRACKING";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_SENDER = "SENDER";
    private static final String COLUMN_USER_NAME = "USER_NAME";
    private static final String COLUMN_URL = "URL";
    private static final String COLUMN_TIME_STAMP = "TIMESTAMP";
    private static final String COLUMN_UNIQUE_ID = "UNIQUE_ID";

    public TrackingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SENDER + " TEXT, " +
                        COLUMN_USER_NAME + " TEXT, " +
                        COLUMN_URL + " TEXT, " +
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
        db.execSQL("DROP TABLE IF EXISTS TRACKING");
        onCreate(db);
    }

    public boolean insert(Tracking tracking) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, tracking.getSender());
        contentValues.put(COLUMN_USER_NAME, tracking.getUserName());
        contentValues.put(COLUMN_URL, tracking.getUrl());
        contentValues.put(COLUMN_TIME_STAMP, tracking.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, tracking.getUniqueId());

        long result = db.insert("TRACKING", null, contentValues);
        db.close();
        return (result != -1);
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM TRACKING WHERE ID=" + id + "", null);
    }

    public int count() {
        SQLiteDatabase db = this.getReadableDatabase();
        return ((int) DatabaseUtils.queryNumEntries(db, TABLE_NAME));
    }

    public boolean update(Tracking tracking) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SENDER, tracking.getSender());
        contentValues.put(COLUMN_USER_NAME, tracking.getUserName());
        contentValues.put(COLUMN_URL, tracking.getUrl());
        contentValues.put(COLUMN_TIME_STAMP, tracking.getTs());
        contentValues.put(COLUMN_UNIQUE_ID, tracking.getUniqueId());

        long result = db.update("TRACKING", contentValues, "SENDER = ? AND UNIQUE_ID = ? AND TIMESTAMP = ?",
                new String[]{tracking.getSender(), Long.toString(tracking.getUniqueId()), Long.toString(tracking.getTs())});
        db.close();

        return (result > 0);
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TRACKING",
                "ID = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Tracking> getAll() {
        ArrayList<Tracking> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM TRACKING", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Tracking tracking = new Tracking(
                    res.getString(res.getColumnIndex(COLUMN_SENDER)),
                    res.getString(res.getColumnIndex(COLUMN_USER_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_URL)),
                    res.getLong(res.getColumnIndex(COLUMN_TIME_STAMP)),
                    res.getLong(res.getColumnIndex(COLUMN_UNIQUE_ID))
            );
            array_list.add(tracking);
            res.moveToNext();
        }
        res.close();
        db.close();

        return array_list;
    }

    public JSONObject getByLocation(long fromTs, long toTs) {
        JSONObject dayData = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT" +
                " COUNT(DISTINCT USER_NAME) AS USER_COUNT," +
                " URL, " +
                " strftime('%j', TIMESTAMP / 1000, 'unixepoch') AS DAY FROM TRACKING" +
                " WHERE TIMESTAMP >= " + Long.toString(fromTs) + " AND TIMESTAMP <= " + Long.toString(toTs) +
                " GROUP BY strftime('%j', TIMESTAMP / 1000, 'unixepoch'), URL ORDER BY URL" , null);

        res.moveToFirst();

        while (!res.isAfterLast()) {
            JSONObject result = new JSONObject();
            try {
                result.put("LOCATION", res.getString(res.getColumnIndex(COLUMN_URL)));
                result.put("USER_COUNT", res.getInt(res.getColumnIndex("USER_COUNT")));

                String day = res.getString(res.getColumnIndex("DAY"));
                if(dayData.has(day)) {
                    ((ArrayList)dayData.get(day)).add(result);
                } else {
                    ArrayList<JSONObject> arrayList = new ArrayList<>();
                    arrayList.add(result);
                    dayData.put(res.getString(res.getColumnIndex("DAY")), arrayList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            res.moveToNext();
        }
        res.close();
        db.close();

        return dayData;
    }
}