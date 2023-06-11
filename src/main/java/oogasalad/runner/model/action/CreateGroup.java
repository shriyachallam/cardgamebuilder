package oogasalad.runner.model.action;

import java.util.List;
import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.action.computer.ComputerAction;
import oogasalad.runner.model.action.player.PlayerAction;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup.Direction;
import oogasalad.runner.model.card.group.CardGroup.ViewType;
import oogasalad.runner.model.card.group.ConcreteCardGroup;

/**
 * Computer callback/player callback that creates a group
 */
public class CreateGroup implements PlayerAction, ComputerAction {
  private final String tag;
  private final ViewType viewType;
  private final Direction direction;
  private final Board board;
  public CreateGroup(Map<String, String> params, Board board) {
    tag = params.get("group_tag").toLowerCase();
    viewType = ViewType.valueOf(params.get("view_type").toUpperCase());
    direction = Direction.valueOf(params.get("direction").toUpperCase());
    this.board = board;
  }
  @Override
  public void act() {
    board.addGroup(new ConcreteCardGroup(tag, viewType, direction));
  }
  /**
   * Returns a TranslationPackage describing the label that will be translated and displayed
   *
   * @return descriptive TranslationPackage
   */
  @Override
  public TranslationPackage description() {
    return new TranslationPackage("CreateGroup",
        direction.toString().toLowerCase(), viewType.toString().toLowerCase(), tag);
  }
}
