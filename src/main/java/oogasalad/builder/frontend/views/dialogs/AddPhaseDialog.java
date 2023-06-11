package oogasalad.builder.frontend.views.dialogs;
/**
 *
 *
 *
 * Author: T. Geissler
 *
 *
 *
 */

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.builder.backend.markdownreader.ParsePDFs;
import oogasalad.builder.backend.record.*;
import oogasalad.builder.frontend.Navigator;
import oogasalad.builder.frontend.views.PhaseBuilder;
import oogasalad.builder.frontend.views.StageBuilder;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager;
import oogasalad.service.Firebase;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;

public class AddPhaseDialog {
  private final String PHASE_DIALOG_CSS = "BuilderUI/CSSFiles/phaseDialog.css";
  private final Group root;
  private final Scene scene;
  private final Stage stage;
  private PhaseBuilder pb;
  private final double WIDTH = 500;
  private final double HEIGHT = 535;
  private final ArrayList<KeyValuePair> actions = new ArrayList<>();
  private final ArrayList<KeyValuePair> conditions = new ArrayList<>();
  private ListView actionsList;
  private ListView conditionsList;
  private PhaseRecord isEdit;
  private Button addAction, editAction = new Button(), addCondition , editCondition = new Button(), save;
  private TextField phaseDescription;
  private Spinner transfers;
  private Navigator nav;
  private String language;
  private Translator translator;
  private StageBuilder rootStage;
  private Label compActionsLabel;
  private AdderType currentMode;
  private Firebase myFirebase;

  public enum AdderType {
    DOUBLE_COMP_ACTION, //Add before & after computer actions
    SINGLE_COMP_ACTION, //Add just before computer action
    DOUBLE_ADD_PHASE //Add player action & conditions
  }

  /**
   * Use for initialization from StageBuilder (editRecords either empty or not)
   * @param primaryStage
   * @param editBeforeRecord
   * @param nav
   * @param language
   * @param rootStage
   * @param myFirebase
   */
  public AddPhaseDialog(Stage primaryStage, List<ComputerActionRecord> editBeforeRecord, List<ComputerActionRecord> editAfterRecord, Navigator nav, String language, StageBuilder rootStage, AdderType mode, Firebase myFirebase) {
    this(primaryStage, null, rootStage, null, editBeforeRecord, editAfterRecord, nav, language, mode, myFirebase);
  }

  /**
   * Use for init from PhaseBuilder
   * @param primaryStage
   * @param pb
   * @param isEdit
   * @param nav
   * @param language
   */
  public AddPhaseDialog(Stage primaryStage, PhaseBuilder pb, PhaseRecord isEdit, Navigator nav, String language, Firebase myFirebase) {
    this(primaryStage, pb, null, isEdit, null, null, nav, language, AdderType.DOUBLE_ADD_PHASE, myFirebase);
  }

  public AddPhaseDialog(Stage primaryStage, PhaseBuilder pb, StageBuilder rootStage, PhaseRecord isEdit, List<ComputerActionRecord> editBeforeRecord, List<ComputerActionRecord> editAfterRecord, Navigator nav, String language, AdderType mode, Firebase myFirebase) {
    this.root = new Group();
    this.scene = new Scene(root, WIDTH, HEIGHT);
    scene.getStylesheets().add(PHASE_DIALOG_CSS);
    this.stage = new Stage();
    this.pb = pb;
    this.nav = nav;
    this.isEdit = isEdit;
    this.language = language;
    this.translator = new DefaultTranslator(AppType.BUILDER, language);
    this.rootStage = rootStage;
    this.currentMode = mode;
    this.myFirebase = myFirebase;

    actionsList = new ListView();
    conditionsList = new ListView();
    initListviewListener();

    stage.setScene(scene);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(primaryStage);

    stage.setX(primaryStage.getX());
    stage.setY(primaryStage.getY() + 100);

    populateBeforeAndAfter();
    addAction = new Button();
    addCondition = new Button();
    save = new Button();

    switch (mode) {
      case DOUBLE_ADD_PHASE -> {
        compActionsLabel.setVisible(false);
        stage.setTitle(translator.translateToUserSpace("AddNewPhase"));
        createSubphaseList(translator.translateToUserSpace("Actions"), actionsList, addAction, editAction, 0);
        createSubphaseList(translator.translateToUserSpace("Conditions"), conditionsList, addCondition, editCondition, 1);
      }
      case DOUBLE_COMP_ACTION -> {
        stage.setTitle(translator.translateToUserSpace("ComputerActions"));
        phaseDescription.setVisible(false);
        compActionsLabel.setVisible(true);
        populateComputerActions(actionsList, actions, editBeforeRecord);
        populateComputerActions(conditionsList, conditions, editAfterRecord);
        createSubphaseList(translator.translateToUserSpace("Before"), actionsList, addAction, editAction, 0);
        createSubphaseList(translator.translateToUserSpace("After"), conditionsList, addCondition, editCondition, 1);
      }
      case SINGLE_COMP_ACTION -> {
        stage.setTitle(translator.translateToUserSpace("ComputerActions"));
        phaseDescription.setVisible(false);
        compActionsLabel.setVisible(true);
        populateComputerActions(actionsList, actions, editBeforeRecord);
        createSubphaseList(translator.translateToUserSpace("Before"), actionsList, addAction, editAction, 0);
      }
    }
    createControlFooter();

    stage.show();
  }

