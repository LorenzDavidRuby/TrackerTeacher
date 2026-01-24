package com.example.trackerteacher;

public class Item {
    String Name;
    String course;
    int image;
    String lastUpdateOfStatus;
    String status;
    String room;

    // constructor
    public Item(String Name, String course, int image, String lastUpdateOfStatus, String status, String room) {
        this.Name = Name;
        this.course = course;
        this.image = image;
        this.lastUpdateOfStatus = lastUpdateOfStatus;
        this.status = status;
        this.room = room;
    }

    // getter setter
    public String getName() {
        return Name;
    }

    public String getCourse() {
        return course;
    }

    public int getImage() {
        return image;
    }

    public String getLastUpdateOfStatus() {
        return lastUpdateOfStatus;
    }

    public String getStatus() {
        return status;
    }

    public String getRoom() {
        return room;
    }
}
