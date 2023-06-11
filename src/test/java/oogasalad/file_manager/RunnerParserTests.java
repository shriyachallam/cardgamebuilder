package oogasalad.file_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ResourceBundle;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.mock_game.PreconfiguredGameMock;
import oogasalad.file_manager.runner_file_manager.runner_parser.RunnerJSONParser;
import oogasalad.file_manager.runner_file_manager.runner_parser.RunnerParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RunnerParserTests {

  private static final String USER_DIRECTORY = System.getProperty("user.dir");
  private static final String JSON_DIRECTORY = USER_DIRECTORY + "/data/json/";
  private static final String MESSAGES_PATH = "oogasalad.file_manager.languages.en-us_messages";
  private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(MESSAGES_PATH);
  private static RunnerParser runnerParser;
  private PreconfiguredGameMock preconfiguredGameMock;

  @Nested
  @DisplayName("Content Tests")
  class ContentTests {
    @BeforeEach
    void setup() {
      runnerParser = new RunnerJSONParser();
      preconfiguredGameMock = new PreconfiguredGameMock();
    }

    @Test
    void testReadGinRummyFromFile() {
      GameRecord gameRecord = runnerParser.loadGameTemplate(JSON_DIRECTORY + "/test_files/builder_parser_test_files/BuilderParserTestStructure.json");
      GameRecord expected = preconfiguredGameMock.createMockGameBuilderRecord();
      assertEquals(gameRecord.name(), expected.name());
    }

    @Test
    void testReadFromBadFilepath() {
      String badFilePath = "bogus/filepath";
      Exception exception = assertThrows(RuntimeException.class,
          () -> runnerParser.loadGameTemplate(badFilePath));
      String expected = String.format(MESSAGES.getString("FileNotFoundError"), badFilePath);
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
      runnerParser = new RunnerJSONParser();
      assertNotNull(runnerParser);
    }

    @Test
    void testValidCustomLanguage() {
      runnerParser = new RunnerJSONParser("English");
      assertNotNull(runnerParser);
    }

    @Test
    void testInvalidCustomLanguage() {
      String badLanguage = "en-zz";
      Exception exception = assertThrows(RuntimeException.class,
          () -> runnerParser = new RunnerJSONParser(badLanguage));
      String expected = String.format(EXCEPTIONS.getString("LanguageNotSupported"), badLanguage);
      String actual = exception.getMessage();
      assertEquals(expected, actual);
    }
  }
}
