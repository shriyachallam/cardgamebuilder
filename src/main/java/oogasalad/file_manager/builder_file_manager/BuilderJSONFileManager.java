package oogasalad.file_manager.builder_file_manager;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.file_manager.exceptions.IncorrectNumberOfParametersException;
import oogasalad.file_manager.exceptions.InvalidParameterException;
import oogasalad.file_manager.exceptions.NonexistentOptionException;
import oogasalad.file_manager.exceptions.NonexistentTranslationException;
import oogasalad.file_manager.builder_file_manager.validation.parameter_validation.OptionTemplateList;
import oogasalad.file_manager.runner_file_manager.runner_parser.RunnerJSONParser;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;
import oogasalad.service.Firebase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The BuilderJSONParser class is responsible for accepting a configured builder_file_manager object
 * representing a user-made game, then converting it to a JSON file. The class uses the GSON library
 * provided by Google. The class also checks for user errors in particular game parameters, throwing
 * exceptions back to the Builder View if any are present. If none are detected, the BuilderParser
 * class will write the file to /data/json. Additionally, this class also provides utilities methods
 * for the Builder app to fetch valid actions and parameters.
 *
 * @author alecliu
 */
public class BuilderJSONFileManager implements BuilderFileManager {

  private static final String OPTIONS_FILEPATH = System.getProperty("user.dir")
      + "/src/main/resources/oogasalad/file_manager/valid_options/valid_%s.json";
  private static final Logger LOG = LogManager.getLogger(BuilderJSONFileManager.class);
  private GameFileProcessor gameFileProcessor;
  private Translator translator;
  private List<OptionTemplateList> validOptionLists;

  private Firebase myFirebase;

  /**
   * Default class constructor.
   */
  public BuilderJSONFileManager() {
    translator = new DefaultTranslator(AppType.FILE_MANAGER);
    gameFileProcessor = new GameFileProcessor(translator);
    initializeValidOptionLists();
  }

  /**
   * Class constructor.
   *
   * @param language locality for user-facing messages
   */
  public BuilderJSONFileManager(String language, Firebase myFirebase) {
    translator = new DefaultTranslator(AppType.FILE_MANAGER, language);
    this.myFirebase = myFirebase;
    gameFileProcessor = new GameFileProcessor(translator);
    initializeValidOptionLists();
  }

  /**
   * Load in all valid options for all game categories
   */
  private void initializeValidOptionLists() {
    validOptionLists = new ArrayList<>();
    Gson g = new Gson();
    for (Category category : Category.values()) {
      String filepath = String.format(OPTIONS_FILEPATH, category.getKey());
      try {
        FileReader fr = new FileReader(filepath);
        validOptionLists.add(g.fromJson(fr, OptionTemplateList.class));
      } catch (FileNotFoundException fileNotFoundException) {
        String message = String.format(
            translator.translateToUserSpace("BadCategoryEnumerationError"),
            translator.translateToUserSpace(category.name()));
        LOG.fatal(message);
        throw new RuntimeException(message);
      }
    }
  }

  /**
   * Primary entry point from the Builder to write a user-made game to a file.
   *
   * @param filepath   filepath to save to
   * @param gameRecord configured game object representation
   * @throws RuntimeException if a game is misconfigured
   */
  @Override
  public void saveGame(String filepath, GameRecord gameRecord) {
    GameRecord processedGame = gameFileProcessor.processGame(gameRecord);
    convertToJson(filepath, processedGame, myFirebase);
  }

