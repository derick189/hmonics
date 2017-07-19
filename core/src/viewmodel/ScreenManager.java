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
    static int selectedTeacherIndex;
    static String selectedTeacherName;
    static int selectedStudentIndex;
    static String selectedStudentName;
    private static GdxGame game;

    public static void start(GdxGame gdxGame, Screen firstScreen) {
        ScreenManager.game = gdxGame;
        game.setScreen(firstScreen);
    }

    public static void nextScreen(Screen next) {
        game.setScreen(next);
        // destroy screens
    }

    public static Table getNewSelectionTable(SelectionType selectionType, String infoText, ChangeListener doAfterSelectName, ChangeListener doAfterAddRemove) {
        int tableWidth = 1150;
        int nameWidth = 800;
        int columnSeparator = 75;
        int buttonWidth = 250;
        int rowHeight = 100;
        int rowSeparator = 25;
        boolean changeVersion = !infoText.equals("");

        Table selectionTable = new Table();
//        selectionTable.background(AssetManager.backplate);
        Table aDataRow = new Table();
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.align(Align.topLeft);
        ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.defaultSkin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        switch (selectionType) {
            case TEACHERS:
            case STUDENTS:
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
                    addButton.addListener(doAfterAddRemove);
                    aDataRow.add(addItemField).width(nameWidth).height(rowHeight).left();
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(addButton).width(buttonWidth).height(rowHeight);

                    aDataRow.background(AssetManager.backplate);
                    selectionTable.add(aDataRow).left();
                    selectionTable.row();
                    selectionTable.add().width(tableWidth).height(rowSeparator);
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
                                deleteButton.addListener(doAfterAddRemove);
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
                                deleteButton.addListener(doAfterAddRemove);
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
                    Table backgroundTable = new Table();
                    backgroundTable.background(AssetManager.backplate);
                    backgroundTable.add(scrollPane).width(tableWidth).height((rowHeight + rowSeparator) * 5);
                    selectionTable.add(backgroundTable);
                } else {
                    selectionTable.background(AssetManager.backplate);
                    selectionTable.add(scrollPane).width(tableWidth).height((rowHeight + rowSeparator) * 6);
                }
                break;
            case HISTORIES:
                int dateWidth = 400;
                nameWidth = 500;
                int numberWidth = 125;
                columnSeparator = 50;
                ArrayList<History> histories = DataManager.getHistory(DataManager.getStudents(selectedTeacherIndex).get(selectedStudentIndex));
                for (int i = 0; i < histories.size(); i++) { // make a data row for each history
                    aDataRow = new Table();
                    TextButton aDate = new TextButton(String.valueOf(histories.get(i).getDateString()), AssetManager.textButtonStyle);
                    TextButton aName = new TextButton(histories.get(i).getGamePlayed(), AssetManager.textButtonStyle);
                    TextButton aValue = new TextButton(String.valueOf(histories.get(i).getWordsSpelled().size()), AssetManager.textButtonStyle);

                    aDataRow.add(aDate).width(dateWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(aName).width(nameWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(aValue).width(numberWidth).height(rowHeight);
                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);
                    verticalGroup.addActor(aDataRow);
                }
                selectionTable.background(AssetManager.backplate);
                selectionTable.add(scrollPane).width(tableWidth).height((rowHeight + rowSeparator) * 6);
                break;
        }
        return selectionTable;
    }

    public enum SelectionType {
        TEACHERS, STUDENTS, HISTORIES, GAMES
    }

    public enum Language {
        ENGLISH("English"), HMONG("Hmong");
        public final String fileName;

        Language(String fileName) {
            this.fileName = fileName;
        }
    }
}