  private void populateComputerActions(ListView listView, List<KeyValuePair> dataList, List<ComputerActionRecord> records) {
    if (!Objects.isNull(records) && !records.isEmpty()) {
      for (ComputerActionRecord r : records) {
        dataList.add(r);
        listView.getItems().add(r.toString());
      }
    }
  }



  private void initListviewListener() {
    System.out.println("action edit disable: " + Objects.isNull(actionsList.getSelectionModel().getSelectedItem()));

    editAction.setDisable(Objects.isNull(actionsList.getSelectionModel().getSelectedItem()));
    editCondition.setDisable(Objects.isNull(conditionsList.getSelectionModel().getSelectedItem()));

    actionsList.setOnMouseClicked(t -> editAction.setDisable(Objects.isNull(actionsList.getSelectionModel().getSelectedItem())));
    conditionsList.setOnMouseClicked(t -> editCondition.setDisable(Objects.isNull(conditionsList.getSelectionModel().getSelectedItem())));
  }

  public void newAction(KeyValuePair playerActionRecord) {
    System.out.println("new callback: " + playerActionRecord.toString());
    actions.add(playerActionRecord);
    actionsList.getItems().add(playerActionRecord.toString());
  }

  public void newCondition(KeyValuePair conditionRecord) {
    System.out.println("new conditions: " + conditionRecord.toString());
    conditions.add(conditionRecord);
    conditionsList.getItems().add(conditionRecord.toString());
  }


  private void createControlFooter() {
    Line endLn = new Line();
    endLn.setStartX(0);
    endLn.setEndX(WIDTH);
    endLn.setStartY(490);
    endLn.setEndY(490);
    root.getChildren().add(endLn);

    Button helpButton = new Button();
    addButtonParams(helpButton, translator.translateToUserSpace("Help"), 10, 500);
    helpButton.setOnAction(t -> {
      switch (currentMode) {
        case DOUBLE_ADD_PHASE -> pb.callHelpBtn();
        default -> rootStage.callHelpBtn();
      }
    });
    root.getChildren().add(helpButton);

    addButtonParams(save, translator.translateToUserSpace("Save"), 440, 500);
    save.setDisable(phaseDescription.getText().equals(""));
    save.setOnAction(t -> {
      switch (currentMode) {
        case DOUBLE_ADD_PHASE -> {
          List<ConditionRecord> convertConditions = new ArrayList<>();
          List<PlayerActionRecord> convertActions = new ArrayList<>();
          for (KeyValuePair index: conditions) {
            convertConditions.add((ConditionRecord) index);
          }
          for (KeyValuePair index: actions) {
            convertActions.add((PlayerActionRecord) index);
          }
          PhaseRecord newPhase = new PhaseRecord(phaseDescription.getText(), convertConditions, convertActions);
          pb.addPhase(newPhase);
        }
        case DOUBLE_COMP_ACTION -> {
          List<ComputerActionRecord> convBefore = new ArrayList<>();
          List<ComputerActionRecord> convAfter = new ArrayList<>();
          for (KeyValuePair index: actions) {
            convBefore.add((ComputerActionRecord) index);
          }
          for (KeyValuePair index: conditions) {
            convAfter.add((ComputerActionRecord) index);
          }
          rootStage.addComputerAction(convBefore, StageBuilder.SelectedCompAction.BEFORE);
          rootStage.addComputerAction(convAfter, StageBuilder.SelectedCompAction.AFTER);
        }
        case SINGLE_COMP_ACTION -> {
          List<ComputerActionRecord> convBefore = new ArrayList<>();
          for (KeyValuePair index: actions) {
            convBefore.add((ComputerActionRecord) index);
          }
          rootStage.addComputerAction(convBefore, StageBuilder.SelectedCompAction.BEFORE);
        }
      }
      stage.close();
    });
    root.getChildren().add(save);
  }

