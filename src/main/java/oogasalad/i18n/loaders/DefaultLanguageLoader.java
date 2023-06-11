package oogasalad.i18n.loaders;

import java.util.ResourceBundle;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;

/**
 * The DefaultLanguageLoader class is the default option for fetching language resource bundles from
 * the project resources. It will accept and translate a semantic language name into a code, then
 * fetch the correct bundle.
 *
 * @author alecliu
 */
public class DefaultLanguageLoader implements LanguageLoader {

  private static final String MESSAGES_PATH = "oogasalad.%s.languages.%s_messages";
  private static final String LANGUAGE_CODES_PATH = "oogasalad.%s.languages.LanguageCodes";
  private static final String EXCEPTIONS_PATH = "oogasalad.%s.languages.LanguageExceptions";

  private AppType appType;
  private ResourceBundle codes;
  private ResourceBundle exceptions;
  /**
   * Class constructor
   * @param appType enumerated app type
   */
  public DefaultLanguageLoader(AppType appType){
    this.appType = appType;
    this.codes = ResourceBundle.getBundle(String.format(LANGUAGE_CODES_PATH, appType.getKey()));
    this.exceptions =ResourceBundle.getBundle(String.format(EXCEPTIONS_PATH, appType.getKey()));
  }

  /**
   * Convert a language into a language code. For example, "English(UnitedStates)" to "en-us".
   * Throws an exception if the language is not supported.
   *
   * @param language desired language
   * @return resource bundle associated with the language
   * @throws RuntimeException unsupported languages
   */
  @Override
  public ResourceBundle getLanguageResource(String language) throws RuntimeException {
    try {
      String code = codes.getString(language.toLowerCase());
      return ResourceBundle.getBundle(String.format(MESSAGES_PATH, appType.getKey(), code));
    } catch (RuntimeException runtimeException) {
      throw new RuntimeException(
          String.format(exceptions.getString("LanguageNotSupported"), language));
    }
  }
}
