package mykyta.titarenko.labtask4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes(number INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT," +
                " importance INTEGER, event_date TEXT, event_time TEXT, creation_date TEXT, image_data BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Drop(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE notes");
        onCreate(db);
    }

    public void onInsert(String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = CreateNote(title, description, importance, eventDate, eventTime, creationDate, imageData);
        db.insert("notes", null, contentValues);
    }

    public void onUpdate(int number, String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = CreateNote(title, description, importance, eventDate, eventTime, creationDate, imageData);
        db.update("notes", contentValues, "number = ?", new String[]{String.valueOf(number)});
    }

    public ContentValues CreateNote(String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("importance", importance);
        contentValues.put("event_date", eventDate);
        contentValues.put("event_time", eventTime);
        contentValues.put("creation_date", creationDate);
        contentValues.put("image_data", imageData);
        return contentValues;
    }

    public void onDelete(int number){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("notes", "number = ?", new String[]{String.valueOf(number)});
    }

    public Cursor onSelect(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("notes", null, null, null, null, null, null);
    }

    public Cursor Filter(String title, String description, boolean importance, int importanceLayer){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notes WHERE (title LIKE ? OR description LIKE ?)";
        String[] selectionArgs = new String[]{"%" + title + "%", "%" + description + "%"};
        if (importance){
            query += "AND importance = ?";
            selectionArgs = new String[]{"%" + title + "%", "%" + description + "%", String.valueOf(importanceLayer)};
        }
        return db.rawQuery(query, selectionArgs);
    }
}
