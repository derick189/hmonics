package viewmodel;

import com.badlogic.gdx.Screen;
import model.History;
import model.Student;
import model.Teacher;
import view.GdxGame;

import java.util.ArrayList;
import java.util.Stack;

public class ScreenManager {
    public static GdxGame game;
    public static Stack<Screen> previousScreens = new Stack<Screen>();

    public static void start(GdxGame gdxGame, Screen firstScreen) {
        ScreenManager.game = gdxGame;
        game.setScreen(firstScreen);
    }

    public static void nextScreen(Screen previous, Screen next) {
        previousScreens.push(previous);
        game.setScreen(next);
    }

    public static void previousScreen() {
//        game.setScreen(previousScreens.size() > 1 ? previousScreens.pop() : previousScreens.peek());
        Screen previous = previousScreens.pop();
        game.setScreen(previous);
        previous.resume();
    }

    public static ArrayList<Teacher> formatTeachers() {

        return null;
    }

    public static ArrayList<Student> formatStudents() {

        return null;
    }

    public static ArrayList<History> formatHistories() {

        return null;
    }

}
