package oogasalad.builder.frontend.views;
/**
 * Author: T. Geissler
 */

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.Main;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.Navigator;
import oogasalad.builder.frontend.views.dialogs.AddPhaseDialog;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.service.Firebase;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;

public class PhaseBuilder extends StageBuilder {
  private ArrayList<PhaseRecord> phases = new ArrayList<>();
  private List<ComputerActionRecord> beforeRecord = new ArrayList<>(), afterRecord = new ArrayList<>();
  private ListView phaseListView;
  private TurnBuilder parent;
  private String language;
  private Label compActionsComplete = new Label();
  private GUIFactory f;
  private Firebase myFirebase;

  public PhaseBuilder(Navigator nav, TurnBuilder parent, String language, Firebase myFirebase) {
    super(nav, language, myFirebase);
    this.parent = parent;
    this.language = language;
    this.myFirebase = myFirebase;
    f = super.nav.getBuilderFactory();

    super.drawUI();
  }

    @Override
    public void addTitleOfPane() {
        Label phraseTitle = f.createLabel(this.getTranslator().translateToUserSpace("Phases"), "phasesTitle", Pos.CENTER);
        compActionsComplete = makeCheckLabel();
        Button beforeGameBtn = f.createButton(this.getTranslator().translateToUserSpace("addBeforeTurnAction"), "beforeTurnBtn", Pos.CENTER);
        beforeGameBtn.setOnAction(t -> {
            new AddPhaseDialog(stage, beforeRecord, afterRecord, nav, language, this, AddPhaseDialog.AdderType.DOUBLE_COMP_ACTION, myFirebase);
        });
        HBox checkTurnBefore = f.formatHBox(new HBox(compActionsComplete, beforeGameBtn), 10, Pos.CENTER_RIGHT);

        AnchorPane compActionsBox = new AnchorPane();
        AnchorPane.setRightAnchor(checkTurnBefore, 0.0);
        AnchorPane.setLeftAnchor(phraseTitle, 0.0);
        compActionsBox.getChildren().addAll(phraseTitle, checkTurnBefore);
        addToVBox(compActionsBox);
    }


    @Override
    public void addListView() {
        phaseListView = new ListView();
        phaseListView.setPrefHeight(scene.getHeight());
        phaseListView.onMouseClickedProperty().set(t -> {
            getEditBtn().setDisable(phaseListView.getSelectionModel().isEmpty());
        });
        addToVBox(phaseListView);
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
    addBtn.setText(this.getTranslator().translateToUserSpace("AddPhase"));
    addBtn.setOnAction(t -> new AddPhaseDialog(nav.getStage(), this, null, nav, language, myFirebase));
  }

  @Override
  public void setEditBtn(Button editBtn) {
    editBtn.setText(this.getTranslator().translateToUserSpace("Edit"));
    editBtn.setOnAction(t -> {
      int index = phaseListView.getSelectionModel().getSelectedIndex();
      new AddPhaseDialog(nav.getStage(), this, phases.get(index), nav, language, myFirebase);
      phases.remove(index);
      phaseListView.getItems().remove(index);
    });
  }

  @Override
  public void addComputerAction(List<ComputerActionRecord> records,
      SelectedCompAction compActionType) {
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
   * Return if view complete (not in carousel)
   * @return
   */
  @Override
  protected boolean viewComplete() {
    return !phases.isEmpty();
  }

  /**
   * Pass view object to superstage
   */
  private void returnValues() {
    if (!phases.isEmpty()) {
      TurnRecord newTurn = new TurnRecord(phases, beforeRecord, afterRecord);
      parent.addTurn(newTurn);
    } else {
      new MessagePopUp(this.getTranslator().translateToUserSpace("NoSubstageMsg"));
    }
  }

  /**
   * Add phase to phasebuilder lists
   * @param newPhase
   */
  public void addPhase(PhaseRecord newPhase) {
    System.out.println("Adding: " + newPhase.toVerboseString());
    phaseListView.getItems().add(newPhase.toString());
    phases.add(newPhase);
  }

  /**
   * return phases - not used
   * @return
   */
  @Deprecated
  public ArrayList<PhaseRecord> getBuildPhases() {
    return phases;
  }


}
