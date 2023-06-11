package oogasalad.runner.model.action.computer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.runner.model.board.Board;

/**
 * Class for a list of computer actions. This is used to combine multiple computer actions into one
 * callback.
 */
public class ComputerActionList implements ComputerAction {

  List<ComputerAction> actions;

  public ComputerActionList(ComputerAction... actions) {
    this.actions = new ArrayList<>(Arrays.asList(actions));
  }

  public void add(ComputerAction action) {
    actions.add(action);
  }

  @Override
  public void act() {
    actions.forEach(action -> action.act());
  }
}
