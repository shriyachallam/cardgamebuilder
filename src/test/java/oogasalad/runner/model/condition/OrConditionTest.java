package oogasalad.runner.model.condition;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.runner.model.condition.mocks.FalseCondition;
import oogasalad.runner.model.condition.mocks.TrueCondition;
import org.junit.jupiter.api.Test;

class OrConditionTest {
  @Test
  void test() {
    OrCondition condition = new OrCondition(null);
    assertFalse(condition.isValid());
    condition = new OrCondition(null, new TrueCondition());
    assertTrue(condition.isValid());
    condition = new OrCondition(null, new FalseCondition());
    assertFalse(condition.isValid());
    condition = new OrCondition(null, new TrueCondition(), new FalseCondition());
    assertTrue(condition.isValid());
    condition = new OrCondition(null, new FalseCondition(), new FalseCondition());
    assertFalse(condition.isValid());
    condition = new OrCondition(null, new TrueCondition(), new TrueCondition());
    assertTrue(condition.isValid());
    condition = new OrCondition(null, new FalseCondition(), new FalseCondition(), new FalseCondition(), new TrueCondition());
    assertTrue(condition.isValid());
  }

}