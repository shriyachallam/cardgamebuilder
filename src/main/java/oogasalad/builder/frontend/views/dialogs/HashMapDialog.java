package oogasalad.builder.frontend.views.dialogs;
/**
 * Author: T. Geissler
 */

import java.io.IOException;
import java.util.*;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.builder.backend.markdownreader.ParsePDFs;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.ConditionRecord;
import oogasalad.builder.backend.record.KeyValuePair;
import oogasalad.builder.backend.record.PlayerActionRecord;
import oogasalad.builder.frontend.views.StageBuilder;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager;
import oogasalad.file_manager.builder_file_manager.BuilderJSONFileManager;
import oogasalad.service.Firebase;
import oogasalad.i18n.AppType;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;

public class HashMapDialog {
  private final String HASH_DIALOG_CSS = "BuilderUI/CSSFiles/hashDialog.css";
  private final Group root;
  private final Scene scene;
  private final Stage stage;
  private final AddPhaseDialog homeDialog;
  private final double WIDTH = 300;
  private final double HEIGHT = 400;
  private final String title;
  private final ObservableList<String> keys = FXCollections.observableList(new ArrayList<>());
  private final ArrayList<String> vals = new ArrayList<>();
  private final KeyValuePair toEdit;
  private ListView listView;
  private TextField valueField;
  private ComboBox keyBox, actionType;
  private BuilderFileManager.Category category;
  private Button addKV, editKV, deleteKV, save;
  private String language;
  private Translator translator;
  private StageBuilder rootView;
  private StageBuilder.SelectedCompAction compActionType;
  private Firebase myFirebase;
  public HashMapDialog(Stage primaryStage, AddPhaseDialog homeDialog, KeyValuePair toEdit,
      BuilderFileManager.Category currentCategory, String language, Firebase myFirebase) {
    this(primaryStage, homeDialog, toEdit, currentCategory, language, null, null, myFirebase);
  }

  public HashMapDialog(Stage primaryStage, AddPhaseDialog homeDialog,
      BuilderFileManager.Category currentCategory, String language, Firebase myFirebase) {
    this(primaryStage, homeDialog, null, currentCategory, language, null, null, myFirebase);
  }

  public HashMapDialog(Stage primaryStage, AddPhaseDialog homeDialog,
      BuilderFileManager.Category currentCategory, String language,
      StageBuilder.SelectedCompAction compActionType, Firebase myFirebase) {
    this(primaryStage, homeDialog, null, currentCategory, language, null, compActionType, myFirebase);
  }

  public HashMapDialog(Stage primaryStage, KeyValuePair toEdit,
      BuilderFileManager.Category currentCategory, String language, StageBuilder rootView, Firebase myFirebase) {
    this(primaryStage, null, toEdit, currentCategory, language, rootView, null, myFirebase);
  }

  public HashMapDialog(Stage primaryStage, AddPhaseDialog homeDialog, KeyValuePair toEdit,
      BuilderFileManager.Category currentCategory, String language, StageBuilder rootView,
      StageBuilder.SelectedCompAction compActionType, Firebase myFirebase) {
    this.root = new Group();
    this.scene = new Scene(root, WIDTH, HEIGHT);
    scene.getStylesheets().add(HASH_DIALOG_CSS);
    this.stage = new Stage();
    this.homeDialog = homeDialog;
    this.toEdit = toEdit;
    this.category = currentCategory;
    this.language = language;
    this.translator = new DefaultTranslator(AppType.BUILDER, language);
    this.rootView = rootView;
    this.compActionType = compActionType;
    this.myFirebase = myFirebase;

    title = translator.translateToUserSpace(category.toString());
    stage.setTitle(translator.translateToUserSpace(new TranslationPackage("Title", title)));
    stage.setScene(scene);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(primaryStage);

    stage.setX(primaryStage.getX() + 100);
    stage.setY(primaryStage.getY() + 100);

    buildDescription();
    populateActionType();
    createListView();
    functionButtons();
    createControlFooter();

    stage.show();
  }

