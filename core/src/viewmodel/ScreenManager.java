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
import view.screens.TeacherScreen;

import java.util.ArrayList;

/**
 * Creates screens and manages the current screen and the global state variables.
 */
public class ScreenManager {
    public static Language selectedLanguage = Language.HMONG;
    public static int selectedTeacherIndex;
    public static String selectedTeacherName;
    public static int selectedStudentIndex;
    public static String selectedStudentName;

    private static GdxGame game;

    public static void start(GdxGame gdxGame, Screen firstScreen) {
        ScreenManager.game = gdxGame;
        game.setScreen(firstScreen);
    }

    public static void setScreen(Screen next) {
        game.setScreen(next);
    }

    /**
     * Creates a table that is all parts of the non-game screens.
     *
     * @param screenType        What kind of screen.
     * @param titleText         Text for the title of the screen.
     * @param doOnBackButton    Do this after back button is pressed.
     * @param doAfterSelectItem Do this after an item is selected.
     * @param addItemInfoText   Info text for the default name in the add item field.
     * @param doAfterAddRemove  Do this after an item has been added or removed.
     * @return A table to be used as a screen.
     */
    public static Table screenFactory(ScreenType screenType, String titleText, ChangeListener doOnBackButton, ChangeListener doAfterSelectItem, String addItemInfoText, ChangeListener doAfterAddRemove) {
        boolean allowChanges = !(addItemInfoText == null);
        int backButtonSize = 150;

        Table mainTable = new Table();
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.height);
        Label titleLabel = new Label(titleText, AssetManager.labelStyle64);
        titleLabel.setBounds(300, 850, mainTable.getWidth() - 600, 200);
        mainTable.addActor(titleLabel);
        Table bodyTable = new Table();
        bodyTable.setBounds(300, 75, mainTable.getWidth() - 600, 750);
        mainTable.addActor(bodyTable);
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.align(Align.topLeft);
        ScrollPane scrollPane = new ScrollPane(verticalGroup, AssetManager.defaultSkin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        ImageButton backButton = new ImageButton(AssetManager.backButtonStyle);
        backButton.setBounds(50, 50, backButtonSize, backButtonSize);
        backButton.setSize(backButtonSize, backButtonSize);
        backButton.addListener(doOnBackButton);
        mainTable.addActor(backButton);

        int rowHeight = 100;
        int rowSeparator = 25;
        Table aDataRow = new Table();

        switch (screenType) {
            case TEACHERS:
            case STUDENTS:
                int nameWidth = 800;
                int columnSeparator = 75;
                int buttonWidth = 300;

                // If the screen allows changes to entries, have first row be the add entry row.
                if (allowChanges) {
                    aDataRow.background(AssetManager.backPlate);
                    aDataRow.pad(10);

                    final TextField addItemField = new TextField(addItemInfoText, AssetManager.textFieldStyle64);
                    addItemField.addListener(new ClickListener() {
                        boolean firstClick = true;

                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (firstClick) addItemField.selectAll();
                            firstClick = false;
                        }
                    });

                    // Button will add a new teacher or student with the string in the text field as the name.
                    TextButton addButton = new TextButton("Add", AssetManager.textButtonStyle64);
                    switch (screenType) {
                        case TEACHERS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    DataManager.addTeacher(new Teacher(addItemField.getText()));
                                }
                            });
                            break;
                        case STUDENTS:
                            addButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    DataManager.addStudent(selectedTeacherIndex, new Student(addItemField.getText()));
                                }
                            });
                            break;
                    }
                    addButton.addListener(doAfterAddRemove);

                    aDataRow.add(addItemField).width(nameWidth).height(rowHeight).left();
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(addButton).width(buttonWidth).height(rowHeight);
                    bodyTable.add(aDataRow).left();
                    bodyTable.row();
                }

                // Make a row for each entry of either teachers or students.
                switch (screenType) {
                    case TEACHERS:
                        final ArrayList<Teacher> teachers = DataManager.getTeachers();
                        for (int i = 0; i < teachers.size(); i++) { // Make a data row for each teacher.
                            aDataRow = new Table();

                            // Button for selecting a teacher.
                            TextButton nameButton = new TextButton(teachers.get(i).getName(), AssetManager.textButtonStyle64);
                            nameButton.setName(String.valueOf(i)); // Name is the list index for this button.
                            nameButton.getLabel().setAlignment(Align.left);
                            nameButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    selectedTeacherIndex = Integer.parseInt(actor.getName());
                                    selectedTeacherName = DataManager.getTeachers().get(selectedTeacherIndex).getName();
                                }
                            });
                            nameButton.addListener(doAfterSelectItem);
                            aDataRow.add(nameButton).width(nameWidth).height(rowHeight);

                            // Button for removing a teacher.
                            if (allowChanges) {
                                TextButton deleteButton = new TextButton("Delete", AssetManager.textButtonStyle64);
                                deleteButton.setName(String.valueOf(i)); // Name is the list index for this button.
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
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
                        for (int i = 0; i < students.size(); i++) { // Make a data row for each student.
                            aDataRow = new Table();

                            // Button for selecting a student.
                            TextButton nameButton = new TextButton(students.get(i).getName(), AssetManager.textButtonStyle64);
                            nameButton.setName(String.valueOf(i)); // Name is the list index for this button.
                            nameButton.getLabel().setAlignment(Align.left);
                            nameButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    selectedStudentIndex = Integer.parseInt(actor.getName());
                                    selectedStudentName = DataManager.getStudents(selectedTeacherIndex).get(selectedStudentIndex).getName();
                                }
                            });
                            nameButton.addListener(doAfterSelectItem);
                            aDataRow.add(nameButton).width(nameWidth).height(rowHeight);

                            // Button for removing a student.
                            if (allowChanges) {
                                TextButton deleteButton = new TextButton("Delete", AssetManager.textButtonStyle64);
                                deleteButton.setName(String.valueOf(i)); // Name is the list index for this button.
                                deleteButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
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

                Table backgroundTable = new Table();
                backgroundTable.background(AssetManager.backPlate);
                backgroundTable.pad(10);
                bodyTable.add(backgroundTable);
                if (allowChanges) {
                    backgroundTable.add(scrollPane).width(nameWidth + columnSeparator + buttonWidth + 20).height((rowHeight + rowSeparator) * 5);
                } else {
                    backgroundTable.add(scrollPane).width(nameWidth + 20).height((rowHeight + rowSeparator) * 6);
                }
                break;
            case HISTORIES:
                int dateWidth = 400;
                int gameNameWidth = 500;
                int numberWidth = 150;
                columnSeparator = 50;

                final ArrayList<History> histories = DataManager.getHistory(DataManager.getStudents(selectedTeacherIndex).get(selectedStudentIndex));
                for (int i = 0; i < histories.size(); i++) { // Make a data row for each history.
                    aDataRow = new Table();
                    Label dateLabel = new Label(String.valueOf(histories.get(i).getDateString()), AssetManager.labelStyle64);
                    dateLabel.setAlignment(Align.center);
                    Label gameNameLabel = new Label(histories.get(i).getGamePlayed(), AssetManager.labelStyle64);
                    gameNameLabel.setAlignment(Align.center);
                    TextButton numberOfWordsButton = new TextButton(String.valueOf(histories.get(i).getWordsSpelled().size()), AssetManager.textButtonStyle64);
                    numberOfWordsButton.setName(String.valueOf(i)); // Name is the list index for this button.
                    numberOfWordsButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            StringBuilder wordList = new StringBuilder();
                            for (String word : histories.get(Integer.parseInt(actor.getName())).getWordsSpelled()) {
                                wordList.append(word).append("\n");
                            }

                            Dialog dialog = new Dialog("Words Spelled", AssetManager.defaultSkin);
                            dialog.text(wordList.toString());
                            dialog.button("OK");
                            dialog.show(TeacherScreen.stage);
                        }
                    });

                    aDataRow.add(dateLabel).width(dateWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(gameNameLabel).width(gameNameWidth).height(rowHeight);
                    aDataRow.add().width(columnSeparator);
                    aDataRow.add(numberOfWordsButton).width(numberWidth).height(rowHeight);
                    aDataRow.row();
                    aDataRow.add().height(rowSeparator);
                    verticalGroup.addActor(aDataRow);
                }

                backgroundTable = new Table();
                backgroundTable.background(AssetManager.backPlate);
                backgroundTable.pad(10);
                bodyTable.add(backgroundTable);
                backgroundTable.add(scrollPane).width(dateWidth + gameNameWidth + numberWidth + columnSeparator * 2).height((rowHeight + rowSeparator) * 6);
                break;
            case GAMES:
                gameNameWidth = 500;
                int gameNameHeight = 300;
                columnSeparator = 75;

                bodyTable.background(AssetManager.backPlate);
                Table gamesList = new Table();
                bodyTable.add(gamesList);
                bodyTable.add().width(columnSeparator);

                TextButton spellingGameButton = new TextButton("Spelling Game", AssetManager.textButtonStyle64);
                spellingGameButton.addListener(doAfterSelectItem);
                gamesList.add(spellingGameButton).width(gameNameWidth).height(gameNameHeight);

                Table languageSelectList = new Table();
                bodyTable.add(languageSelectList);

                TextButton englishButton = new TextButton("English", AssetManager.textButtonStyle64Checked);
                englishButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ScreenManager.selectedLanguage = Language.ENGLISH;
                    }
                });
                languageSelectList.add(englishButton).width(gameNameWidth).height(gameNameHeight);
                languageSelectList.row();
                TextButton hmongButton = new TextButton("Hmong", AssetManager.textButtonStyle64Checked);
                hmongButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ScreenManager.selectedLanguage = Language.HMONG;
                    }
                });
                languageSelectList.add(hmongButton).width(gameNameWidth).height(gameNameHeight);

                switch (selectedLanguage) {
                    case ENGLISH:
                        englishButton.setChecked(true);
                        break;
                    case HMONG:
                        hmongButton.setChecked(true);
                        break;
                }
                ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
                buttonGroup.setMaxCheckCount(1);
                buttonGroup.setMinCheckCount(1);
                buttonGroup.add(englishButton);
                buttonGroup.add(hmongButton);
                break;
        }
        return mainTable;
    }

    public enum ScreenType {
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
