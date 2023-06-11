package oogasalad.builder.frontend;

/**
 *
 *
 *
 * Author: T. Geissler
 *
 *
 *
 */

import javafx.stage.Stage;
import java.util.ArrayList;

import oogasalad.builder.backend.record.GameRecord;
import oogasalad.builder.frontend.views.StageBuilder;
import oogasalad.builder.frontend.views.SetUpBuilder;
import oogasalad.builder.frontend.views.RoundBuilder;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;
import oogasalad.file_manager.builder_file_manager.BuilderJSONFileManager;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager;
import oogasalad.service.Firebase;

public class Navigator {
  public final RoundBuilder ROUNDS;
  public final SetUpBuilder PARAMS;
  private final Stage stage;
  private final StageBuilder[] viewOrder;
  private int viewOrderIndex;
  private BuilderFileManager builderParser;
  private GUIFactory builderFactory = new GUIFactory("builder");
  private Firebase myFirebase;

  /**
   * Navigator setup - add new Views here and above
   *
   * @param primaryStage
   */
  public Navigator(Stage primaryStage, String language, Firebase myFirebase) {
    stage = primaryStage;
    this.myFirebase = myFirebase;

    ROUNDS = new RoundBuilder(this, language, myFirebase);
    PARAMS = new SetUpBuilder(this, language, myFirebase);

    viewOrder = new StageBuilder[]{PARAMS, ROUNDS};

    ROUNDS.initializeScene();
    PARAMS.initializeScene();
  }

  /**
   * Route from current View to next View
   *
   * @param nextView target view to route view
   * @param title    Title of next view
   */
  public void route(StageBuilder nextView, String title) {
    nextView.showView(stage, title);
  }

  /**
   * Increment current view through carousel
   *
   * @param increment
   * @return
   */
  public StageBuilder viewOrderIncr(int increment) {
    viewOrderIndex = viewOrderIndex + increment;
    return viewOrder[viewOrderIndex];
  }

  /**
   * Assemble game object from views
   * @param name
   * @return
   */
  public GameRecord makeRecord(String name){
    GameRecord record = new GameRecord(name, new ArrayList<>(), ROUNDS.getBuildRounds(), ROUNDS.getBeforeRecord(), ROUNDS.getAfterRecord());
    return record;
  }

  /**
   * Send Game object to parser to be saved
   *
   * @param language
   */
  public void sendToParser(String language){
    GameRecord record = makeRecord(PARAMS.getName());
    String location = PARAMS.getFile().toString();

    try {
      builderParser = new BuilderJSONFileManager(language, myFirebase);
      builderParser.saveGame(location, record);
    } catch (Exception e) {
      MessagePopUp saveGameError = new MessagePopUp("Error saving game");
      System.out.println("Error saving game");
      e.printStackTrace();
    }
  }

  /**
   * Return main Stage of the Builder
   * @return
   */
  public Stage getStage() {
    return stage;
  }
  public GUIFactory getBuilderFactory() { return builderFactory; }

}
