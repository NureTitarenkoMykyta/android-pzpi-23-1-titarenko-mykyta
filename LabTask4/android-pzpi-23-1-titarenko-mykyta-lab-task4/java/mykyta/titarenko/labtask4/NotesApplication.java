package mykyta.titarenko.labtask4;

import android.app.Application;
import mykyta.titarenko.labtask4.Notes;

public class NotesApplication extends Application {
    private Notes notes;

    @Override
    public void onCreate() {
        super.onCreate();
        notes = new Notes(this);
    }

    public Notes GetNotes() {
        return notes;
    }

}



