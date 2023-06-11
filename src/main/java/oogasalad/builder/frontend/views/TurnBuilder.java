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
import oogasalad.builder.backend.record.*;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.views.dialogs.AddPhaseDialog;
import oogasalad.builder.frontend.Navigator;
import oogasalad.service.Firebase;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;

public class TurnBuilder extends StageBuilder {

  private ArrayList<TurnRecord> turns = new ArrayList<>();
  private List<ComputerActionRecord> beforeRecord = new ArrayList<>(), afterRecord = new ArrayList<>();
  private ListView turnListView;
  private RoundBuilder parent;
  private String language;
  private Label compActionsComplete = new Label();
  private GUIFactory f;
  private Firebase myFirebase;

    public TurnBuilder(Navigator nav, RoundBuilder parent, String language, Firebase myFirebase) {
        super(nav, language, myFirebase);
        f = super.nav.getBuilderFactory();
        this.parent = parent;
        this.language = language;
        super.drawUI();
    }


  @Override
  public void addTitleOfPane() {
    Label turnsTitle = f.createLabel(this.getTranslator().translateToUserSpace("Turns"),
        "turnsTitle", Pos.CENTER);

      Button beforeGameBtn = f.createButton(this.getTranslator().translateToUserSpace("addBeforeRoundAction"), "beforeRoundBtn", Pos.CENTER);
      beforeGameBtn.setOnAction(t -> {
          new AddPhaseDialog(stage, beforeRecord, afterRecord, nav, language, this, AddPhaseDialog.AdderType.DOUBLE_COMP_ACTION, myFirebase);
      });

      compActionsComplete = makeCheckLabel();
      HBox checkRoundBefore = f.formatHBox(new HBox(compActionsComplete, beforeGameBtn), 10, Pos.CENTER_RIGHT);

      AnchorPane compActionsBox = new AnchorPane();
      AnchorPane.setLeftAnchor(turnsTitle, 0.0);
      compActionsBox.getChildren().add(turnsTitle);
      AnchorPane.setRightAnchor(checkRoundBefore, 0.0);
      compActionsBox.getChildren().add(checkRoundBefore);

      addToVBox(compActionsBox);
  }

  @Override
  public void addListView() {
    turnListView = new ListView();
    turnListView.setPrefHeight(scene.getHeight());
    turnListView.onMouseClickedProperty().set(t -> {
        getEditBtn().setDisable(turnListView.getSelectionModel().isEmpty());
    });
    addToVBox(turnListView);
}


  @Override
  public void setLeftBtn(Button leftBtn) {
    leftBtn.setText(this.getTranslator().translateToUserSpace("Cancel"));
    leftBtn.setOnAction(t -> {
        returnValues();
        stage.close();
    });
  }

  @Override
  public void setRightBtn(Button rightBtn) {
    rightBtn.setText(this.getTranslator().translateToUserSpace("Save"));
    rightBtn.setOnAction(t -> {
      returnValues();
      stage.close();
    });
  }

  @Override
  public void setAddBtn(Button addBtn) {
    addBtn.setText(this.getTranslator().translateToUserSpace("AddTurn"));
    addBtn.setOnAction(t -> {
      PhaseBuilder phaseBuilder = new PhaseBuilder(nav, this, language, myFirebase);
      phaseBuilder.initializeScene();
      phaseBuilder.newWindow(this.getTranslator().translateToUserSpace("NewTurnScene"), this);
    });
  }

  @Override
  public void setEditBtn(Button editBtn) {
    editBtn.setText(this.getTranslator().translateToUserSpace("Edit"));
    editBtn.setDisable(turnListView.getSelectionModel().isEmpty());
    editBtn.setOnAction(t -> {
      int index = turnListView.getSelectionModel().getSelectedIndex();
      PhaseBuilder phaseBuilder = new PhaseBuilder(nav, this, language, myFirebase);
      phaseBuilder.initializeScene();
      List<PhaseRecord> phasesToEdit = turns.get(index).stages();
      for (PhaseRecord pr : phasesToEdit) {
        phaseBuilder.addPhase(pr);
      }
      phaseBuilder.newWindow(this.getTranslator().translateToUserSpace("EditTurnScene"), this);
      turns.remove(index);
      turnListView.getItems().remove(index);
    });
  }


  /**
   * Save computer actions returned from hashmapdialog
   * @param records
   */
  @Override
  public void addComputerAction(List<ComputerActionRecord> records,
      SelectedCompAction compActionType) {
    //System.out.println("Saving " + records.toString() + " as comp action type " + compActionType);
    switch (compActionType) {
      case BEFORE -> {
        beforeRecord = records;
      }
      case AFTER -> {
        afterRecord = records;
      }
    }
    compActionsComplete.setVisible(true);
  }

  /**
   * Check if view is complete
   * @return
   */
  @Override
  protected boolean viewComplete() {
    return !turns.isEmpty();
  }

  /**
   * Return record object to superstage
   */
  private void returnValues() {
    if (!turns.isEmpty()) {
      RoundRecord newRound = new RoundRecord(turns, beforeRecord, afterRecord);
      parent.addRound(newRound);
    } else {
      new MessagePopUp(this.getTranslator().translateToUserSpace("NoSubstageMsg"));
    }
  }

  /**
   * Add turn object to lists
   * @param turn
   */
  public void addTurn(TurnRecord turn) {
    System.out.println("Adding new turn: " + turn.toString());
    turnListView.getItems().add(turn.toString());
    turns.add(turn);
  }

  /**
   * Return turnrecord generally
   * @return
   */
  @Deprecated
  public ArrayList<TurnRecord> getBuildTurns() {
    return turns;
  }


}
