package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import oogasalad.runner.model.condition.mocks.FalseCondition;
import oogasalad.runner.model.condition.mocks.TrueCondition;
import org.junit.jupiter.api.Test;

class AndConditionTest {

  @Test
  void test() {
    AndCondition condition = new AndCondition(null);
    assertTrue(condition.isValid());
    condition = new AndCondition(null, new TrueCondition());
    assertTrue(condition.isValid());
    condition = new AndCondition(null, new FalseCondition());
    assertFalse(condition.isValid());
    condition = new AndCondition(null, new TrueCondition(), new FalseCondition());
    assertFalse(condition.isValid());
    condition = new AndCondition(null, new FalseCondition(), new FalseCondition());
    assertFalse(condition.isValid());
    condition = new AndCondition(null, new TrueCondition(), new TrueCondition());
    assertTrue(condition.isValid());
    condition = new AndCondition(null, new TrueCondition(), new TrueCondition(),
        new TrueCondition(), new FalseCondition());
    assertFalse(condition.isValid());

  }

}