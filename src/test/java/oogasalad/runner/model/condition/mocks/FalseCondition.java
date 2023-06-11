package oogasalad.runner.model.condition.mocks;

import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.condition.Condition;

public class FalseCondition implements Condition {
  public FalseCondition() {
  }
  @Override
  public boolean isValid() {
    return false;
  }

  public boolean check() {
    return false;
  }

  public String name() {
    return "False";
  }

  @Override
  public TranslationPackage description() {
    return null;
  }
}
