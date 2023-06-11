package oogasalad.file_manager.mock_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.builder.backend.record.ComputerActionRecord;
import oogasalad.builder.backend.record.ConditionRecord;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.builder.backend.record.PhaseRecord;
import oogasalad.builder.backend.record.PlayerActionRecord;
import oogasalad.builder.backend.record.RoundRecord;
import oogasalad.builder.backend.record.TurnRecord;

public class PreconfiguredGameMockGerman extends PreconfiguredGameMock{
  public GameRecord createMockGameBuilderRecord() {
    List<RoundRecord> rounds = createRounds();
    List<String> groups = new ArrayList<>();
    GameRecord gameRecord = new GameRecord("BuilderParserTestStructure", null, rounds, buildBeforeGame(),
        new ArrayList<>());
    return gameRecord;
  }

  private ArrayList<String> createGroups() {
    return new ArrayList<>(List.of("Hand", "Discard", "Deck", "Set"));
  }

  private List<RoundRecord> createRounds() {
    List<TurnRecord> turnBuilders = createTurns();

    // initialize rounds
    RoundRecord roundRecord = new RoundRecord(turnBuilders,
        new ArrayList<>(), new ArrayList<>());
    return List.of(roundRecord);
  }

  private List<TurnRecord> createTurns() {
    List<PhaseRecord> phases = createPhases();

    List<ComputerActionRecord> afterActions = new ArrayList<>();

    Map<String, String> checkWinMap = new HashMap<>();
    checkWinMap.put("Gewinnbedingung","isEmpty");
    ComputerActionRecord checkWin = new ComputerActionRecord("Gewinnkartenanzahl prüfen", checkWinMap);
    afterActions.add(checkWin);


    TurnRecord turnBuilder = new TurnRecord(phases, new ArrayList<>(),
        new ArrayList<>());
    return new ArrayList<>(List.of(turnBuilder));
  }

  private ArrayList<ComputerActionRecord> buildBeforeGame() {
    List<ComputerActionRecord> actions = new ArrayList<>();

    actions.add(new ComputerActionRecord("Gruppe erstellen", Map.of("Gruppenzugehörigkeit", "deck",
        "Deckstil", "stack", "Sichtbarkeit", "face_down")));

    actions.add(new ComputerActionRecord("Deck platzieren", new HashMap<>() {{
      put("Gruppenzugehörigkeit", "deck");
    }}));

    actions.add(new ComputerActionRecord("Gruppe erstellen", Map.of("Gruppenzugehörigkeit", "discard",
        "Deckstil", "stack", "Sichtbarkeit", "face_up")));

    actions.add(new ComputerActionRecord("Spieler erstellen", new HashMap<>() {{
      put("Spielername", "shriya");
    }}));

    actions.add(new ComputerActionRecord("Spieler erstellen", new HashMap<>() {{
      put("Spielername", "del");
    }}));

    actions.add(new ComputerActionRecord("Spieler erstellen", new HashMap<>() {{
      put("Spielername","ken");
    }}));

    actions.add(new ComputerActionRecord("Überweisung", new HashMap<>() {{
      put("Quellgruppe","deck");
      put("Zielgruppe","hand");
      put("Betrag", "7");
    }}));

    actions.add(new ComputerActionRecord("Überweisung", new HashMap<>() {{
      put("Quellgruppe","deck");
      put("Zielgruppe","discard");
      put("Betrag","1");
    }}));

    return new ArrayList<>(actions);
  }


  private List<PhaseRecord> createPhases() {

    /**
     * Draw Phase
     */
    List<PlayerActionRecord> actions = new ArrayList<>();
    Map<String, String> params = new HashMap<>();


    params = new HashMap<>();
    params.put("Quellgruppe", "deck");
    params.put("Zielgruppe", "own");
    PlayerActionRecord deckToHandÜberweisung = new PlayerActionRecord("Spielerziehung", params);
    actions.add(deckToHandÜberweisung);

    params = new HashMap<>();
    params.put("Quellgruppe", "discard");
    params.put("Zielgruppe", "own");
    PlayerActionRecord discardToDeckÜberweisung = new PlayerActionRecord("Spielerziehung", params);
    actions.add(discardToDeckÜberweisung);

    List<ConditionRecord> conditions = new ArrayList<>();

    params = new HashMap<>();
    params.put("Betrag", "1");
    ConditionRecord transferBetragZustand = new ConditionRecord("Überweisungsbetrag", params);

    PhaseRecord draw = new PhaseRecord("Draw", conditions, actions);

    /**
     * Play Phase
     */

    actions = new ArrayList<>();
    PlayerActionRecord createPile = new PlayerActionRecord("Gruppe erstellen", Map.of("Gruppenzugehörigkeit", "set",
        "Deckstil", "sequence", "Sichtbarkeit", "face_up"));
    actions.add(createPile);

    params = new HashMap<>();
    params.put("Quellgruppe", "own");
    params.put("Zielgruppe", "set");
    PlayerActionRecord transferToPile = new PlayerActionRecord("Spielertransfer", params);
    actions.add(transferToPile);

    conditions = new ArrayList<>();
    params = new HashMap<>();
    params.put("Gruppenzugehörigkeit", "set");
    ConditionRecord condition = new ConditionRecord("Gruppe wird ausgeführt oder zusammengelegt", params);
    conditions.add(condition);

    PhaseRecord play = new PhaseRecord("Play", conditions, actions);

    /**
     * Discard Phase
     */

    actions = makeDiscardActions();
    conditions = makeDiscardZustands();
    PhaseRecord discard = new PhaseRecord("discard", conditions, actions);


    return new ArrayList<>(List.of(draw, play, discard));

  }

  private static List<ConditionRecord> makeDiscardZustands() {
    List<ConditionRecord> conditions;
    Map<String, String> params;
    conditions = new ArrayList<>();

    params = new HashMap<>();
    params.put("Betrag", "1");
    ConditionRecord discardÜberweisungBetrag = new ConditionRecord("Überweisungsbetrag", params);
    return conditions;
  }

  private static List<PlayerActionRecord> makeDiscardActions() {
    List<PlayerActionRecord> actions;
    Map<String, String> params;
    actions = new ArrayList<>();
    params = new HashMap<>();
    params.put("Quellgruppe", "own");
    params.put("Zielgruppe", "discard");
    PlayerActionRecord throwAway = new PlayerActionRecord("Spielertransfer", params);
    actions.add(throwAway);
    return actions;
  }
}