  private void createListView() {
    listView = new ListView();
    listView.setLayoutX(0);
    listView.setLayoutY(70);
    listView.setPrefWidth(WIDTH);
    listView.setPrefHeight(200);
    listView.setOnMouseClicked(t -> {
      editKV.setDisable(Objects.isNull(listView.getSelectionModel().getSelectedItem()));
      deleteKV.setDisable(Objects.isNull(listView.getSelectionModel().getSelectedItem()));
    });
    if (!Objects.isNull(toEdit)) {
      Map<String, String> map = toEdit.parameters();
      for (Map.Entry<String, String> entry : map.entrySet()) {
        keys.add(entry.getKey());
        vals.add(entry.getValue());
        listView.getItems().add(entry.getKey() + " : " + entry.getValue());
      }
    }
    root.getChildren().add(listView);

    keyBox = new ComboBox();
    keyBox.setPromptText(translator.translateToUserSpace("Key"));
    keyBox.setLayoutX(10);
    keyBox.setLayoutY(285);
    keyBox.setPrefWidth(180);
    keyBox.setOnAction(t -> {
      addKV.setDisable(Objects.isNull(keyBox.getSelectionModel().getSelectedItem()));
    });
    updateKeysCombo(new BuilderJSONFileManager(language, myFirebase));
    root.getChildren().add(keyBox);

    valueField = new TextField();
    valueField.setPromptText(translator.translateToUserSpace("Value"));
    valueField.setLayoutX(10);
    valueField.setLayoutY(315);
    valueField.setPrefWidth(180);
    root.getChildren().add(valueField);
  }

  private List<String> populateActionType() {
    BuilderFileManager builderParser = new BuilderJSONFileManager(language, myFirebase);
    List<String> actions = builderParser.fetchOptions(category);

    actionType.setOnAction(t -> {
      updateKeysCombo(builderParser);
    });
    return actions;
  }

  private void updateKeysCombo(BuilderFileManager builderParser) {
    List<String> keys = builderParser.fetchParametersForOption(category,
        (String) actionType.getSelectionModel().getSelectedItem());

    keyBox.getItems().clear();
    for (String k : keys) {
      keyBox.getItems().add(k);
    }
  }

  private boolean validateInput() {
    String actionName = (String) actionType.getSelectionModel().getSelectedItem();

    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      map.put(keys.get(i), vals.get(i));
    }

