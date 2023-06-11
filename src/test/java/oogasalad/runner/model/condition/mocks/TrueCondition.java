package oogasalad.runner.model.condition.mocks;

import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.condition.Condition;

public class TrueCondition implements Condition {
  public TrueCondition() {
  }
  @Override
  public boolean isValid() {
    return true;
  }

  public boolean check() {
    return true;
  }

  public String name() {
    return "True";
  }

  @Override
  public TranslationPackage description() {
    return null;
  }
}
