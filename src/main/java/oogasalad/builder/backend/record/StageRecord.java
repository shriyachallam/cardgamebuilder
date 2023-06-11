package oogasalad.builder.backend.record;

import java.util.List;

public interface StageRecord<SUBSTAGE extends StageRecord> {

  List<ComputerActionRecord> beforeActions();

  List<ComputerActionRecord> afterActions();

  List<SUBSTAGE> stages();
}
