package oogasalad.i18n.translators;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import oogasalad.file_manager.exceptions.NonexistentTranslationException;
import oogasalad.i18n.AppType;
import oogasalad.i18n.TranslationPackage;
import oogasalad.i18n.loaders.DefaultLanguageLoader;
import oogasalad.i18n.loaders.LanguageLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The DefaultTranslator class is the default option for fetching the translated messages. It will
 * fetch the correct translation for messages using a common resource key.
 *
 * @author alecliu
 */
public class DefaultTranslator implements Translator {

  private static final String DEFAULT_LANGUAGE = "english(unitedstates)";
  private static final Logger LOG = LogManager.getLogger(DefaultTranslator.class);
  private ResourceBundle messages;
  private LanguageLoader languageLoader;
  private String language;

  /**
   * Default class constructor. Calls constructor with the default language (US English).
   */
  public DefaultTranslator(AppType appType) {
    this(appType, DEFAULT_LANGUAGE);
  }

  /**
   * Class constructor
   *
   * @param appType  desired app translator
   * @param language desired language (i.e. "english")
   */
  public DefaultTranslator(AppType appType, String language) {
    languageLoader = new DefaultLanguageLoader(appType);
    setLanguage(language);
  }

  /**
   * Fetches a translated message based on the resource key
   *
   * @param key resource key
   * @return translated message
   */
  @Override
  public String translateToUserSpace(String key) {
    return messages.getString(key);
  }

  /**
   * Fetches a translated message based on the resource key and an arbitrary list of arguments. This
   * is suited to using the String.format() method.
   *
   * @param translationPackage formatted package containing the key and list of parameters
   * @return translated message
   */
  @Override
  public String translateToUserSpace(TranslationPackage translationPackage) {
    return String.format(messages.getString(translationPackage.resourceKey()),
        translationPackage.arguments());
  }

  /**
   * Translates an outgoing list from the resource representation to the user-friendly
   * representation
   *
   * @param resourceList resource-based list
   * @return user-friendly list
   */
  @Override
  public List<String> translateToUserSpace(List<String> resourceList) {
    List<String> translatedList = new ArrayList<>();
    for (String element : resourceList) {
      translatedList.add(messages.getString(element));
    }
    return translatedList;
  }

  /**
   * Translates an incoming string from user space to the resource representation
   *
   * @param value given value
   * @return corresponding resource representation
   * @throws NonexistentTranslationException if there is no suitable translation for the message
   */
  @Override
  public String translateFromUserSpace(String value) throws NonexistentTranslationException {
    Enumeration<String> keys = messages.getKeys();
    String caseBlindValue = value.toLowerCase();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String caseBlindPotentialMatch = messages.getString(key).toLowerCase();
      if (caseBlindPotentialMatch.equals(caseBlindValue)) {
        return key;
      }
    }

    String message = String.format(messages.getString("NoMatchingMessageError"), value);
    LOG.error(message);
    throw new NonexistentTranslationException(message);
  }

  /**
   * Translates an incoming map of parameters from user representations to resource
   * representations.
   *
   * @param parameters user-based representations of parameters
   * @return a map with the keys translated to their resource representations
   */
  @Override
  public Map<String, String> translateFromUserSpace(Map<String, String> parameters) {
    Map<String, String> resourceMap = new HashMap<>();
    for (String userKey : parameters.keySet()) {
      resourceMap.put(translateFromUserSpace(userKey), parameters.get(userKey));
    }
    return resourceMap;
  }

  /**
   * Returns the current language set in the translator
   *
   * @return current language
   */
  @Override
  public String getLanguage() {
    return language;
  }

  /**
   * Configures the resource bundle to output the correct language
   *
   * @param language desired language (i. e. English)
   */
  public void setLanguage(String language) {
    this.language = language;
    messages = languageLoader.getLanguageResource(language);
  }
}
