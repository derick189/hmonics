package model;

import java.util.ArrayList;

/**
 * Every Teacher has a list of Students with their associated game history
 * @authors Derick Lenvik, Jared Johnson
 */
public class Teacher {
    private String name;
    private ArrayList<Student> students;

    public Teacher(String name) {
        this.name = name;
        students = new ArrayList<Student>();
    }
    public Teacher(){} // Constructor for DataManager.load() --> data persist

    public String getName() {
        return name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
