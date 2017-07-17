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

public class ScreenManager {
    private static GdxGame game;
    private static int selectedTeacherIndex;
    private static String selectedTeacherName;
    private static int selectedStudentIndex;
    private static String selectedStudentName;

    public static void start(GdxGame gdxGame, Screen firstScreen) {
        ScreenManager.game = gdxGame;
        game.setScreen(firstScreen);
    }

    public static void nextScreen(Screen next) {
        game.setScreen(next);
        // destroy screens
    }

    public static Table getNewSelectionTable(SelectionType selectionType, ChangeListener doOnSelectName, boolean changeVersion, String text) {
        int totalWidth = 1200;
        int nameWidth = 800;
        int buttonWidth = 200;
        int rowHeight = 75;
        int columnSeparator = 100;
        int rowSeparator = 25;

        Table selectionTable = new Table();
        switch (selectionType) {
            case TEACHERS:
            case STUDENTS:
                Table aDataRow = new Table();
                final TextField addItemField = new TextField(text, AssetManager.buttonSkin);
                VerticalGroup verticalGroup = new VerticalGroup();
                ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.buttonSkin);
                scrollPane.setScrollingDisabled(true, false);
                scrollPane.setFadeScrollBars(false);

                selectionTable.add().width(totalWidth).row();

                // first row for adding an item
                if (changeVersion) {
                    TextButton addButton = new TextButton("ADD", AssetManager.buttonSkin);
                    switch (selectionType) {
                        case TEACHERS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    System.out.println("add teacher: " + addItemField.getText()); // TODO
                                }
                            });
                            break;
                        case STUDENTS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    System.out.println("add student: " + addItemField.getText()); // TODO
                                }
                            });
                            break;
                    }
                    aDataRow.add(addItemField).width(nameWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(addButton).width(buttonWidth).height(rowHeight);
                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);

                    selectionTable.add(aDataRow);
                    selectionTable.row();
                } else {
                    selectionTable.add().height(rowHeight + rowSeparator);
                    selectionTable.row();
                }

                // rest of rows of items
                switch (selectionType) {
                    case TEACHERS:
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
                                TextButton deleteButton = new TextButton("DELETE", AssetManager.buttonSkin);
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        System.out.println("delete: " + teachers.get(Integer.parseInt(actor.getName()))); //TODO
                                    }
                                });
                                aDataRow.add().width(columnSeparator);
                                aDataRow.add(deleteButton).width(buttonWidth).height(rowHeight);
                            } else {
                                aDataRow.add().width(columnSeparator + buttonWidth);
                            }

                            aDataRow.row();
                            aDataRow.add().height(rowSeparator);

                            verticalGroup.addActor(aDataRow);
                        }
                        break;
                    case STUDENTS:
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
                                TextButton deleteButton = new TextButton("DELETE", AssetManager.buttonSkin);
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        System.out.println(actor.toString());
                                        System.out.println("delete: " + DataManager.getTeachers().get(selectedTeacherIndex).getStudents().get(Integer.parseInt(actor.getName()))); //TODO
                                    }
                                });
                                aDataRow.add().width(columnSeparator);
                                aDataRow.add(deleteButton).width(buttonWidth).height(rowHeight);
                            } else {
                                aDataRow.add().width(columnSeparator + buttonWidth);
                            }

                            aDataRow.row();
                            aDataRow.add().height(rowSeparator);

                            verticalGroup.addActor(aDataRow);
                        }
                        break;
                }
                selectionTable.add(scrollPane).width(totalWidth).height((rowHeight + columnSeparator) * 5);
                break;
            case HISTORIES:
                break;
            case GAMES:
                break;
        }
        return selectionTable;
    }

    public enum SelectionType {
        TEACHERS, STUDENTS, HISTORIES, GAMES
    }
}
