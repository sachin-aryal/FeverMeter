package app.fevermeter.org.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.fevermeter.org.Helper.HelperService;
import app.fevermeter.org.Model.Fever;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by iam on 9/16/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "feverMeter";

    // fever table name
    public static final String TABLE_FEVER = "fever";

    // fever Table Columns names
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_FEVER_DATE = "feverDate";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FEVER_TABLE = "CREATE TABLE " + TABLE_FEVER + "("
                + KEY_TEMPERATURE + " INTEGER," + KEY_FEVER_DATE + " INTEGER" + ")";
        db.execSQL(CREATE_FEVER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEVER);

        // Create tables again
        onCreate(db);

    }


    public void addFever(Fever fever) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE,fever.getTemperature());
        values.put(KEY_FEVER_DATE,fever.getFeverDate());
        db.insert(TABLE_FEVER,null,values);

    }



    public List<Fever> getData(String startDate,String endDate){

        String query = HelperService.buildDbQuery(startDate,endDate)+" ORDER BY "+KEY_FEVER_DATE+" DESC";

        List<Fever> fevers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Fever fever = new Fever();
                fever.setTemperature(Double.parseDouble(cursor.getString(0)));
                fever.setFeverDate(Long.parseLong(cursor.getString(1)));
                fevers.add(fever);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fevers;
    }

    public List<Fever> getLimitData(){

        List<Fever> fevers = new ArrayList<>();
        String fetchQuery = "SELECT *FROM "+TABLE_FEVER+" ORDER BY "+KEY_FEVER_DATE+" DESC LIMIT 10";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(fetchQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Fever fever = new Fever();
                fever.setTemperature(Double.parseDouble(cursor.getString(0)));
                fever.setFeverDate(Long.parseLong(cursor.getString(1)));
                fevers.add(fever);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fevers;
    }




}
