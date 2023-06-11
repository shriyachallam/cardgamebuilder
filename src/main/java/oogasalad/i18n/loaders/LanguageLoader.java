package oogasalad.i18n.loaders;

import java.util.ResourceBundle;

/**
 * The LanguageLoader interface is responsible for fetching language resource bundles. This
 * interface can accept semantic language names like "English", then fetch the correct language
 * resource to use during translation.
 *
 * @author alecliu
 */
public interface LanguageLoader {

  /**
   * Convert a language into a language code. For example, "English(UnitedStates)" to "en-us".
   * Throws an exception if the language is not supported.
   *
   * @param language desired language
   * @return resource bundle associated with the language
   * @throws RuntimeException unsupported languages
   */
  ResourceBundle getLanguageResource(String language) throws RuntimeException;

}
