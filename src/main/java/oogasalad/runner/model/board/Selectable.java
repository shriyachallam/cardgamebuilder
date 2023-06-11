package oogasalad.runner.model.board;

/**
 * Selectable interface for objects that can be selected and deselected by a GameViewObserver
 */
public interface Selectable {

    void select();

    void deselect();

    boolean isSelected();
    int id();

}
