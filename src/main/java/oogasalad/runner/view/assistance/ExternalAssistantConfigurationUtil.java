package oogasalad.runner.view.assistance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import org.apache.commons.beanutils.ConvertUtils;

/**
 * Utility class for loading and initializing external assistance configurations. Provides methods
 * for loading resource bundles and initializing instance variables based on available resource
 * bundle values.
 *
 * @author Ted Peterson
 */
public class ExternalAssistantConfigurationUtil {

    /**
     * Loads a resource bundle with the given base name for the default locale.
     *
     * @param baseName the base name of the resource bundle to be loaded
     * @return the loaded resource bundle
     */
    public static ResourceBundle loadResourceBundle(String baseName) {
        return ResourceBundle.getBundle(baseName, Locale.getDefault());
    }

    /**
     * Initializes non-final instance variables of the given object with values from the associated
     * resource bundle fields.
     *
     * @param instance the object whose instance variables are to be initialized
     * @throws IllegalAccessException if access to a field is denied
     */
    public static void initializeInstanceVariables(Object instance) throws IllegalAccessException {
        List<ResourceBundle> resourceBundleObjects = getResourceBundleFields(instance);
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!Modifier.isFinal(field.getModifiers()) && field.get(instance) == null) {
                String fieldName = field.getName().toLowerCase();
                Class<?> fieldType = field.getType();
                Stream<ResourceBundle> resourceBundleStream = resourceBundleObjects.stream();
                resourceBundleStream.filter(rb -> rb.containsKey(fieldName))
                        .map(rb -> rb.getString(fieldName))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .ifPresent(resourceValue -> {
                            Object convertedValue = ConvertUtils.convert(resourceValue, fieldType);
                            try {
                                field.set(instance, convertedValue);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
            }
            field.setAccessible(false);
        }
    }

    /**
     * Retrieves a list of resource bundle fields from the given object.
     *
     * @param instance the object from which resource bundle fields are to be retrieved
     * @return a list of resource bundle fields from the object
     */
    public static List<ResourceBundle> getResourceBundleFields(Object instance) {
        List<ResourceBundle> resourceBundleFields = new ArrayList<>();
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().equals(ResourceBundle.class)) {
                try {
                    resourceBundleFields.add((ResourceBundle) field.get(instance));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }
        return resourceBundleFields;
    }
}