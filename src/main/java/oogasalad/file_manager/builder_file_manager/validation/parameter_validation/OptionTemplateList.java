package oogasalad.file_manager.builder_file_manager.validation.parameter_validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.file_manager.exceptions.InvalidParameterException;
import oogasalad.file_manager.exceptions.NonexistentOptionException;
import oogasalad.file_manager.exceptions.IncorrectNumberOfParametersException;

/**
 * The OptionTemplateList class is responsible for managing one category of valid actions or
 * conditions. Its functionality to provide a list of all available options and to validate whether
 * a parameter received a value of the correct type.
 *
 * @author alecliu
 */
public class OptionTemplateList {

  private String category;
  private List<OptionTemplate> options;

  /**
   * Class constructor
   *
   * @param category type of actions/conditions being recorded
   * @param options  list of valid options
   */
  public OptionTemplateList(String category, List<OptionTemplate> options) {
    this.category = category;
    this.options = options;
  }

  /**
   * Getter method
   *
   * @return category name
   */
  public String getCategory() {
    return category;
  }

  /**
   * Getter method
   *
   * @return names of all available options
   */
  public List<String> getAllOptionNames() {
    List<String> optionNames = new ArrayList<>();
    for (OptionTemplate optionTemplate : options) {
      optionNames.add(optionTemplate.optionName());
    }
    return optionNames;
  }

  /**
   * Fetches the required parameters for a particular option
   * @param optionName optionName
   * @return list of required parameters
   * @throws NonexistentOptionException if the provided option does not exist in the category
   */
  public List<String> getParametersForOption(String optionName) throws NonexistentOptionException {
    for(OptionTemplate optionTemplate : options) {
      if(optionTemplate.optionName().equals(optionName)){
        List<String> requiredParameters = new ArrayList<>();
        for(ParameterConfiguration pC : optionTemplate.parameterList()){
          requiredParameters.add(pC.parameterName());
        }
        return requiredParameters;
      }
    }
    throw new NonexistentOptionException();
  }
  /**
   * Validates if a given option received the correct types of parameter inputs
   *
   * @param requestedOption name of the requested option
   * @param parameters      proposed values of the requested option
   * @throws InvalidParameterException            if the given value does not match the required
   *                                              type
   * @throws NonexistentOptionException           if the given option does not exist in the given
   *                                              category
   * @throws IncorrectNumberOfParametersException if the given map contained in the incorrect number
   *                                              of parameters
   */
  public void validateExpectedType(String requestedOption, Map<String, String> parameters)
      throws InvalidParameterException, NonexistentOptionException {
    for (OptionTemplate optionTemplate : options) {
      if (optionTemplate.optionName().equals(requestedOption)) {
        if (parameters.keySet().size() != optionTemplate.parameterList().size()) {
          throw new IncorrectNumberOfParametersException(
              String.valueOf(optionTemplate.parameterList().size()));
        }
        for (ParameterConfiguration pC : optionTemplate.parameterList()) {
          if (!attemptParse(pC.expectedClassType(), parameters.get(pC.parameterName()))) {
            throw new InvalidParameterException(pC.parameterName());
          }
          return;
        }
      }
    }
    throw new NonexistentOptionException();
  }

  /**
   * Attempts to interpret an arbitrary String as a given data type
   *
   * @param type  data type
   * @param value given value
   * @return whether the value matches the given type
   */
  private boolean attemptParse(String type, String value) {
    switch (type) {
      case "String" -> {
        return true;
      }
      case "Integer" -> {
        try {
          Integer.parseInt(value);
          return true;
        } catch (NumberFormatException numberFormatException) {
          return false;
        }
      }
      default -> {
        return false;
      }
    }
  }
}
