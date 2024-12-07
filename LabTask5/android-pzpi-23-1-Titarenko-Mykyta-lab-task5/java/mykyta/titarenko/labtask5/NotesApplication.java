package mykyta.titarenko.labtask5;

import android.app.Application;
import mykyta.titarenko.labtask5.Notes;

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



