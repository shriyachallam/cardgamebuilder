package oogasalad.builder.frontend.views;
/**
 * Author: T. Geissler
 */

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.views.dialogs.AddPhaseDialog;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.builder.frontend.Navigator;
import oogasalad.service.Firebase;

public class RoundBuilder extends StageBuilder {

  private ArrayList<RoundRecord> rounds = new ArrayList<>();
  private List<ComputerActionRecord> beforeRecord = new ArrayList<>(), afterRecord = new ArrayList<>();
  private ListView roundListView;
  private String language;
  private Label compActionsComplete = makeCheckLabel();
  private GUIFactory f;
  private Firebase myFirebase;


  public RoundBuilder(Navigator nav, String language, Firebase myFirebase) {
    super(nav, language, myFirebase);
    this.language = language;
    this.myFirebase = myFirebase;
    f = super.nav.getBuilderFactory();

    super.drawUI();
  }

    @Override
    public void addTitleOfPane() {
        Label roundsTitle = f.createLabel(this.getTranslator().translateToUserSpace("Rounds"), "roundsTitle", Pos.CENTER);
        compActionsComplete = makeCheckLabel();
        Button beforeGameBtn = f.createButton(this.getTranslator().translateToUserSpace("addBeforeGameAction"), "beforeGameBtn", Pos.CENTER);
        beforeGameBtn.setOnAction(t -> {
            new AddPhaseDialog(stage, beforeRecord, afterRecord, nav, language, this, AddPhaseDialog.AdderType.DOUBLE_COMP_ACTION, myFirebase);
        });
        HBox checkGameBefore = f.formatHBox(new HBox(compActionsComplete, beforeGameBtn), 10, Pos.CENTER_RIGHT);

        AnchorPane compActionsBox = new AnchorPane();
        AnchorPane.setLeftAnchor(roundsTitle, 0.0);
        AnchorPane.setRightAnchor(checkGameBefore, 0.0);
        compActionsBox.getChildren().addAll(roundsTitle, checkGameBefore);

        addToVBox(compActionsBox);
    }
    @Override
    public void addListView() {
        roundListView = new ListView();
        roundListView.setPrefHeight(scene.getHeight());
        roundListView.onMouseClickedProperty().set(t -> {
            getEditBtn().setDisable(roundListView.getSelectionModel().isEmpty());
        });
        addToVBox(roundListView);
    }

  @Override
  public void setLeftBtn(Button leftBtn) {
    leftBtn.setText(this.getTranslator().translateToUserSpace("Previous"));
    leftBtn.setOnAction(e -> {
      nav.route(nav.viewOrderIncr(-1), this.getTranslator().translateToUserSpace("CreateGames"));
    });
  }

    @Override
    public void setRightBtn(Button rightBtn) {
        rightBtn.setText(this.getTranslator().translateToUserSpace("Save"));
        rightBtn.setOnAction(e -> {
            saveGame();
        });
    }

  @Override
  public void setAddBtn(Button addBtn) {
    addBtn.setText(this.getTranslator().translateToUserSpace("AddRound"));
    addBtn.setOnAction(t -> {
      TurnBuilder turnBuilder = new TurnBuilder(nav, this, language, myFirebase);
      turnBuilder.initializeScene();
      turnBuilder.newWindow(this.getTranslator().translateToUserSpace("NewRoundScene"), this);
    });
  }

  @Override
  public void setEditBtn(Button editBtn) {
    editBtn.setDisable(roundListView.getSelectionModel().isEmpty());
    editBtn.setOnAction(t -> {
      int index = roundListView.getSelectionModel().getSelectedIndex();
      TurnBuilder turnBuilder = new TurnBuilder(nav, this, language, myFirebase);
      turnBuilder.initializeScene();
      List<TurnRecord> turnRecordToEdit = rounds.get(index).stages();
      for (TurnRecord tr : turnRecordToEdit) {
        turnBuilder.addTurn(tr);
      }
      turnBuilder.newWindow(this.getTranslator().translateToUserSpace("EditRoundScene"), this);
      rounds.remove(index);
      roundListView.getItems().remove(index);
    });
  }

    /**
     * Save computer actions returned from hashmapdialog
     * @param records
     */
    @Override
    public void addComputerAction(List<ComputerActionRecord> records, SelectedCompAction compActionType){
        switch (compActionType) {
            case BEFORE -> { beforeRecord = records; }
            case AFTER -> { afterRecord = records; }
        }
        compActionsComplete.setVisible(true);
    }

  /**
   * Return if view is completed
   * @return
   */
  @Override
  protected boolean viewComplete() {
    return !rounds.isEmpty();
  }


  /**
   * Add round obj to lists
   * @param round
   */
  public void addRound(RoundRecord round) {
    roundListView.getItems().add(round.toString());
    rounds.add(round);
  }

  /**
   * Return round object
   * @return
   */
  @Deprecated
  public ArrayList<RoundRecord> getBuildRounds() {
    return rounds;
  }

    public List<ComputerActionRecord> getBeforeRecord() { return beforeRecord; }

    public List<ComputerActionRecord> getAfterRecord() { return afterRecord; }

}
