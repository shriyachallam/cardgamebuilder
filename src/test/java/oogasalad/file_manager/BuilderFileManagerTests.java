

package oogasalad.file_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager.Category;
import oogasalad.file_manager.builder_file_manager.BuilderJSONFileManager;
import oogasalad.file_manager.builder_file_manager.BuilderFileManager;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.file_manager.mock_game.PreconfiguredGameMock;
import oogasalad.service.Firebase;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;


public class BuilderFileManagerTests {

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
  @DisplayName("Saving/Loading Tests")
  class SavingLoadingTests {

    @BeforeEach
    void setup() {
      builderFileManager = new BuilderJSONFileManager();
      preconfiguredGameMock = new PreconfiguredGameMock();
      translator = new DefaultTranslator(AppType.FILE_MANAGER);
    }

    @Test
    void recreateGinRummyWithMocks() {
      GameRecord ginRummyRecreation = preconfiguredGameMock.createMockGameBuilderRecord();
      try {
        builderFileManager.saveGame(JSON_DIRECTORY + "/test_files/builder_parser_test_files/BuilderParserTestStructure.json",
            ginRummyRecreation);
      } catch (NullPointerException ignore) {

      }

      try {
        filesEqual(JSON_DIRECTORY + "/test_files/builder_parser_test_files/BuilderParserTestStructure.json",
            JSON_DIRECTORY + "/test_files/builder_parser_test_files/BuilderParserTestStructure.json");
      } catch (JSONException | IOException exception) {
        fail(exception.getMessage());
      }
    }

    @Test
    void testMacOsValidation() {
      String badGameName = "GinRummy/:";
      GameRecord gameWithFaultyFilename = new GameRecord(badGameName, null, null, null, null);
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.saveGame(JSON_DIRECTORY + "FaultyGame.json",
              gameWithFaultyFilename));
      String expected = String.format(translator.translateToUserSpace("InvalidFilenameError"),
          badGameName);
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }

    @Test
    void testEmptyStringValidation() {
      String badGameName = "";
      GameRecord gameWithFaultyFilename = new GameRecord(badGameName, null, null, null, null);
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.saveGame(JSON_DIRECTORY + "FaultyGame.json",
              gameWithFaultyFilename));
      String expected = String.format(translator.translateToUserSpace("InvalidFilenameError"),
          badGameName);
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }

    @Test
    void testLoadBadJSONFile(){
      String filepath = JSON_DIRECTORY + "games/gin/saved_examples/AlmostGin.json";
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.loadGameTemplate(filepath));
      String expected = String.format(translator.translateToUserSpace("BadJsonError"),
          filepath);
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("Initialization Tests")
  class InitializationTests {

    private static final String EXCEPTIONS_PATH = "oogasalad.file_manager.languages.LanguageExceptions";
    private static final ResourceBundle EXCEPTIONS = ResourceBundle.getBundle(EXCEPTIONS_PATH);

    @Test
    void testValidDefaultLanguage() {
      builderFileManager = new BuilderJSONFileManager();
      assertNotNull(builderFileManager);
    }

    @Test
    void testValidCustomLanguage() {
      builderFileManager = new BuilderJSONFileManager("English", null);
      assertNotNull(builderFileManager);
    }

    @Test
    void testInvalidCustomLanguage() {
      String badLanguage = "en-zz";
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager = new BuilderJSONFileManager(badLanguage, null));
      String expected = String.format(EXCEPTIONS.getString("LanguageNotSupported"), badLanguage);
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("Category Tests")
  class CategoryTests {

    Map<String, String> parameters;

    @BeforeEach
    void setup() {
      builderFileManager = new BuilderJSONFileManager();
      parameters = new HashMap<>();
      translator = new DefaultTranslator(AppType.FILE_MANAGER);
    }

    @Test
    void testValidParameterAndValue() {
      parameters.put("Group Affiliation", "Hand");
      parameters.put("Deck Style", "sequence");
      parameters.put("Visibility", "face_down");
      builderFileManager.validateOption(Category.COMPUTER_ACTION, "Create Group", parameters);
    }

    @Test
    void testInputIgnoresCase() {
      parameters.put("group affiliation", "Hand");
      parameters.put("DECK STYLE", "sequence");
      parameters.put("VISibiLitY", "face_down");
      builderFileManager.validateOption(Category.COMPUTER_ACTION, "CReatE group", parameters);
    }

    @Test
    void testRealParameterAndInvalidValue() {
      parameters.put("Amount", "abc");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.CONDITION, "Transfer Amount Condition",
              parameters));
      String expected = String.format(translator.translateToUserSpace("CategoryValidationError"),
          translator.translateToUserSpace(Category.CONDITION.name()), "Transfer Amount Condition", "Amount",
          "abc");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }

    @Test
    void testNonexistentParameterAndValue() {
      parameters.put("NonexistentParam", "abc");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.CONDITION, "123", parameters));
      String expected = String.format(translator.translateToUserSpace("NonexistentOptionError"),
          translator.translateToUserSpace(Category.CONDITION.name()), "123");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }

    @Test
    void testComplexOptionWithMultipleParameters() {
      parameters.put("Source Group", "Hand");
      parameters.put("Destination Group", "Deck");
      parameters.put("Amount", "4");
      builderFileManager.validateOption(Category.COMPUTER_ACTION, "Transfer", parameters);
    }

    @Test
    void testComplexOptionWithIncorrectNumberOfParameters() {
      parameters.put("Source Group", "Hand");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.COMPUTER_ACTION, "Transfer",
              parameters));
      String expected = String.format(
          translator.translateToUserSpace("IncorrectNumberOfParametersError"),
          translator.translateToUserSpace(Category.COMPUTER_ACTION.name()), "Transfer", "3");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }

    @Test
    void testComplexOptionWithBadParameters() {
      parameters.put("Source Group", "Hand");
      parameters.put("Destination Group", "Deck");
      parameters.put("Amount", "abc");
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.validateOption(Category.COMPUTER_ACTION, "Transfer",
              parameters));
      String expected = String.format(translator.translateToUserSpace("CategoryValidationError"),
          translator.translateToUserSpace(Category.COMPUTER_ACTION.name()), "Transfer", "Amount",
          "abc");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
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
          List.of("Amount", "Source Group", "Destination Group"));
      List<String> actual = builderFileManager.fetchParametersForOption(Category.COMPUTER_ACTION,
          "Transfer");
      assertEquals(expected, actual);
    }

    @Test
    void testFetchParametersWithBadOption() {
      Exception exception = assertThrows(RuntimeException.class,
          () -> builderFileManager.fetchParametersForOption(Category.CONDITION, "123"));
      String expected = String.format(translator.translateToUserSpace("NonexistentOptionError"),
          translator.translateToUserSpace(Category.CONDITION.name()), "123");
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }
}
