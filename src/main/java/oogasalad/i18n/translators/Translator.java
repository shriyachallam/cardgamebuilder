package oogasalad.i18n.translators;

import java.util.List;
import java.util.Map;

import oogasalad.file_manager.exceptions.NonexistentTranslationException;
import oogasalad.i18n.TranslationPackage;

/**
 * The Translator interface is a wrapper class around the message-related resources. It will fetch
 * the correct translation for messages using a common resource key. Additionally, it will manage
 * general translations between the resource files and the view.
 *
 * @author alecliu
 */
public interface Translator {

  /**
   * Fetches a translated message based on the resource key
   *
   * @param key resource key
   * @return translated message
   */
  String translateToUserSpace(String key);

  /**
   * Fetches a translated message based on the resource key and an arbitrary list of arguments. This
   * is suited to using the String.format() method.
   *
   * @param translationPackage formatted package containing the key and list of parameters
   * @return translated message
   */
  String translateToUserSpace(TranslationPackage translationPackage);

  /**
   * Translates an outgoing list from the resource representation to the user-friendly
   * representation
   *
   * @param resourceList resource-based list
   * @return user-friendly list
   */
  List<String> translateToUserSpace(List<String> resourceList);

  /**
   * Translates an incoming string from user space to the resource representation
   *
   * @param value given value
   * @return corresponding resource representation
   * @throws NonexistentTranslationException if there is no suitable translation for the message
   */
  String translateFromUserSpace(String value) throws NonexistentTranslationException;

  /**
   * Translates an incoming map of parameters from user representations to resource
   * representations.
   *
   * @param parameters user-based representations of parameters
   * @return a map with the keys translated to their resource representations
   * @throws NonexistentTranslationException if there is no suitable translation for certain keys
   */
  Map<String, String> translateFromUserSpace(Map<String, String> parameters)
      throws NonexistentTranslationException;

  /**
   * Returns the current language set in the translator
   *
   * @return current language
   */
  String getLanguage();

  /**
   * Configures the resource bundle to output the correct language
   *
   * @param language desired language (i. e. English)
   */
  void setLanguage(String language);

}
