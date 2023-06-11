package oogasalad.builder.frontend.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import oogasalad.builder.frontend.GUIFactory;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.frontend.Navigator;
import oogasalad.service.Firebase;

public class SetUpBuilder extends StageBuilder {
    private File selectedFile;
    private String name;
    private List<ComputerActionRecord> beforeRecord = new ArrayList<>();
    private Label compActionsComplete;
    private GUIFactory f;

  private Firebase myFirebase;


  public SetUpBuilder(Navigator nav, String language, Firebase myFirebase) {
    super(nav, language, myFirebase);
    f = super.nav.getBuilderFactory();
    super.drawUI();
    super.disableAddEditSlice();
    this.myFirebase = myFirebase;
  }


    /**
     * Save computer actions returned from hashmapdialog
     */
    @Override
    public void addTitleOfPane() {
        Label saveDescription = f.createLabel(this.getTranslator().translateToUserSpace("saveDesc"), "saveDesc", Pos.CENTER);

        FileChooser picker = new FileChooser();
        picker.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));

        Button pickButton = f.createButton(this.getTranslator().translateToUserSpace("pickButton"), "pickButton", Pos.CENTER);
        pickButton.setOnAction(event -> {selectedFile = picker.showSaveDialog(nav.getStage());});

        addToVBox(f.formatVBox(new VBox(saveDescription, pickButton), 10, Pos.TOP_LEFT));
    }

  @Override
  public void addListView() {
    ListView dummyList = new ListView();
    dummyList.setVisible(false);
    dummyList.setPrefHeight(scene.getHeight());
    addToVBox(dummyList);
  }

  /**
   * Save computer actions returned from hashmapdialog
   *
   * @param records
   */
  @Override
  public void addComputerAction(List<ComputerActionRecord> records,
      SelectedCompAction compActionType) {
    System.out.println("Saving " + records.toString() + " as comp action type " + compActionType);
    beforeRecord = records;
    compActionsComplete.setVisible(true);
  }


  @Override
  protected boolean viewComplete() {
    return (selectedFile != null);
  }

  @Override
  public void setLeftBtn(Button leftBtn) {
    leftBtn.setText(this.getTranslator().translateToUserSpace("Cancel"));
    leftBtn.setOnAction(event -> stage.close());
  }

  @Override
  public void setRightBtn(Button rightBtn) {
    rightBtn.setText(this.getTranslator().translateToUserSpace("Next"));
    rightBtn.setOnAction(e -> {
      nextView();
    });
  }

  @Override
  public void setAddBtn(Button addBtn) {
    //do nothing
  }

  @Override
  public void setEditBtn(Button editBtn) {
    //do nothing
  }


  public String getName() {
    if (selectedFile != null) {
      name = selectedFile.getName().split(".json")[0];
      return name;
    } else {
      MessagePopUp error = new MessagePopUp(this.getTranslator().translateToUserSpace("NoFileCreated"));
      return error.toString();
    }
  }

  public File getFile() {
    if (selectedFile != null) {
      return selectedFile;
    }
    MessagePopUp error = new MessagePopUp(this.getTranslator().translateToUserSpace("NoFileCreated"));
    return null;
  }

}
