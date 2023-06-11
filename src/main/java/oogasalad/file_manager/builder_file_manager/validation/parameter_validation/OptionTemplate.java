package oogasalad.file_manager.builder_file_manager.validation.parameter_validation;

import java.util.List;

/**
 * The OptionTemplate record class is responsible for holding information about one valid option
 * within a category.
 *
 * @param optionName option name
 * @param classpath classpath to access
 * @param parameterList list of parameters and expected types
 *
 * @author alecliu
 */
public record OptionTemplate(String optionName, String classpath,
                             List<ParameterConfiguration> parameterList) {

}
