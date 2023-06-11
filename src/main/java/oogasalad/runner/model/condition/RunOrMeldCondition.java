package oogasalad.runner.model.condition;

import java.util.Map;
import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.card.group.CardGroup;

/*
checks that a set is either the standard set or run
 */
public class RunOrMeldCondition implements Condition {
  private final RunCondition runCondition;
  private final MeldCondition meldCondition;
  private final String tag;
  private final Board board;
  public RunOrMeldCondition(Map<String, String> params, Board board) {
    runCondition = new RunCondition(params, board);
    meldCondition = new MeldCondition(params, board);
    this.board=board;
    this.tag = params.get("group_tag");
  }
  @Override
  public boolean isValid() {
    for(CardGroup group: board.groups().stream().filter(group -> group.tag().equals(tag))
    .toArray(CardGroup[]::new)){
      if(!(runCondition.isValid(group) || meldCondition.isValid(group))){
        return false;
      }
    }
      return true;
  }

  public boolean isValid(CardGroup group){
    return runCondition.isValid(group) || meldCondition.isValid(group);
  }

  @Override
  public TranslationPackage description() {
    return new TranslationPackage("RunOrMeld", runCondition.getTag());
  }
}