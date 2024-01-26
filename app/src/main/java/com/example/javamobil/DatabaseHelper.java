package com.example.javamobil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static String DATABASE_NAME = "todo.db";
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_TABLE_NAME = "todolist";
    private static String COLUMN_ID = "id";
    private static String COLUMN_NOTE = "notes";
    private static String COLUMN_SOUND = "sound";
    private static String COLUMN_DATE = "date";
    private static String COLUMN_ALARM_DATE = "alarmDate";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + DATABASE_TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NOTE + " TEXT, " +
                        COLUMN_SOUND + " TEXT, " +
                        COLUMN_DATE + " DATETIME, " +
                        COLUMN_ALARM_DATE + " DATETIME);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }

    void addToDo(String note, String sound, String date, String time, String notTime) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        LocalDateTime localDateTime=null;
        DateTimeFormatter dateTimeFormatter=  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String date1 = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2) + " " + time.substring(0, 2) + ":" + time.substring(3, 5);
        localDateTime=LocalDateTime.parse(date1,dateTimeFormatter);
        localDateTime.minusMinutes(Long.parseLong(notTime.substring(0,2)));
        cv.put(COLUMN_NOTE, note);
        cv.put(COLUMN_SOUND, sound);
        cv.put(COLUMN_DATE, date1);
        cv.put(COLUMN_ALARM_DATE, String.valueOf(localDateTime));
        long result = db.insert(DATABASE_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "başarılı", Toast.LENGTH_SHORT).show();
        }

    }
    Cursor readAllData(){
        String query = "SELECT * FROM " +DATABASE_TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(DATABASE_TABLE_NAME, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Hata silinemedi.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "silme işlemi başarılı.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DATABASE_TABLE_NAME);
    }
}
