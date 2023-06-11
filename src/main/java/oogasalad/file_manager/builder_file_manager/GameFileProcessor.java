package oogasalad.file_manager.builder_file_manager;

import java.util.ArrayList;
import java.util.List;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.ConditionRecord;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.builder.backend.record.PlayerActionRecord;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.file_manager.builder_file_manager.validation.os_validation.ValidationStrategy;
import oogasalad.i18n.translators.Translator;

/**
 * The GameFileProcessor class is responsible for validation and conversion of a Builder-originated
 * game record. This class's primary method is to validate the game against OS parameters, then
 * standardize the given game record into resource-consistent namings, suitable for reflection and
 * common across all games.
 *
 * @author alecliu
 */
public class GameFileProcessor {

  private Translator translator;
  private static ValidationStrategy validator;

  /**
   * Class constructor
   *
   * @param translator configured translator object
   */
  public GameFileProcessor(Translator translator) {
    this.translator = translator;
    validator = new ValidationStrategy(translator);
  }

  /**
   * Preliminary checking for valid game parameters. Throws an exception if errors are detected.
   * Additionally, this method standardizes user representations of options in resource based
   * options.
   *
   * @param gameRecord configured game object representation
   * @throws RuntimeException if the game is misconfigured
   */
  public GameRecord processGame(GameRecord gameRecord) throws RuntimeException {
    validator.validateGameName(gameRecord.name());
    return standardizeGame(gameRecord);
  }

  /**
   * Standardize all actions and conditions in an incoming game from user-friendly representations
   * into resource representations.
   *
   * @param gameRecord game-record to be saved
   */
  private GameRecord standardizeGame(GameRecord gameRecord) {
    List<RoundRecord> standardizedRounds = standardizeRounds(gameRecord.stages());
    return new GameRecord(gameRecord.name(), gameRecord.groups(), standardizedRounds,
        standardizeComputerActions(gameRecord.beforeActions()),
        standardizeComputerActions(gameRecord.afterActions()));
  }

  /**
   * Standardize all actions and conditions within a list of rounds
   *
   * @param rounds list of rounds
   * @return list of standardized rounds
   */
  private List<RoundRecord> standardizeRounds(List<RoundRecord> rounds) {
    List<RoundRecord> standardizedRounds = new ArrayList<>();
    for (RoundRecord record : rounds) {
      standardizedRounds.add(new RoundRecord(standardizeTurn(record.stages()),
          standardizeComputerActions(record.beforeActions()),
          standardizeComputerActions(record.afterActions())));
    }
    return standardizedRounds;
  }

  /**
   * Standardize all actions and conditions within a list of turns
   *
   * @param turns list of turns
   * @return list of standardized turns
   */
  private List<TurnRecord> standardizeTurn(List<TurnRecord> turns) {
    List<TurnRecord> standardizedTurns = new ArrayList<>();
    for (TurnRecord record : turns) {
      standardizedTurns.add(new TurnRecord(standardizePhase(record.stages()),
          standardizeComputerActions(record.beforeActions()),
          standardizeComputerActions(record.afterActions())));
    }
    return standardizedTurns;
  }

  /**
   * Standardize all actions and conditions within a list of phases
   *
   * @param phases list of phases
   * @return list of standardized phases
   */
  private List<PhaseRecord> standardizePhase(List<PhaseRecord> phases) {
    List<PhaseRecord> standardizedPhases = new ArrayList<>();
    for (PhaseRecord record : phases) {
      standardizedPhases.add(
          new PhaseRecord(record.description(), standardizeConditions(record.conditions()),
              standardizePlayerActions(record.playerActions())));
    }
    return standardizedPhases;
  }

  /**
   * Standardize a list of computer actions from the user space representation to the resource
   * representation.
   *
   * @param computerActions list of computer action records
   * @return list of resource based records
   */
  private List<ComputerActionRecord> standardizeComputerActions(
      List<ComputerActionRecord> computerActions) {
    List<ComputerActionRecord> standardizedList = new ArrayList<>();
    for (ComputerActionRecord computerActionRecord : computerActions) {
      standardizedList.add(
          new ComputerActionRecord(translator.translateFromUserSpace(computerActionRecord.name()),
              translator.translateFromUserSpace(computerActionRecord.parameters())));
    }
    return standardizedList;
  }

  /**
   * Standardize a list of player actions from the user space representation to the resource
   * representation.
   *
   * @param playerActions list of player action records
   * @return list of resource based records
   */
  private List<PlayerActionRecord> standardizePlayerActions(
      List<PlayerActionRecord> playerActions) {
    List<PlayerActionRecord> standardizedList = new ArrayList<>();
    for (PlayerActionRecord playerActionRecord : playerActions) {
      standardizedList.add(
          new PlayerActionRecord(translator.translateFromUserSpace(playerActionRecord.name()),
              translator.translateFromUserSpace(playerActionRecord.parameters())));
    }
    return standardizedList;
  }

  /**
   * Standardize a list of conditions from the user space representation to the resource
   * representation.
   *
   * @param conditions list of computer action records
   * @return list of resource based records
   */
  private List<ConditionRecord> standardizeConditions(List<ConditionRecord> conditions) {
    List<ConditionRecord> standardizedList = new ArrayList<>();
    for (ConditionRecord conditionRecord : conditions) {
      standardizedList.add(
          new ConditionRecord(translator.translateFromUserSpace(conditionRecord.name()),
              translator.translateFromUserSpace(conditionRecord.parameters())));
    }
    return standardizedList;
  }
}
