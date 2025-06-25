package com.nhom1.tlulearningonline;

public class Lesson {
    private String id;
    private String title;
    private String content;
    private String courseId;

    public Lesson(String id, String title, String content, String courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}


