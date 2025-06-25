package com.nhom1.tlulearningonline;

/**
 * Data model for a course item.
 * Represents a course including title, instructor, department, number of lessons and course ID.
 */
public class CourseItem {
    private String id;
    private String title;
    private String instructor;
    private String department;
    private int soBaiHoc;
    private String des;

    /**
     * Constructs a new CourseItem with full parameters.
     *
     * @param id          The unique ID of the course (used for detail navigation).
     * @param title       The title of the course.
     * @param instructor  The instructor's name.
     * @param department  The department name.
     * @param soBaiHoc    Number of lessons in the course.
     */
    public CourseItem(String id, String title, String instructor, String department,String des, int soBaiHoc) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.department = department;
        this.des = des;
        this.soBaiHoc = soBaiHoc;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDepartment() {
        return department;
    }

    public int getSoBaiHoc() {
        return soBaiHoc;
    }
}
