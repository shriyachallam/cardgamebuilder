package oogasalad.runner.model.condition;

import oogasalad.i18n.TranslationPackage;

public interface Condition {

  boolean isValid();

  TranslationPackage description();
}
