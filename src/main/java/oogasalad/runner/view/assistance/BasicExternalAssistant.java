package oogasalad.runner.view.assistance;

/**
 * @author alecliu The BasicExternalAssistant is the most basic implementation of the
 * ExternalAssistant interface. It generates a hard-coded response, regardless of the input.
 */
public class BasicExternalAssistant implements ExternalAssistant {

  /**
   * Return a String with player advice based on a player input
   *
   * @param userPrompt a player question
   * @return external advice (could be from a variety of different sources)
   */
  @Override
  public String provideAdvice(String userPrompt) {
    return "I don't know what to say, but you can do it!";
  }
}
