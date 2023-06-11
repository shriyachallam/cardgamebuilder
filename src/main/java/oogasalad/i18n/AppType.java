package oogasalad.i18n;

/**
 * The AppType enumeration describes which section of the app that a Translator should be
 * translating for. This helps the translator to determine the correct file paths to use when
 * grabbing resources.
 *
 * @author alecliu
 */
public enum AppType {
  BUILDER("builder"),
  FILE_MANAGER("file_manager"),
  RUNNER("runner");

  private final String key;

  AppType(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}