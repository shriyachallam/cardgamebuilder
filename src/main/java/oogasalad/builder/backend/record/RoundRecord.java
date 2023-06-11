package oogasalad.builder.backend.record;

import java.util.List;

public record RoundRecord(List<TurnRecord> stages, List<ComputerActionRecord> beforeActions,
                          List<ComputerActionRecord> afterActions) implements
    StageRecord<TurnRecord> {

    public String toVerboseString() {
        return "RoundRecord{" +
                "stages=" + stages +
                ", beforeActions=" + beforeActions +
                ", afterActions=" + afterActions +
                '}';
    }

    @Override
    public String toString() {
        return "New Round: " + stages + ", \tBefore: " + beforeActions +", \tActions: " + afterActions;
    }
}
