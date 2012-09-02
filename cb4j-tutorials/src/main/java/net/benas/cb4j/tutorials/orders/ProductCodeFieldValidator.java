package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.model.Field;

public class ProductCodeFieldValidator implements FieldValidator {

    @Override
    public boolean isValidField(Field field) {
        return field.getContent().startsWith("PC");
    }

    @Override
    public String getValidationRuleDescription() {
        return "Product code should start with 'PC'";
    }
}
