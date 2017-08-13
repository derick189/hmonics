package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Utility class that persists data and handles Teachers, Students, and Words storage and population.
 * @authors Derick Lenvik, Jared Johnson
 */
public class DataManager {
    private static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    private static ArrayList<Word> wordList = new ArrayList<Word>();
    private static Preferences prefs= Gdx.app.getPreferences("My Preferences");

    private DataManager() {} // Constructor prevents outside instantiation

    public static void populate() {
        load();
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
//        addTeacher(new Teacher("Teacher"));
//        Student student = new Student("Student");
//        addStudent(0, student);
//        History history = new History("Spelling Game");
//        student.startNewCurrentHistory(history);
//        history.addWord("cat");
//        history.addWord("bird");
//        history.addWord("dog");
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
        save();
    }

    public static void removeTeacher(int teacherIndex) {
        teachers.remove(teacherIndex);
        save();
    }

    public static void addStudent(int teacherIndex, Student student) {
        teachers.get(teacherIndex).getStudents().add(student);
        save();
    }

    public static void removeStudent(int teacherIndex, int studentIndex) {
        teachers.get(teacherIndex).getStudents().remove(studentIndex);
        save();
    }
    /**
     * Persist data
     */
    public static void save() {
        String json = new Json().toJson(teachers);
        prefs.putString("teachers", json);
        // bulk update the preferences
        prefs.flush();
    }
    private static void load() { teachers = new Json().fromJson(ArrayList.class, prefs.getString("teachers")); }
}
