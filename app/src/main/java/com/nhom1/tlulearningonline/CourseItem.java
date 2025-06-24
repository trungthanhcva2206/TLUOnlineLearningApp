package com.nhom1.tlulearningonline; // Đảm bảo khớp với package của bạn

/**
 * Data model for a course item.
 * Represents information about a single course displayed in the application,
 * particularly for lists like "Featured Courses".
 */
public class CourseItem {
    private String title;
    private String instructor;
    private String department;
    private int progress; // Course completion progress (e.g., 0-100)
    private boolean isStarred; // Indicates if the course is starred/saved (for the star icon)
    private int soBaiHoc; // Đã THÊM: Số lượng bài học trong khóa học

    /**
     * Constructs a new CourseItem.
     *
     * @param title      The title of the course.
     * @param instructor The name of the instructor for the course.
     * @param department The department or field of the course.
     * @param progress   The progress percentage of the course (e.g., 0-100).
     * @param isStarred  True if the course is starred/highlighted, false otherwise.
     * @param soBaiHoc   The number of lessons in the course. (Đã THÊM tham số này)
     */
    public CourseItem(String title, String instructor, String department, int progress, boolean isStarred, int soBaiHoc) {
        this.title = title;
        this.instructor = instructor;
        this.department = department;
        this.progress = progress;
        this.isStarred = isStarred;
        this.soBaiHoc = soBaiHoc; // Khởi tạo trường mới
    }

    /**
     * Overloaded constructor for compatibility with older calls (assigns default soBaiHoc).
     *
     * @param title      The title of the course.
     * @param instructor The name of the instructor for the course.
     * @param department The department or field of the course.
     * @param progress   The progress percentage of the course (e.g., 0-100).
     * @param isStarred  True if the course is starred/highlighted, false otherwise.
     */
    public CourseItem(String title, String instructor, String department, int progress, boolean isStarred) {
        this(title, instructor, department, progress, isStarred, 0); // Mặc định soBaiHoc là 0 cho constructor cũ
    }


    /**
     * Returns the title of the course.
     * @return The course title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the instructor's name.
     * @return The instructor's name.
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Returns the department of the course.
     * @return The course's department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the progress percentage of the course.
     * @return The progress as an integer (0-100).
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Checks if the course is starred/highlighted.
     * @return True if starred, false otherwise.
     */
    public boolean isStarred() {
        return isStarred;
    }

    /**
     * Sets the starred status of the course.
     * @param starred The new starred status.
     */
    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    /**
     * Returns the number of lessons in the course. (Đã THÊM getter này)
     * @return The number of lessons.
     */
    public int getSoBaiHoc() {
        return soBaiHoc;
    }
}
