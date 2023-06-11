package oogasalad.runner.model.action.player;

import oogasalad.i18n.TranslationPackage;
import oogasalad.runner.model.action.Action;

/**
 * Interface for player actions. Player actions are the things that the player can do in the game.
 */
public interface PlayerAction extends Action {

  /**
   * Returns a TranslationPackage describing the label that will be translated and displayed
   * @return descriptive TranslationPackage
   */
  TranslationPackage description();

}
