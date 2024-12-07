package mykyta.titarenko.labtask5;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import mykyta.titarenko.labtask5.DBHelper;
import mykyta.titarenko.labtask5.Note;

public class Notes {
    private DBHelper db;
    private List<Note> notes;
    public int lastNoteNumber = 0;

    public Notes(Application application){
        db = new DBHelper(application, "notes", null, 1); // Initialize DBHelper with proper context
        notes = new ArrayList<>();
    }

    public DBHelper getDBHelper(){
        return db;
    }

    public void updateNotes(){
        Cursor cursor = db.onSelect();
        CreateNotes(cursor);
        if (notes.isEmpty()){
            lastNoteNumber = -1;
        } else {
            lastNoteNumber = notes.get(notes.size() - 1).getNumber();
        }
    }

    public void AddNote(String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        db.onInsert(title, description, importance, eventDate, eventTime, creationDate, imageData);
    }

    public void EditNote(int number, String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        db.onUpdate(number, title, description, importance, eventDate, eventTime, creationDate, imageData);
    }

    public void DeleteNote(int index, int number){
        db.onDelete(number);
        notes.remove(index);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Note> Filter(String title, String description, boolean importance, int importanceLayer){
        Cursor cursor = db.Filter(title, description, importance, importanceLayer);
        return CreateNotes(cursor);
    }

    public List<Note> CreateNotes(Cursor cursor){
        notes.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") int number = cursor.getInt(cursor.getColumnIndex("number"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") int importance = cursor.getInt(cursor.getColumnIndex("importance"));
            @SuppressLint("Range") String eventDate = cursor.getString(cursor.getColumnIndex("event_date"));
            @SuppressLint("Range") String eventTime = cursor.getString(cursor.getColumnIndex("event_time"));
            @SuppressLint("Range") String creationDate = cursor.getString(cursor.getColumnIndex("creation_date"));
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image_data"));
            notes.add(new Note(number, title, description, importance, eventDate, eventTime, creationDate, imageBytes));
        }
        cursor.close();
        return notes;
    }
}
