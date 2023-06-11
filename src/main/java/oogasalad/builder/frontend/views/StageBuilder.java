package oogasalad.builder.frontend.views;
/**
 * Author: T. Geissler
 */

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.builder.backend.markdownreader.ParsePDFs;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;
import oogasalad.builder.frontend.Navigator;


import java.io.IOException;
import java.util.List;

import oogasalad.Main;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.frontend.views.dialogs.PDFViewer;
import oogasalad.service.Firebase;
import oogasalad.file_manager.builder_file_manager.BuilderJSONFileManager;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class StageBuilder {

  private final String CSS = "BuilderUI/CSSFiles/builderUIDefault.css";
  private static final Logger LOG = LogManager.getLogger(BuilderJSONFileManager.class);


  protected Navigator nav;
  protected String language;
  protected Stage stage;
  protected Scene scene;

  private VBox newRoot = new VBox();
  //protected ResourceBundle myResources;
  private Translator translator;
  private GUIFactory f;
  private Label incompleteLabel;
  protected boolean overrideComplete;
  private Button addBtn, editBtn, leftBtn, helpBtn, rightBtn;
  private Firebase myFirebase;
  public enum SelectedCompAction {BEFORE, AFTER}

  private AnchorPane addEditPane;

  /**
   * Constructor for abstract StageBuilder to be implemented by Rounds, Turns, and Phases
   *
   * @param nav
   * @param language
   */
  public StageBuilder(Navigator nav, String language, Firebase myFirebase) {
    scene = new Scene(newRoot, Main.WIDTH, Main.HEIGHT);
    newRoot.setPrefHeight(scene.getHeight());
    scene.getStylesheets().add(CSS);

    translator = new DefaultTranslator(AppType.BUILDER, language);

    this.nav = nav;
    this.language = language;
    this.myFirebase = myFirebase;
    f = new GUIFactory("stageBuilder");
    overrideComplete = false;

    devToolsListener();
  }

  /**
   * Add override to skip completeness checker if shift is down (for testing)
   */
  private void devToolsListener() {
    scene.setOnKeyPressed(t -> {
      switch (t.getCode()) {
        case K -> overrideComplete = true;
      }
    });
    scene.setOnKeyReleased(t -> {
      switch (t.getCode()) {
        case K -> overrideComplete = false;
      }
    });
  }

  /**
   * Initialize the stage to show the scene
   */
  public void initializeScene() {
    initPrevHelpNextSlice();
    scene.setRoot(newRoot);
  }

  /**
   * Add a node to the root VBox
   *
   * @param node
   */
  public void addToVBox(Node node) {
    VBox wrap = f.formatVBox(new VBox(node), 10, Pos.CENTER_LEFT);
    wrap.getStyleClass().add("vbox");
    newRoot.getChildren().add(wrap);
  }

  /**
   * Hook from HashMapDialog to root Builder view
   *
   * @param records
   */
  public abstract void addComputerAction(List<ComputerActionRecord> records, SelectedCompAction compActionType);

  /**
   * Defines the title and before/after actions of the current stage
   */
  public abstract void addTitleOfPane();

  /**
   * Defines the main listview of the current stage
   */
  public abstract void addListView();

  /**
   * Defines the add and edit button bar of the current stage
   */
  public void initAddEditSlice() {
    addBtn = f.createButton(translator.translateToUserSpace("Add"), "addBtn", Pos.CENTER);
    setAddBtn(addBtn);
    editBtn = f.createButton(translator.translateToUserSpace("Edit"), "editBtn", Pos.CENTER);
    setEditBtn(editBtn);

    addEditPane = new AnchorPane();
    AnchorPane.setLeftAnchor(addBtn, 0.0);
    AnchorPane.setRightAnchor(editBtn, 2.0);
    addEditPane.getChildren().addAll(addBtn, editBtn);

    addToVBox(addEditPane);
  }

  /**
   * Defines the previous, help, and next button bar of the current stage
   */
  public void initPrevHelpNextSlice() {
    leftBtn = f.createButton(null, "leftBtn", Pos.CENTER_LEFT);
    setLeftBtn(leftBtn);

    helpBtn = f.createButton(translator.translateToUserSpace("Help"), "helpBtn", Pos.CENTER);
    helpBtn.setOnAction(e -> {
      callHelpBtn();
    });

    incompleteLabel = f.createLabel(translator.translateToUserSpace("Incomplete"),
        "incompleteLabel", Pos.CENTER);
    incompleteLabel.setVisible(false);

    rightBtn = f.createButton(null, "rightBtn", Pos.CENTER_RIGHT);
    setRightBtn(rightBtn);

    HBox cancelBox = new HBox(10, leftBtn, helpBtn);
    HBox nextBox = new HBox(10, incompleteLabel, rightBtn);

    BorderPane prevHelpNextPane = new BorderPane();
    prevHelpNextPane.setLeft(cancelBox);
    prevHelpNextPane.setRight(nextBox);
    addToVBox(prevHelpNextPane);
  }

  /**
   * Populates the root VBox to create the current stage
   */
  public void drawUI() {
    addTitleOfPane();
    addListView();
    initAddEditSlice();
  }

  /**
   * Defines the functionality of the left button
   *
   * @param leftBtn
   */
  public abstract void setLeftBtn(Button leftBtn);

  /**
   * Defines the functionality of the right button
   *
   * @param rightBtn
   */
  public abstract void setRightBtn(Button rightBtn);

  /**
   * Defines the functionality of the add button
   *
   * @param addBtn
   */
  public abstract void setAddBtn(Button addBtn);

  /**
   * Defines the functionality of the edit button
   *
   * @param editBtn
   */
  public abstract void setEditBtn(Button editBtn);

  /**
   * Disables the add and edit buttons
   */
  public void disableAddEditSlice() {
    addEditPane.setVisible(false);
  }

  /**
   * Getter for the edit button
   *
   * @return Button
   */
  public Button getEditBtn(){
    return editBtn;
  }


  /**
   * Increment carousel to next view
   */
  public void nextView() {
    if (viewComplete() || overrideComplete) {
      nav.route(nav.viewOrderIncr(1), translator.translateToUserSpace("CreateGames"));
    } else {
      incompleteLabel.setVisible(true);
    }
  }

  /**
   * Create completed actions label
   *
   * @return label
   */
  public Label makeCheckLabel() {
    Label compActionsComplete = f.createLabel("✔", "compActionsComplete", Pos.CENTER);
    compActionsComplete.setVisible(false);
    return compActionsComplete;
  }

  /**
   * End of carousel, call nav to save game
   */
  public void saveGame() {
    System.out.println("Save OBJ!");
    if (viewComplete() || overrideComplete) {
      nav.sendToParser(language);
      MessagePopUp savedGame = new MessagePopUp(translator.translateToUserSpace("GameSaved"), translator.translateToUserSpace("GameSaveTitle"));
      stage.close();
    } else {
      incompleteLabel.setVisible(true);
    }
  }

  /**
   * Return true if view is complete/correct
   *
   * @return boolean
   */
  protected abstract boolean viewComplete();

  /**
   * Overall method to initialize the help window
   */
  public void callHelpBtn() {
    try {
      ParsePDFs helpPDF = new ParsePDFs(this.getClass().getName());
      Parent parsedPDF = helpPDF.getRenderedPDF();
      String title = this.getClass().getName()
          .substring(this.getClass().getName().lastIndexOf('.') + 1);
      new PDFViewer(stage, "Help Docs: " + title, parsedPDF);
    } catch (IOException ioe) {
      new MessagePopUp("Help documentation not found...", Duration.INDEFINITE);
    }
  }

  /**
   * Show new view
   *
   * @param stage Next stage to show
   * @param title Title of new page
   */
  public void showView(Stage stage, String title) {
    stage.setScene(scene);
    stage.setTitle(title);
    stage.show();
    this.stage = stage;
  }

  /**
   * Init StageBuilder as a new window
   *
   * @param title
   * @param parent
   */
  public void newWindow(String title, StageBuilder parent) {
    this.stage = new Stage();
    stage.setTitle(title);
    stage.setScene(scene);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(parent.stage);
    stage.setX(parent.stage.getX() + 200);
    stage.setY(parent.stage.getY() + 30);
    stage.show();
  }

  /**
   * get translator for other classes to use
   *
   * @return translator
   */
  public Translator getTranslator() {
    if (translator == null) {
      System.out.println("myResources is null");
      LOG.error("Translator not initialized in builder");
    }
    return translator;
  }


}