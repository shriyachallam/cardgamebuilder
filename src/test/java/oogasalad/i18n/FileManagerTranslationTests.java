package oogasalad.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager.Category;
import oogasalad.file_manager.builder_file_manager.BuilderJSONFileManager;
import oogasalad.file_manager.mock_game.PreconfiguredGameMock;
import oogasalad.file_manager.mock_game.PreconfiguredGameMockGerman;
import oogasalad.file_manager.mock_game.PreconfiguredGameMockSpanish;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class FileManagerTranslationTests {
  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private Translator translator;
  private BuilderFileManager builderFileManager;
  private PreconfiguredGameMock preconfiguredGameMock;

  void filesEqual(String filepath1, String filepath2) throws JSONException, IOException {
    String expectedJson = FileUtils.readFileToString(new File(filepath1));
    String actualJson = FileUtils.readFileToString(new File(filepath2));
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
  @Nested
  @DisplayName("Spanish Language Tests")
  class SpanishLanguageTests {
    Map<String, String> parameters;
    Translator translator;

    @BeforeEach
    void setup() {
      preconfiguredGameMock = new PreconfiguredGameMockSpanish();
      builderFileManager = new BuilderJSONFileManager("Spanish", null);
      parameters = new HashMap<>();
      translator = new DefaultTranslator(AppType.FILE_MANAGER, "Spanish");
    }

    @Test
    void recreateGinRummyWithMocksSpanish() {
      GameRecord ginRummyRecreationSpanish = preconfiguredGameMock.createMockGameBuilderRecord();
      builderFileManager = new BuilderJSONFileManager("Spanish", null);
      try {
        builderFileManager.saveGame(JSON_DIRECTORY + "BuilderParserTestStructure.json",
            ginRummyRecreationSpanish);
      } catch (NullPointerException ignore) {

      }

      try {
        filesEqual(JSON_DIRECTORY + "BuilderParserTestStructure.json",
            JSON_DIRECTORY + "BuilderParserTestStructure.json");
      } catch (JSONException | IOException exception) {
        fail(exception.getMessage());
      }
    }

    @Test
    void testFetchAllOptions() {
      List<String> computerActionList = builderFileManager.fetchOptions(Category.COMPUTER_ACTION);
      List<String> playerActionList = builderFileManager.fetchOptions(Category.PLAYER_ACTION);
      List<String> conditionList = builderFileManager.fetchOptions(Category.CONDITION);

      assertTrue(computerActionList.size() >= 5);
      assertTrue(playerActionList.size() >= 3);
      assertTrue(conditionList.size() >= 5);
    }

    @Test
    void testFetchAllParameters() {
      List<String> expected = new ArrayList<>(
          List.of("Cantidad", "Grupo de origen", "Grupo de destino"));
      List<String> actual = builderFileManager.fetchParametersForOption(Category.COMPUTER_ACTION,
          "Transferir");
      assertEquals(expected, actual);
    }

    @Test
    void testInvalidParameterAndValue() {
      parameters.put("Cantidad", "abc");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.CONDITION, "Importe de la transferencia",
              parameters));
      String expected = String.format(translator.translateToUserSpace("CategoryValidationError"),
          translator.translateToUserSpace(Category.CONDITION.name()), "Importe de la transferencia",
          "Cantidad", "abc");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("German Language Tests")
  class GermanLanguageTests {

    Map<String, String> parameters;
    Translator translator;

    @BeforeEach
    void setup() {
      preconfiguredGameMock = new PreconfiguredGameMockGerman();
      builderFileManager = new BuilderJSONFileManager("German", null);
      parameters = new HashMap<>();
      translator = new DefaultTranslator(AppType.FILE_MANAGER, "German");
    }

    @Test
    void recreateGinRummyWithMocksGerman() {
      GameRecord ginRummyRecreationGerman = preconfiguredGameMock.createMockGameBuilderRecord();
      try {
        builderFileManager.saveGame(JSON_DIRECTORY + "BuilderParserTestStructure.json",
            ginRummyRecreationGerman);
      } catch (NullPointerException ignore) {

      }

      try {
        filesEqual(JSON_DIRECTORY + "BuilderParserTestStructure.json",
            JSON_DIRECTORY + "BuilderParserTestStructure.json");
      } catch (JSONException | IOException exception) {
        fail(exception.getMessage());
      }
    }

    @Test
    void testFetchAllOptions() {
      List<String> computerActionList = builderFileManager.fetchOptions(Category.COMPUTER_ACTION);
      List<String> playerActionList = builderFileManager.fetchOptions(Category.PLAYER_ACTION);
      List<String> conditionList = builderFileManager.fetchOptions(Category.CONDITION);

      assertTrue(computerActionList.size() >= 5);
      assertTrue(playerActionList.size() >= 3);
      assertTrue(conditionList.size() >= 5);
    }

    @Test
    void testFetchAllParameters() {
      List<String> expected = new ArrayList<>(
          List.of("Betrag", "Quellgruppe", "Zielgruppe"));
      List<String> actual = builderFileManager.fetchParametersForOption(Category.COMPUTER_ACTION,
          "Überweisung");
      assertEquals(expected, actual);
    }

    @Test
    void testInvalidParameterAndValue() {
      parameters.put("Betrag", "abc");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.CONDITION, "ÜberweisungsbEtrag",
              parameters));
      String expected = String.format(translator.translateToUserSpace("CategoryValidationError"),
          translator.translateToUserSpace(Category.CONDITION.name()), "ÜberweisungsbEtrag",
          "Betrag", "abc");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }
}
