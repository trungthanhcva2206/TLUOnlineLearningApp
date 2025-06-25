package com.nhom1.tlulearningonline;

public class CourseItemGV {
    String title;
    String description;
    int lessonCount;

    public CourseItemGV(String title, String description, int lessonCount) {
        this.title = title;
        this.description = description;
        this.lessonCount = lessonCount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLessonCount() {
        return lessonCount;
    }
}