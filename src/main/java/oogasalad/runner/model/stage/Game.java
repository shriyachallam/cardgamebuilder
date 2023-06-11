package oogasalad.runner.model.stage;


import oogasalad.file_manager.runner_file_manager.runner_loader.DefaultLoader;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.action.computer.Transfer;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.board.ImmutableBoard;
import oogasalad.runner.model.board.PlayerBoard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;

/**
 * Represents a game, which contains a board and a step() method to advance the game.
 */
public class Game implements Stage {

  private static final Logger LOG = LogManager.getLogger(Game.class);

  private ComputerAction beforeAction;
  private ComputerAction afterAction;
  private Board board;
  private final RoundRecord roundRecord;
  private Round currentRound;
  private boolean started;
  private int stepCounter;

  Translator translator;

  private GameRecord savedGameTemplate;


  public Game(GameRecord gameRecord, Board board, boolean started, Translator translator) {
    this.board = board;
    board.setGameStep(this::step);
    beforeAction = Stage.makeBeforeAction(gameRecord, board);
    afterAction = Stage.makeAfterAction(gameRecord, board);
    this.roundRecord = gameRecord.stages().get(0);
    this.currentRound = new Round(roundRecord, board);
    this.savedGameTemplate = gameRecord;
    this.started = started;
    this.translator = translator;
  }

  /**
   * Creates a new game from the given game record
   *
   * @param gameRecord
   */
  public Game(GameRecord gameRecord, Translator translator) {
    this.board = new ConcreteBoard(this::step);
    beforeAction = Stage.makeBeforeAction(gameRecord, board);
    afterAction = Stage.makeAfterAction(gameRecord, board);
    this.roundRecord = gameRecord.stages().get(0);
    this.currentRound = new Round(roundRecord, board);
    this.savedGameTemplate = gameRecord;
    started = false;
    this.translator = translator;
  }

  @Override
  public Status step() {
    stepCounter++;
    if (!started) {
      try {
        beforeAction.act();
      }
      catch (Exception e) {
        String message = translator.translateToUserSpace(new TranslationPackage("PlayerActionError",e.getMessage()));
        LOG.error(message);
        throw new RuntimeException(message);
      }
      started = true;
    }
    if (currentRound.step() == Status.DONE) {
      currentRound = new Round(roundRecord, board);
    }
    return Status.ACTIVE;
  }

  public Board getBoard() {
    return this.board;
  }

  /**
   * Sets board to the given board Used when restoring a saved game (or initialzing players in the
   * first place)
   *
   * @param board
   */
  public void setBoard(Board board) {
    this.board = board;
    board.setGameStep(this::step);
  }

  /**
   * Returns an immutable reference to the board
   * Used to allow AI External Assitant to access the board
   * @return
   */
  public ImmutableBoard getImmutableBoard() {
    return this.board;
  }

  public GameRecord getSavedGameTemplate() {
    return this.savedGameTemplate;
  }


  /**
   * Returns the step counter Used for saving and loading games return int stepCounter
   *
   * @return
   */
  public int getStepCounter() {
    return stepCounter;
  }

  public GameRecord getGameRecord() {
    return savedGameTemplate;
  }

}
