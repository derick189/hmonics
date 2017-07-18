package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataManager implements Serializable {
    public static ArrayList<Teacher> teachers;
    private static ArrayList<String> wordIdList;
    private static ArrayList<String> hmongWordList;
    private static HashMap<String, Word> wordIdToWord;

    public static void populate() {
        teachers = new ArrayList<Teacher>();
        for (int i = 0; i < 2; i++) {
            addTeacher(new Teacher("Teacher " + i));
        }
        for (int i = 0; i < 10; i++) {
            Student student;
            addStudent(0, student = new Student("Student " + i));
            History history;
            student.startNewCurrentHistory(history = new History("Spelling Game"));
            history.addWord("bird");
            history.addWord("bird");
            addStudent(1, new Student("Student " + i));
        }

        wordIdList = new ArrayList<String>();
        wordIdList.addAll(Arrays.asList("apple", "bird", "cherry", "gem", "money", "pear"));

        hmongWordList = new ArrayList<String>();
        hmongWordList.addAll(Arrays.asList("Kua", "noog", "lws suav", "lub pov haum", "nyiaj", "txiv moj coos"));

        // Key: english spelling, Value: Word object
        wordIdToWord = new HashMap<String, Word>();
        for (String wordId : wordIdList) {
            wordIdToWord.put(wordId, new Word(wordId, hmongWordList.get(wordIdList.indexOf(wordId))));
        }
    }

    public static ArrayList<String> getWordIdList() {
        return wordIdList;
    }

    public static Word getWord(String wordId) {
        return wordIdToWord.get(wordId);
    }

    public static ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public static ArrayList<Student> getStudents(int teacherIndex) {
        return teachers.get(teacherIndex).getStudents();
    }

    public static void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public static void removeTeacher(int teacherIndex) {
        teachers.remove(teacherIndex);
    }

    public static void addStudent(int teacherIndex, Student student) {
        teachers.get(teacherIndex).getStudents().add(student);
    }

    public static void removeStudent(int teacherIndex, int studentIndex) {
        teachers.get(teacherIndex).getStudents().remove(studentIndex);
    }

    public static ArrayList<History> getHistory(Student student) {
        return student.getGameHistory();
    }

}
