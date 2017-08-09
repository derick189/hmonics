package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Handles Teachers, Students, and Words storage.
 */
public class DataManager {
    private static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    private static ArrayList<Word> wordList = new ArrayList<Word>();

    public static void populate() {
        wordList.add(new Word("apple", "kua", 2));
        wordList.add(new Word("money", "nyiaj", 3));
        wordList.add(new Word("bird", "noog", 3));
        wordList.add(new Word("pig", "npua", 2));
        wordList.add(new Word("dog", "aub", 2));
        wordList.add(new Word("boat", "nkoj", 3));
        wordList.add(new Word("fish", "ntses", 3));
        wordList.add(new Word("deer", "mos lwj", 7));
        wordList.add(new Word("cat", "miv", 3));
        wordList.add(new Word("horse", "nees", 3));
        wordList.add(new Word("flower", "paj", 3));
        wordList.add(new Word("frog", "qav", 3));
        wordList.add(new Word("pumpkin", "taub dag", 7));
        wordList.add(new Word("sheep", "yaj", 3));
        wordList.add(new Word("dragon", "zaj", 3));

        // TESTING
//        addTeacher(new Teacher("Mrs. Anderson"));
//        addTeacher(new Teacher("Mr. Johnson"));
//        addTeacher(new Teacher("Mrs. Smith"));
//
//        addStudent(1, new Student("Jared"));
//        addStudent(1, new Student("Derick"));
//        addStudent(1, new Student("Philip"));
//
//        for (int i = 0; i < 9; i++) {
//            Student student;
//            addStudent(0, student = new Student("Student " + (i + 1)));
//            History history;
//            student.startNewCurrentHistory(history = new History("Spelling Game"));
//            history.addWord("bird");
//            history.addWord("cat");
//            history.addWord("dog");
//        }
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
