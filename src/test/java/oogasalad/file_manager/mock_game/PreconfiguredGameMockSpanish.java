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

public class PreconfiguredGameMockSpanish extends PreconfiguredGameMock {
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
    checkWinMap.put("Condición del triunfo","isEmpty");
    ComputerActionRecord checkWin = new ComputerActionRecord("Comprobar el número de cartas ganadoras", checkWinMap);
    afterActions.add(checkWin);


    TurnRecord turnBuilder = new TurnRecord(phases, new ArrayList<>(),
        new ArrayList<>());
    return new ArrayList<>(List.of(turnBuilder));
  }

  private ArrayList<ComputerActionRecord> buildBeforeGame() {
    List<ComputerActionRecord> actions = new ArrayList<>();

    actions.add(new ComputerActionRecord("Crear Grupo", Map.of("Afiliación de grupo", "deck",
        "Estilo de mazo", "stack", "Visibilidad", "face_down")));

    actions.add(new ComputerActionRecord("Colocar Deck", new HashMap<>() {{
      put("Afiliación de grupo", "deck");
    }}));

    actions.add(new ComputerActionRecord("Crear Grupo", Map.of("Afiliación de grupo", "discard",
        "Estilo de mazo", "stack", "Visibilidad", "face_up")));

    actions.add(new ComputerActionRecord("Crear Jugador", new HashMap<>() {{
      put("Nombre del jugador", "shriya");
    }}));

    actions.add(new ComputerActionRecord("Crear Jugador", new HashMap<>() {{
      put("Nombre del jugador", "del");
    }}));

    actions.add(new ComputerActionRecord("Crear Jugador", new HashMap<>() {{
      put("Nombre del jugador","ken");
    }}));

    actions.add(new ComputerActionRecord("Transferir", new HashMap<>() {{
      put("Grupo de origen","deck");
      put("Grupo de destino","hand");
      put("Cantidad", "7");
    }}));

    actions.add(new ComputerActionRecord("Transferir", new HashMap<>() {{
      put("Grupo de origen","deck");
      put("Grupo de destino","discard");
      put("Cantidad","1");
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
    params.put("Grupo de origen", "deck");
    params.put("Grupo de destino", "own");
    PlayerActionRecord deckToHandTransferir = new PlayerActionRecord("Jugador Sorteo", params);
    actions.add(deckToHandTransferir);

    params = new HashMap<>();
    params.put("Grupo de origen", "discard");
    params.put("Grupo de destino", "own");
    PlayerActionRecord discardToDeckTransferir = new PlayerActionRecord("Jugador Sorteo", params);
    actions.add(discardToDeckTransferir);

    List<ConditionRecord> conditions = new ArrayList<>();

    params = new HashMap<>();
    params.put("Cantidad", "1");
    ConditionRecord transferCantidadCondición = new ConditionRecord("Importe de la transferencia", params);

    PhaseRecord draw = new PhaseRecord("Draw", conditions, actions);

    /**
     * Play Phase
     */

    actions = new ArrayList<>();
    PlayerActionRecord createPile = new PlayerActionRecord("Crear Grupo", Map.of("Afiliación de grupo", "set",
        "Estilo de mazo", "sequence", "Visibilidad", "face_up"));
    actions.add(createPile);

    params = new HashMap<>();
    params.put("Grupo de origen", "own");
    params.put("Grupo de destino", "set");
    PlayerActionRecord transferToPile = new PlayerActionRecord("transferencia de jugador", params);
    actions.add(transferToPile);

    conditions = new ArrayList<>();
    params = new HashMap<>();
    params.put("Afiliación de grupo", "set");
    ConditionRecord condition = new ConditionRecord("Grupo es Secuencia o Fusión", params);
    conditions.add(condition);

    PhaseRecord play = new PhaseRecord("Play", conditions, actions);

    /**
     * Discard Phase
     */

    actions = makeDiscardActions();
    conditions = makeDiscardCondicións();
    PhaseRecord discard = new PhaseRecord("discard", conditions, actions);


    return new ArrayList<>(List.of(draw, play, discard));

  }

  private static List<ConditionRecord> makeDiscardCondicións() {
    List<ConditionRecord> conditions;
    Map<String, String> params;
    conditions = new ArrayList<>();

    params = new HashMap<>();
    params.put("Cantidad", "1");
    ConditionRecord discardTransferirCantidad = new ConditionRecord("Importe de la transferencia", params);
    return conditions;
  }

  private static List<PlayerActionRecord> makeDiscardActions() {
    List<PlayerActionRecord> actions;
    Map<String, String> params;
    actions = new ArrayList<>();
    params = new HashMap<>();
    params.put("Grupo de origen", "own");
    params.put("Grupo de destino", "discard");
    PlayerActionRecord throwAway = new PlayerActionRecord("transferencia de jugador", params);
    actions.add(throwAway);
    return actions;
  }
}
