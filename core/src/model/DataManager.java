package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataManager {
    static ArrayList<Teacher> test;
    private static ArrayList<String> wordIdList;
    private static ArrayList<String> hmongWordList;
    private static HashMap<String, Word> wordIdToWord;

    public static void populate() {
        wordIdList = new ArrayList<String>();
        wordIdList.addAll(Arrays.asList("apple", "bird", "cherry", "gem", "money", "pear"));

        hmongWordList = new ArrayList<String>();
        hmongWordList.addAll(Arrays.asList("Kua", "noog", "lws suav", "lub pov haum", "nyiaj", "txiv moj coos"));

        // Key: english spelling, Value: Word object
        wordIdToWord = new HashMap<String, Word>();
        for (String wordId : wordIdList) {
                wordIdToWord.put(wordId, new Word(wordId, hmongWordList.get(wordIdList.indexOf(wordId))));
        }

        test = new ArrayList<Teacher>();
        for (int i = 0; i < 10; i++) {
            Teacher teacher = new Teacher("T" + i);
            test.add(teacher);
            for (int j = 0; j < 30; j++) {
                Student student = new Student("S" + i + j);
                teacher.addStudent(student);
            }
        }
    }

    public static ArrayList<String> getWordIdList() {
        return wordIdList;
    }

    public static Word getWord(String wordId) {
        return wordIdToWord.get(wordId);
    }

    public static ArrayList<Teacher> getTeachers() {
        return test;
    }

    public static ArrayList<Student> getStudents(Teacher teacher) {
        return teacher.getStudents();
    }

    public static ArrayList getHistories(Student student) {
        return student.getGameHistory();
    }
}
