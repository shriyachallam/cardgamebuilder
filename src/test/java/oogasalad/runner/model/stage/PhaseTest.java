package oogasalad.runner.model.stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.runner.model.board.ConcreteBoard;
import oogasalad.runner.model.stage.Stage.Status;
import org.junit.jupiter.api.Test;

class PhaseTest {

  @Test
  void test() {
    Phase phase = new Phase(
        new PhaseRecord("test", new ArrayList<>(), new ArrayList<>()), new ConcreteBoard(() -> {}));
    try {
      assertEquals(phase.step(), Status.DONE);
    }
    catch (Exception e) {
      fail();
    }
  }

}