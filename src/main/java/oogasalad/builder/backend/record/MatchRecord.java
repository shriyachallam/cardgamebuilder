package oogasalad.builder.backend.record;

import java.util.List;

public record MatchRecord(List<ComputerActionRecord> beforeActions,
                          List<ComputerActionRecord> afterActions,
                          List<GameRecord> stages) implements StageRecord<GameRecord> {

}
