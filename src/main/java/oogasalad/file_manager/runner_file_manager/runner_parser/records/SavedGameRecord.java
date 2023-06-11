package oogasalad.file_manager.runner_file_manager.runner_parser.records;

import java.util.List;
import java.util.Set;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.runner.model.card.group.CardGroup;
import oogasalad.runner.model.player.Player;

public record SavedGameRecord(GameRecord gameTemplate, List<Player> players, Set<CardGroup> groups, int stepCounter){

}
