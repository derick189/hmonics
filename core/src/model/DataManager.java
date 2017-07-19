package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {
    private static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    private static ArrayList<Word> wordList = new ArrayList<Word>();

    public static void populate() {
        wordList.add(new Word("apple", "kua"));
        wordList.add(new Word("money", "nyiaj"));
        wordList.add(new Word("bird", "noog"));
        wordList.add(new Word("pig", "bua"));
        wordList.add(new Word("dog", "dev"));
        wordList.add(new Word("boat", "goj"));
        wordList.add(new Word("fish", "jes"));
        wordList.add(new Word("deer", "kauv"));
        wordList.add(new Word("cat", "miv"));
        wordList.add(new Word("horse", "nees"));
        wordList.add(new Word("flower", "paj"));
        wordList.add(new Word("frog", "qav"));
        wordList.add(new Word("pumpkin", "taub"));
        wordList.add(new Word("sheep", "yaj"));
        wordList.add(new Word("dragon", "zaj"));

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
    }

    public static ArrayList<Word> getWordList() {
        return wordList;
    }

    public static Word getWord(String wordId) {
        for (Word word : wordList) {
            if (word.compareTo(wordId) == 0)
                return word;
        }
        return null;
    }

    public static ArrayList<Teacher> getTeachers() {
        Collections.sort(teachers, new Comparator<Teacher>() {
            @Override
            public int compare(Teacher o1, Teacher o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return teachers;
    }

    public static ArrayList<Student> getStudents(int teacherIndex) {
        Collections.sort(teachers.get(teacherIndex).getStudents(), new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return teachers.get(teacherIndex).getStudents();
    }

    public static ArrayList<History> getHistory(Student student) {
        Collections.sort(student.getGameHistory(), new Comparator<History>() {
            @Override
            public int compare(History o1, History o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return student.getGameHistory();
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

}
