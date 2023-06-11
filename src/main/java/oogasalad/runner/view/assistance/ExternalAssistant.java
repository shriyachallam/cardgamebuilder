package oogasalad.runner.view.assistance;

/**
 * @author alecliu The ExternalAssistant interface defines behavior for an external assistant.
 * Implemented classes must be able to accept a String input and generate a response.
 */
public interface ExternalAssistant {

  /**
   * Return a String with player advice based on a player input
   *
   * @param userPrompt a player question
   * @return external advice (could be from a variety of different sources)
   */
  String provideAdvice(String userPrompt);
}
