package com.nhom1.tlulearningonline;

/**
 * Data model for a teacher.
 * Represents information about a teacher, including their name and avatar.
 */
public class TeacherItem {
    private String name;
    private String avatarResId;
    /**
     * Constructs a new TeacherItem.
     *
     * @param name        The name of the teacher.
     * @param avatarResId The resource ID of the teacher's avatar drawable.
     */
    public TeacherItem(String name, String avatarResId) {
        this.name = name;
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public String getAvatarResId() {
        return avatarResId;
    }
}
