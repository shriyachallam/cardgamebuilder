package oogasalad.runner.model.stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.runner.model.board.Board;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.player.ConcretePlayer;
import oogasalad.runner.model.stage.Stage.Status;
import org.junit.jupiter.api.Test;

class RoundTest {
  PhaseRecord phaseRecord = new PhaseRecord("test", new ArrayList<>(), new ArrayList<>());
  TurnRecord turnRecord = new TurnRecord(List.of(phaseRecord), new ArrayList<>(),
      new ArrayList<>());
  @Test
  void testOnePlayer() {
    Board board = new ConcreteBoard(() -> {});
    board.addPlayer(new ConcretePlayer("test"));
    Round round = new Round(new RoundRecord(List.of(turnRecord)
        , new ArrayList<>(),
            new ArrayList<>()), board);
    try {
      assertEquals(Status.DONE, round.step());
      assertEquals(Status.DONE, round.step());
    }
    catch (Exception e) {
      fail();
    }
  }
  @Test
  void testTwoPlayers() {
    Board board = new ConcreteBoard(() -> {});
    board.addPlayer(new ConcretePlayer("test1"));
    board.addPlayer(new ConcretePlayer("test2"));
    Round round = new Round(new RoundRecord(List.of(turnRecord)
        , new ArrayList<>(),
            new ArrayList<>()), board);

    try {
      assertEquals(Status.ACTIVE, round.step());
      assertEquals(Status.DONE, round.step());
      assertEquals(Status.DONE, round.step());
    }
    catch (Exception e) {
      fail();
    }

  }
  @Test
  void testFourPlayers() {
    Board board = new ConcreteBoard(() -> {});
    board.addPlayer(new ConcretePlayer("test1"));
    board.addPlayer(new ConcretePlayer("test2"));
    board.addPlayer(new ConcretePlayer("test3"));
    board.addPlayer(new ConcretePlayer("test4"));
    Round round = new Round(new RoundRecord(List.of(turnRecord)
        , new ArrayList<>(),
        new ArrayList<>()), board);

    try {
      assertEquals(Status.ACTIVE, round.step());
      assertEquals(Status.ACTIVE, round.step());
      assertEquals(Status.ACTIVE, round.step());
      assertEquals(Status.DONE, round.step());
      assertEquals(Status.DONE, round.step());
    }
    catch (Exception e) {
      fail();
    }
  }
}