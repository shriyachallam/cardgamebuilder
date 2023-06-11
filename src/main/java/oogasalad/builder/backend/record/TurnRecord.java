package oogasalad.builder.backend.record;

import java.util.List;

public record TurnRecord(List<PhaseRecord> stages, List<ComputerActionRecord> beforeActions,
                         List<ComputerActionRecord> afterActions) implements
    StageRecord<PhaseRecord> {

    public String toVerboseString() {
        return "TurnRecord{" +
                "stages=" + stages +
                ", beforeActions=" + beforeActions +
                ", afterActions=" + afterActions +
                '}';
    }

    @Override
    public String toString() {
        return "New Turn: " + stages + ", \tBefore: " + beforeActions +", \tActions: " + afterActions;
    }
}