  /**
   * Load a game template from a file into the builder for editing. Note that this functionality is
   * duplicated across the Builder and Runner File Managers. This is intentional in order to reduce
   * dependency.
   *
   * @param filepath given filepath
   * @return loaded game template
   * @throws RuntimeException if the file does not exist or is invalid
   */
  @Override
  public GameRecord loadGameTemplate(String filepath) throws RuntimeException {
    try {
      Gson g = new Gson();
      FileReader fr = new FileReader(filepath);
      GameRecord gameRecord = g.fromJson(fr, GameRecord.class);
      LOG.info(String.format(translator.translateToUserSpace("SuccessfulReadFromFile"),
          filepath));
      return gameRecord;
    } catch (FileNotFoundException fileNotFoundException) {
      String message = String.format(translator.translateToUserSpace("FileNotFoundError"),
          (filepath));
      LOG.error(message);
      throw new RuntimeException(message);
    } catch (JsonSyntaxException | JsonIOException jsonParseException) {
      String message = String.format(translator.translateToUserSpace("BadJsonError"), filepath);
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Update the language of messages returned by the parser.
   *
   * @param language desired language
   */
  @Override
  public void setLanguage(String language) {
    translator.setLanguage(language);
  }


  /**
   * Writes a game object to a JSON file in /data/json.
   *
   * @param filepath   given filepath
   * @param gameRecord configured game object
   * @throws RuntimeException if a bad game name is provided
   */
  private void convertToJson(String filepath, GameRecord gameRecord, Firebase myFirebase) throws RuntimeException {
    try {
      Gson g = new Gson();
      FileWriter fw = new FileWriter(filepath);
      g.toJson(gameRecord, fw);
      fw.close();
      LOG.info(String.format(translator.translateToUserSpace("SuccessfulWriteToFile"),
          gameRecord.name()));
      myFirebase.uploadGame(gameRecord.name(), g.toJson(gameRecord));
    } catch (IOException ioException) {
      String message = translator.translateToUserSpace("WriteToDirectoryError");
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }


  /**
   * Helper method to fetch the correct list of rules
   *
   * @param category rule enumeration
   * @return valid options for that rule
   */
  private OptionTemplateList lookupList(Category category) {
    for (OptionTemplateList optionTemplateList : validOptionLists) {
      if (optionTemplateList.getCategory().equals(category.getKey())) {
        return optionTemplateList;
      }
    }
    // Should be unreachable
    return null;
  }

  /**
   * Fetch all available options for a category
   *
   * @param categoryType category type
   * @return list of available options
   */
  @Override
  public List<String> fetchOptions(Category categoryType) {
    OptionTemplateList validOptions = lookupList(categoryType);
    List<String> validOptionNames = validOptions.getAllOptionNames();
    return translator.translateToUserSpace(validOptionNames);
  }

  /**
   * Fetches a list of all required parameters for a particular option
   *
   * @param categoryType category type
   * @param optionName   list of required parameters
   * @return list of all required parameters
   * @throws NonexistentOptionException if the provided option does not exist in the category
   */
  @Override
  public List<String> fetchParametersForOption(Category categoryType,
      String optionName) throws NonexistentOptionException {
    OptionTemplateList validOptions = lookupList(categoryType);
    try {
      String translatedOption = translator.translateFromUserSpace(optionName);
      List<String> requiredParameterNames = validOptions.getParametersForOption(translatedOption);
      return translator.translateToUserSpace(requiredParameterNames);
    } catch (NonexistentOptionException | NonexistentTranslationException nonexistentException) {
      String message = String.format(translator.translateToUserSpace("NonexistentOptionError"),
          translator.translateToUserSpace(categoryType.name()), optionName);
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }

  /**
   * Validates if a given option was provided with the correct input type
   *
   * @param optionName option name
   * @param parameters map of values to parameters
   * @throws RuntimeException if the given information is invalid, with different exceptions thrown
   *                          in different scenarios
   */
  @Override
  public void validateOption(Category categoryType, String optionName,
      Map<String, String> parameters)
      throws RuntimeException {
    try {
      String resourceOptionName = translator.translateFromUserSpace(optionName);
      Map<String, String> resourceParameters = translator.translateFromUserSpace(parameters);
      OptionTemplateList validOptions = lookupList(categoryType);
      validOptions.validateExpectedType(resourceOptionName, resourceParameters);
    } catch (InvalidParameterException invalidParameterException) {
      String badParameterKey = translator.translateToUserSpace(
          invalidParameterException.getMessage());
      String message = String.format(translator.translateToUserSpace("CategoryValidationError"),
          translator.translateToUserSpace(categoryType.name()), optionName, badParameterKey,
          parameters.get(badParameterKey));
      LOG.error(message);
      throw new RuntimeException(message);
    } catch (NonexistentOptionException | NonexistentTranslationException nonexistentException) {
      String message = String.format(translator.translateToUserSpace("NonexistentOptionError"),
          translator.translateToUserSpace(categoryType.name()), optionName);
      LOG.error(message);
      throw new RuntimeException(message);
    } catch (IncorrectNumberOfParametersException incorrectNumberOfParametersException) {
      String message = String.format(
          translator.translateToUserSpace("IncorrectNumberOfParametersError"),
          translator.translateToUserSpace(categoryType.name()), optionName,
          incorrectNumberOfParametersException.getMessage());
      LOG.error(message);
      throw new RuntimeException(message);
    }
  }
}
