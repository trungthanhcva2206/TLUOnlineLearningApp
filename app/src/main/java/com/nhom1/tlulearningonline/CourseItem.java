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

    /**
     * Constructs a new CourseItem.
     *
     * @param title      The title of the course.
     * @param instructor The name of the instructor for the course.
     * @param department The department or field of the course.
     * @param progress   The progress percentage of the course (e.g., 80 for 80%).
     * @param isStarred  True if the course is starred/highlighted, false otherwise.
     */
    public CourseItem(String title, String instructor, String department, int progress, boolean isStarred) {
        this.title = title;
        this.instructor = instructor;
        this.department = department;
        this.progress = progress;
        this.isStarred = isStarred;
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
}
