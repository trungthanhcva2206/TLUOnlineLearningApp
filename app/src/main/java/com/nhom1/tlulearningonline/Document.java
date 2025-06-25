package com.nhom1.tlulearningonline;

public class Document {
    private String id;
    private String name;
    private String url;
    private String lessonId;

    public Document(String id, String name, String url, String lessonId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lessonId = lessonId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
}

