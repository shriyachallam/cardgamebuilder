package oogasalad.runner.controller.observer;

public interface Observable<T extends Observer> {

  void register(T observer);
}
