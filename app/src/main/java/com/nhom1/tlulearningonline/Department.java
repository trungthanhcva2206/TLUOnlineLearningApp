package com.nhom1.tlulearningonline;

public class Department {
    private String id;
    private String name;

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // getter
    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}

