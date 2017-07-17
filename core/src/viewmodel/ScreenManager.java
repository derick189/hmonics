package viewmodel;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import model.DataManager;
import model.Student;
import model.Teacher;
import view.AssetManager;
import view.GdxGame;

import java.util.ArrayList;
import java.util.Stack;

public class ScreenManager {
    public static GdxGame game;
    public static Stack<Screen> previousScreens = new Stack<Screen>();
    static int selectedTeacherIndex;
    static String selectedTeacherName;
    static int selectedStudentIndex;
    static String selectedStudentName;

    public static void start(GdxGame gdxGame, Screen firstScreen) {
        ScreenManager.game = gdxGame;
        game.setScreen(firstScreen);
    }

    public static void nextScreen(Screen next) {
        game.setScreen(next);
    }

    public static void previousScreen() {
        game.setScreen(previousScreens.pop());
    }

    public static Table getNewSelectionTable(DataType dataType, ChangeListener doOnSelectName, boolean changeVersion, String text) {
        int totalWidth = 1200;
        int nameWidth = 800;
        int buttonWidth = 200;
        int rowHeight = 75;
        int columnSeparator = 100;
        int rowSeparator = 25;

        Table selectionTable = new Table();
        Table aDataRow = new Table();
        ImageButton addButton = new ImageButton(AssetManager.buttonSkin);
        final TextField addItemField = new TextField(text, AssetManager.buttonSkin);
        VerticalGroup verticalGroup = new VerticalGroup();
        ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.buttonSkin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        selectionTable.add().width(totalWidth).row();


        switch (dataType) {
            case TEACHERS:
                // first row for adding an item of this type
                aDataRow.add(addItemField).width(nameWidth).height(rowHeight);
                aDataRow.add().width(columnSeparator);
                aDataRow.add(addButton).width(buttonWidth).height(rowHeight);
                aDataRow.row();
                aDataRow.add().height(rowSeparator);

                selectionTable.add(aDataRow);
                selectionTable.row();

                addButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.out.println("add: " + addItemField.getMessageText()); // TODO
                    }
                });

                // rest of rows
                final ArrayList<Teacher> teachers = DataManager.getTeachers();
                for (int i = 0; i < teachers.size(); i++) {
                    aDataRow = new Table();
                    aDataRow.setName(String.valueOf(i));
                    TextButton aName = new TextButton(teachers.get(i).getName(), AssetManager.buttonSkin);
                    aName.setName(String.valueOf(i));
                    aName.addListener(doOnSelectName);
                    aName.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            selectedTeacherIndex = Integer.parseInt(actor.getName());
                            selectedTeacherName = DataManager.getTeachers().get(selectedTeacherIndex).getName();
                            System.out.println("pressed: " + DataManager.getTeachers().get(selectedTeacherIndex).toString());
                        }
                    });
                    aDataRow.add(aName).width(nameWidth).height(rowHeight);

                    if (changeVersion) {
                        ImageButton delete = new ImageButton(AssetManager.buttonSkin);
                        delete.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                System.out.println("delete: " + teachers.get(Integer.parseInt(actor.getName()))); //TODO
                            }
                        });
                        aDataRow.add().width(columnSeparator);
                        aDataRow.add(delete).width(buttonWidth).height(rowHeight);
                    } else {
                        aDataRow.add().width(columnSeparator + buttonWidth);
                    }

                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);

                    verticalGroup.addActor(aDataRow);
                }
                break;
            case STUDENTS:
                // first row for adding an item of this type
                aDataRow.add(addItemField).width(nameWidth).height(rowHeight);
                aDataRow.add().width(columnSeparator);
                aDataRow.add(addButton).width(buttonWidth).height(rowHeight);
                aDataRow.row();
                aDataRow.add().height(rowSeparator);

                selectionTable.add(aDataRow);
                selectionTable.row();

                addButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.out.println("add: " + addItemField.getMessageText()); // TODO
                    }
                });

                // rest of rows
                final ArrayList<Student> students = DataManager.getStudents(DataManager.getTeachers().get(selectedTeacherIndex));
                for (int i = 0; i < students.size(); i++) {
                    aDataRow = new Table();
                    aDataRow.setName(String.valueOf(i));
                    TextButton aName = new TextButton(students.get(i).getName(), AssetManager.buttonSkin);
                    aName.setName(String.valueOf(i));
                    aName.addListener(doOnSelectName);
                    aName.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            selectedStudentIndex = Integer.parseInt(actor.getName());
                            selectedStudentName = DataManager.getStudents(DataManager.getTeachers().get(selectedTeacherIndex)).get(selectedStudentIndex).getName();
                            System.out.println("pressed: " + DataManager.getStudents(DataManager.getTeachers().get(selectedTeacherIndex)).get(selectedStudentIndex).toString());
                        }
                    });
                    aDataRow.add(aName).width(nameWidth).height(rowHeight);

                    if (changeVersion) {
                        ImageButton delete = new ImageButton(AssetManager.buttonSkin);
                        delete.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                System.out.println(actor.toString());
                                System.out.println("delete: " + DataManager.getTeachers().get(selectedTeacherIndex).getStudents().get(Integer.parseInt(actor.getName()))); //TODO
                            }
                        });
                        aDataRow.add().width(columnSeparator);
                        aDataRow.add(delete).width(buttonWidth).height(rowHeight);
                    } else {
                        aDataRow.add().width(columnSeparator + buttonWidth);
                    }

                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);

                    verticalGroup.addActor(aDataRow);
                }
                break;
            case HISTORIES:
                break;
            case GAMES:
                break;
        }
        selectionTable.add(scrollPane).width(totalWidth).height((rowHeight + columnSeparator) * 5);
        selectionTable.debug();
        return selectionTable;
    }

    public enum DataType {
        TEACHERS, STUDENTS, HISTORIES, GAMES
    }
}