  private void createSubphaseList(String label, ListView listView, Button add, Button edit, int position) {
    Label actionsLabel = new Label();
    actionsLabel.setText(label + ":");
    actionsLabel.setLayoutX(10);
    actionsLabel.setLayoutY(45 + position * 225);
    root.getChildren().add(actionsLabel);

    listView.setLayoutX(0);
    listView.setLayoutY(65 + position * 225);
    listView.setPrefWidth(WIDTH);
    listView.setPrefHeight(150);
    if (!Objects.isNull(isEdit)) {
      if (position == 0) {
        for (int i = 0; i < isEdit.playerActions().size(); i++) {
          listView.getItems().add(isEdit.playerActions().get(i));
          actions.add(isEdit.playerActions().get(i));
        }
      } else {
        for (int i = 0; i < isEdit.conditions().size(); i++) {
          listView.getItems().add(isEdit.conditions().get(i));
          conditions.add(isEdit.conditions().get(i));
        }
      }
    }
    root.getChildren().add(listView);

    // Get fields from Parser for combo boxes
    BuilderFileManager.Category currentCategory;
    switch (currentMode) {
      case DOUBLE_ADD_PHASE -> {
         currentCategory = (position == 1) ? BuilderFileManager.Category.CONDITION : BuilderFileManager.Category.PLAYER_ACTION;
      }
      default -> {
        currentCategory = BuilderFileManager.Category.COMPUTER_ACTION;
      }
    }

    addButtonParams(add, translator.translateToUserSpace("Add"), 10, 225 + position * 225);
    add.setOnAction(t -> {
      switch (currentMode) {
        case DOUBLE_COMP_ACTION -> {
          if (position == 0) {
            new HashMapDialog(stage, this, currentCategory, language, StageBuilder.SelectedCompAction.BEFORE, myFirebase);
          } else {
            new HashMapDialog(stage, this, currentCategory, language, StageBuilder.SelectedCompAction.AFTER, myFirebase);
          }
        }
        case SINGLE_COMP_ACTION -> {
          new HashMapDialog(stage, this, currentCategory, language, StageBuilder.SelectedCompAction.BEFORE, myFirebase);
        } default -> new HashMapDialog(stage, this, currentCategory, language, myFirebase);
      }
    });
    addButtonParams(edit, translator.translateToUserSpace("Edit"), 445, 225 + position * 225);
    edit.setOnAction(t -> {
      int index = listView.getSelectionModel().getSelectedIndex();
      if (position == 0) {
        new HashMapDialog(stage, this, actions.get(index), currentCategory, language, myFirebase);
        actions.remove(index);
      } else {
        new HashMapDialog(stage, this, conditions.get(index), currentCategory, language, myFirebase);
        conditions.remove(index);
      }
      listView.getItems().remove(index);
    });
    root.getChildren().add(add);
    root.getChildren().add(edit);
  }

  /**
   * Create Label/txtfield - dynamically chosen depending on operation mode
   */
  private void populateBeforeAndAfter() {
    compActionsLabel = new Label();
    compActionsLabel.setText(translator.translateToUserSpace("SetComputerActions"));
    compActionsLabel.setLayoutX(10);
    compActionsLabel.setLayoutY(10);
    compActionsLabel.setStyle("-fx-font-weight: bold");
    root.getChildren().add(compActionsLabel);

    phaseDescription = new TextField();
    phaseDescription.setPromptText(translator.translateToUserSpace("PhaseDescription"));
    phaseDescription.setText(translator.translateToUserSpace("MyPhase"));
    phaseDescription.setLayoutX(10);
    phaseDescription.setLayoutY(10);
    if (!Objects.isNull(isEdit)) {
      phaseDescription.setText(isEdit.description());
    }
    phaseDescription.setOnKeyTyped(t -> {
      save.setDisable(phaseDescription.getText().equals(""));
    });
    root.getChildren().add(phaseDescription);
  }


  private void addButtonParams(Button button, String txt, int x, int y) {
    button.setText(txt);
    button.setLayoutX(x);
    button.setLayoutY(y);
  }

}
