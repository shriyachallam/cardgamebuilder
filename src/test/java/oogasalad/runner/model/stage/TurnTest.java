package oogasalad.runner.model.stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.builder.backend.record.TurnRecord;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.stage.Stage.Status;
import org.junit.jupiter.api.Test;

class TurnTest {
  PhaseRecord phaseRecord = new PhaseRecord("test", new ArrayList<>(), new ArrayList<>());

  @Test
  void testOnePhase() {
    Turn turn = new Turn(
        new TurnRecord(List.of(phaseRecord), new ArrayList<>(),
            new ArrayList<>()), new ConcreteBoard(() -> {}));
    try {
      assertEquals(Status.DONE, turn.step());
      assertEquals(Status.DONE, turn.step());
    }
    catch (Exception e) {
      fail();
    }
  }
  @Test
  void testTwoPhases() {
    Turn turn = new Turn(
        new TurnRecord(List.of(phaseRecord, phaseRecord), new ArrayList<>(),
            new ArrayList<>()), new ConcreteBoard(() -> {}));
    try{
      assertEquals(Status.ACTIVE, turn.step());
      assertEquals(Status.DONE, turn.step());
      assertEquals(Status.DONE, turn.step());
    }
    catch (Exception e) {
      fail();
    }

  }

  @Test
  void testFivePhases() {
    Turn turn = new Turn(
        new TurnRecord(List.of(phaseRecord, phaseRecord, phaseRecord, phaseRecord, phaseRecord), new ArrayList<>(),
            new ArrayList<>()), new ConcreteBoard(() -> {}));

    try {
      assertEquals(Status.ACTIVE, turn.step());
      assertEquals(Status.ACTIVE, turn.step());
      assertEquals(Status.ACTIVE, turn.step());
      assertEquals(Status.ACTIVE, turn.step());
      assertEquals(Status.DONE, turn.step());
      assertEquals(Status.DONE, turn.step());
    }
    catch (Exception e) {
      fail();
    }

  }


}