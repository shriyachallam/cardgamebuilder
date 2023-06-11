package oogasalad.file_manager.builder_file_manager.validation.parameter_validation;

/**
 * The ParameterConfiguration record represents an internal representation that holds a parameter name and the
 * expected type of the parameter. For examples, some parameters need to store String IDs or
 * specific integers. This is unique compared to other record classes, which store the parameter
 * name and actual value.
 *
 * @author alecliu
 */
public record ParameterConfiguration(String parameterName, String expectedClassType){

}