    try {
      BuilderFileManager builderValidator = new BuilderJSONFileManager(language, myFirebase);
      builderValidator.validateOption(category, actionName, map);
      switch (category) {
        case CONDITION -> {
          ConditionRecord conditionRecord = new ConditionRecord(actionName, map);
          System.out.println("New condition record: " + conditionRecord.toVerboseString());
          homeDialog.newCondition(conditionRecord);
        }
        case PLAYER_ACTION -> {
          PlayerActionRecord actionBuilder = new PlayerActionRecord(actionName, map);
          System.out.println("new player action: " + actionBuilder.toVerboseString());
          homeDialog.newAction(actionBuilder);
        }
        case COMPUTER_ACTION -> {
          ComputerActionRecord computerActionRecord = new ComputerActionRecord(actionName, map);
          System.out.println("new computer " + compActionType + " action: "
              + computerActionRecord.toVerboseString());
          switch (compActionType) {
            case BEFORE -> homeDialog.newAction(computerActionRecord);
            case AFTER -> homeDialog.newCondition(computerActionRecord);
          }
        }
      }
      stage.close();
      return true;
    } catch (RuntimeException ex) {
      new MessagePopUp(ex.getMessage(), Duration.INDEFINITE);
      return false;
    }
  }

  private void functionButtons() {
    addKV = new Button();
    addKV.setText(translator.translateToUserSpace("Add"));
    addKV.setLayoutX(200);
    addKV.setLayoutY(285);
    addKV.setDisable(Objects.isNull(keyBox.getSelectionModel().getSelectedItem()));

    addKV.setOnAction(t -> {
      String newKey = (String) keyBox.getSelectionModel().getSelectedItem();
      String newVal = valueField.getText();
      keys.add(newKey);
      vals.add(newVal);
      listView.getItems().add(newKey + " : " + newVal);
      valueField.clear();
    });
    root.getChildren().add(addKV);

    editKV = new Button();
    editKV.setText(translator.translateToUserSpace("Edit"));
    editKV.setLayoutX(250);
    editKV.setLayoutY(285);
    editKV.setDisable(Objects.isNull(listView.getSelectionModel().getSelectedItem()));

    editKV.setOnAction(t -> {
      int index = listView.getSelectionModel().getSelectedIndex();
      Scanner delim = new Scanner(
          (String) listView.getSelectionModel().getSelectedItem()).useDelimiter(" : ");
      String key = delim.next();
      String val = delim.next();
      keyBox.getSelectionModel().select(key);
      valueField.setText(val);
      listView.getItems().remove(index);
      vals.remove(index);
      keys.remove(index);
    });
    root.getChildren().add(editKV);

    deleteKV = new Button();
    deleteKV.setText(translator.translateToUserSpace("Delete"));
    deleteKV.setLayoutX(200);
    deleteKV.setLayoutY(315);
    deleteKV.setPrefWidth(85);
    deleteKV.setDisable(Objects.isNull(listView.getSelectionModel().getSelectedItem()));
    deleteKV.setOnAction(t -> {
      int index = listView.getSelectionModel().getSelectedIndex();
      keys.remove(index);
      vals.remove(index);
      listView.getItems().remove(index);
    });
    root.getChildren().add(deleteKV);
  }

  private void buildDescription() {
    //steal first focus
    TextField blank = new TextField();
    blank.setLayoutX(10000);
    blank.setLayoutY(100000);
    root.getChildren().add(blank);

    actionType = new ComboBox();
    actionType.setLayoutX(10);
    actionType.setLayoutY(10);
    actionType.setPrefWidth(180);
    for (String str : populateActionType()) {
      actionType.getItems().add(str);
    }
    if (!Objects.isNull(toEdit)) {
      actionType.getSelectionModel().select(toEdit.name());
    } else {
      actionType.getSelectionModel().select(0); //select first by default
    }
    root.getChildren().add(actionType);

    Label label = new Label();
    label.setText(translator.translateToUserSpace(new TranslationPackage("Parameters", title)));
    label.setLayoutX(10);
    label.setLayoutY(50);
    root.getChildren().add(label);
  }

  private void createControlFooter() {
    Line endLn = new Line();
    endLn.setStartX(0);
    endLn.setEndX(WIDTH);
    endLn.setStartY(355);
    endLn.setEndY(355);
    root.getChildren().add(endLn);

    Button help = newButton(translator.translateToUserSpace("Help"), 10, 365);
    help.setOnAction(t -> {
      callHelpBtn();
    });
    root.getChildren().add(help);

    save = newButton(translator.translateToUserSpace("Save"), 240, 365);
    save.setDisable(keys.isEmpty());
    // Listen for change to list - update save accordingly
    keys.addListener((ListChangeListener.Change<? extends String> c) -> {
      save.setDisable(keys.isEmpty());
    });
    save.setOnAction(t -> {
      validateInput();
    });
    root.getChildren().add(save);
  }

  public void callHelpBtn() {
    try {
      ParsePDFs helpPDF = new ParsePDFs(this.getClass().getName());
      Parent parsedPDF = helpPDF.getRenderedPDF();
      String title = this.getClass().getName()
          .substring(this.getClass().getName().lastIndexOf('.') + 1);
      new PDFViewer(stage, translator.translateToUserSpace("HelpDocs") + title, parsedPDF);
    } catch (IOException ioe) {
      new MessagePopUp(translator.translateToUserSpace("HelpDocsNotFound"), Duration.INDEFINITE);
    }
  }

  private Button newButton(String txt, int x, int y) {
    Button button = new Button();
    button.setText(txt);
    button.setLayoutX(x);
    button.setLayoutY(y);
    return button;
  }
}
