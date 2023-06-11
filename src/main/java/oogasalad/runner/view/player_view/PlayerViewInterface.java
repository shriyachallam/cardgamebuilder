package oogasalad.runner.view.player_view;

import javafx.scene.Node;

/**
 * This interface is used to display the player's cards, player icon, and score. It is implemented
 * by ActivePlayerView and InactivePlayerView since these two classes must display the player's
 * cards, player icon, and score differently.
 */
public interface PlayerViewInterface {

  Node getNode();

}

