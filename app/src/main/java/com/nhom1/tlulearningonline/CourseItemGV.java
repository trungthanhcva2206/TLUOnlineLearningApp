package com.nhom1.tlulearningonline;

public class CourseItemGV {
    private String id;
    String title;
    String description;
    int lessonCount;
    private String teacherName;

    public CourseItemGV(String id, String title, String description, int lessonCount, String teacherName) {
        this.title = title;
        this.description = description;
        this.lessonCount = lessonCount;
        this.teacherName = teacherName;
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getId() {
        return id;
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