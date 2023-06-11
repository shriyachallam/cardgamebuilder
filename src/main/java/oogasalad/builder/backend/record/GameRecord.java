package oogasalad.builder.backend.record;

import java.util.List;

//TODO -- add javadoc
//TODO -- Why is there group here? Not used by Stages in runner
public record GameRecord(String name, List<String> groups, List<RoundRecord> stages,
                         List<ComputerActionRecord> beforeActions,
                         List<ComputerActionRecord> afterActions) implements
    StageRecord<RoundRecord> {

}
