package viewmodel;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import model.DataManager;
import model.History;
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

    public static Table getNewSelectionTable(SelectionType selectionType, boolean changeVersion, String infoText, ChangeListener doAfterSelectName, ChangeListener doAfterChange) {
        int totalWidth = 1200;
        int nameWidth = 800;
        int columnSeparator = 75;
        int buttonWidth = 250;
        int rowHeight = 100;
        int rowSeparator = 25;

        Table selectionTable = new Table();
        switch (selectionType) {
            case TEACHERS:
            case STUDENTS:
                Table aDataRow = new Table();
                VerticalGroup verticalGroup = new VerticalGroup();
                ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.defaultSkin);
                scrollPane.setScrollingDisabled(true, false);
                scrollPane.setFadeScrollBars(false);

                // first row for adding an item
                if (changeVersion) {
                    final TextField addItemField = new TextField(infoText, AssetManager.textFieldStyle);
                    addItemField.addListener(new ClickListener() {
                        boolean firstClick = true;

                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (firstClick) addItemField.selectAll();
                            firstClick = false;
                        }
                    });
                    // button for adding a teacher or student
                    TextButton addButton = new TextButton("Add", AssetManager.textButtonStyle);
                    switch (selectionType) {
                        case TEACHERS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    System.out.println("add teacher: " + addItemField.getText());
                                    DataManager.addTeacher(new Teacher(addItemField.getText()));
                                }
                            });
                            break;
                        case STUDENTS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    System.out.println("add student: " + addItemField.getText());
                                    DataManager.addStudent(selectedTeacherIndex, new Student(addItemField.getText()));
                                }
                            });
                            break;
                    }
                    addButton.addListener(doAfterChange);
                    aDataRow.add(addItemField).width(nameWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(addButton).width(buttonWidth).height(rowHeight);
                    aDataRow.add().width(10); // scroll bar width
                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);

                    selectionTable.add(aDataRow);
                    selectionTable.row();
                }

                // rest of rows of items
                switch (selectionType) {
                    case TEACHERS:
                        final ArrayList<Teacher> teachers = DataManager.getTeachers();
                        for (int i = 0; i < teachers.size(); i++) { // make a data row for each teacher
                            aDataRow = new Table();
                            aDataRow.setName(String.valueOf(i));
                            // button for selecting a teacher
                            TextButton aName = new TextButton(teachers.get(i).getName(), AssetManager.textButtonStyle);
                            aName.setName(String.valueOf(i)); // name stores index for button
                            aName.getLabel().setAlignment(Align.left);
                            aName.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    selectedTeacherIndex = Integer.parseInt(actor.getName());
                                    selectedTeacherName = DataManager.getTeachers().get(selectedTeacherIndex).getName();
                                    System.out.println("pressed: " + selectedTeacherName);
                                }
                            });
                            aName.addListener(doAfterSelectName);
                            aDataRow.add(aName).width(nameWidth).height(rowHeight);

                            if (changeVersion) {
                                // button for removing a teacher
                                TextButton deleteButton = new TextButton("Delete", AssetManager.textButtonStyle);
                                deleteButton.setName(String.valueOf(i)); // name stores index for button
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        System.out.println("remove: " + Integer.parseInt(actor.getName()));
                                        DataManager.removeTeacher(Integer.parseInt(actor.getName()));
                                    }
                                });
                                deleteButton.addListener(doAfterChange);
                                aDataRow.add().width(columnSeparator);
                                aDataRow.add(deleteButton).width(buttonWidth).height(rowHeight);
                            }

                            aDataRow.row();
                            aDataRow.add().height(rowSeparator);
                            verticalGroup.addActor(aDataRow);
                        }
                        break;
                    case STUDENTS:
                        final ArrayList<Student> students = DataManager.getStudents(selectedTeacherIndex);
                        for (int i = 0; i < students.size(); i++) { // make a data row for each student
                            aDataRow = new Table();
                            aDataRow.setName(String.valueOf(i));
                            // button for selecting a student
                            TextButton aName = new TextButton(students.get(i).getName(), AssetManager.textButtonStyle);
                            aName.setName(String.valueOf(i)); // name stores index for button
                            aName.getLabel().setAlignment(Align.left);
                            aName.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    selectedStudentIndex = Integer.parseInt(actor.getName());
                                    selectedStudentName = DataManager.getStudents(selectedTeacherIndex).get(selectedStudentIndex).getName();
                                    System.out.println("pressed: " + selectedStudentName);
                                }
                            });
                            aName.addListener(doAfterSelectName);
                            aDataRow.add(aName).width(nameWidth).height(rowHeight);

                            if (changeVersion) {
                                // button for removing a student
                                TextButton deleteButton = new TextButton("Delete", AssetManager.textButtonStyle);
                                deleteButton.setName(String.valueOf(i)); // name stores index for button
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        System.out.println("remove: " + DataManager.getStudents(selectedTeacherIndex).get(Integer.parseInt(actor.getName())).getName());
                                        DataManager.removeStudent(selectedTeacherIndex, Integer.parseInt(actor.getName()));
                                    }
                                });
                                deleteButton.addListener(doAfterChange);
                                aDataRow.add().width(columnSeparator);
                                aDataRow.add(deleteButton).width(buttonWidth).height(rowHeight);
                            }

                            aDataRow.row();
                            aDataRow.add().height(rowSeparator);
                            verticalGroup.addActor(aDataRow);
                        }
                        break;
                }
                if (changeVersion) {
                    selectionTable.add(scrollPane).width(totalWidth).height((rowHeight + rowSeparator) * 5);
                } else {
                    selectionTable.add(scrollPane).width(totalWidth).height((rowHeight + rowSeparator) * 6);
                }
                break;
        }
        return selectionTable;
    }

    public static Table getNewHistoryTable(SelectionType selectionType) {
        int totalWidth = 1200;
        int nameWidth = 600;
        int columnSeparator = 100;
        int dataWidth = 300;
        int rowHeight = 100;
        int rowSeparator = 25;

        Table historyTable = new Table();
        switch (selectionType) {
            case HISTORIES:
                VerticalGroup verticalGroup = new VerticalGroup();
                ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.defaultSkin);
                scrollPane.setScrollingDisabled(true, false);
                scrollPane.setFadeScrollBars(false);

                ArrayList<History> histories = DataManager.getHistory(DataManager.getStudents(selectedTeacherIndex).get(selectedStudentIndex));
                for (int i = 0; i < histories.size(); i++) { // make a data row for each history
                    Table aDataRow = new Table();
                    TextButton aName = new TextButton(histories.get(i).getGamePlayed(), AssetManager.textButtonStyle);
                    TextButton aValue = new TextButton(String.valueOf(histories.get(i).getWordsSpelled().size()), AssetManager.textButtonStyle);

                    aDataRow.add(aName).width(nameWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(aValue).width(dataWidth).height(rowHeight);
                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);
                    verticalGroup.addActor(aDataRow);
                }
                historyTable.add(scrollPane).width(totalWidth).height((rowHeight + rowSeparator) * 6);
                break;
            case GAMES:

                break;
        }
        return historyTable;
    }

    public enum SelectionType {
        TEACHERS, STUDENTS, HISTORIES, GAMES
    }
}
