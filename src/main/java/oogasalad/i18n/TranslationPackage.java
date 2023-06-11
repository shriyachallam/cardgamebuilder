package oogasalad.i18n;

import java.util.List;

/**
 * The TranslationPackage record is used to compactly transport desired phrases to be translated.
 * This helps to consolidate translation to fewer layers, preventing the need for all classes in a
 * hierarchy to contain a translator object.
 *
 * @param resourceKey corresponding resource key in the properties file
 * @param arguments arguments to be formatted
 *
 * @author alecliu
 */
public record TranslationPackage(String resourceKey, String... arguments) {

  @Override
  public boolean equals(Object other) {
    if (other instanceof TranslationPackage otherPackage ) {
      return resourceKey.equals(otherPackage.resourceKey) && List.of(arguments).equals(List.of(otherPackage.arguments));
    }
    return false;
  }

}
