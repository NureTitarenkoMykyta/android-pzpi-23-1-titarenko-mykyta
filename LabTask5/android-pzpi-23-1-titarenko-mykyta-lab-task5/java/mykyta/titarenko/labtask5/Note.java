package mykyta.titarenko.labtask5;

import android.net.Uri;

public class Note {

    private int number;

    private String title;

    private String description;

    private int importance;

    private String eventDate;

    private String eventTime;

    private String creationDate;

    private byte[] imageData;

    public Note(int number, String title, String description, int importance, String eventDate, String eventTime, String creationDate, byte[] imageData){
        this.number = number;
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.creationDate = creationDate;
        this.imageData = imageData;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImportance() {
        return importance;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public int getNumber() {
        return number;
    }
}
