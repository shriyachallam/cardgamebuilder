package oogasalad.runner.model.action.computer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import org.junit.jupiter.api.Test;

class CreatePlayerTest {
  @Test
  void test() {
    Board board = new ConcreteBoard(() -> {});
    CreatePlayer action = new CreatePlayer(Map.of("name", "test1"), board);
    assertEquals(0, board.players().size());
    action.act();
    assertEquals(1, board.players().size());
    assertEquals("test1", board.players().get(0).name());
    action = new CreatePlayer(Map.of("name", "test2"), board);
    action.act();
    assertEquals(2, board.players().size());
    assertEquals("test2", board.players().get(1).name());
  }

}