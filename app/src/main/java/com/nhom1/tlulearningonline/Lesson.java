package com.nhom1.tlulearningonline;

public class Lesson {
    private String title;
    private String duration;

    public Lesson(String title, String duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }
}

