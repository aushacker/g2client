/*
 * Copyright 2019 Stephen Davies
 *
 * This file is part of g2client.
 *
 * g2client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * g2client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2client. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.aushacker.g2client.protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.json.JsonNumber;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map JSON integer values onto Java Enums.
 *
 * @author Stephen Davies
 * @since September 2019
 */
public class EnumPropertyHandler extends PropertyHandler {

    private final Logger logger = LoggerFactory.getLogger(EnumPropertyHandler.class);

    /**
     * Enum type to be mapped into the property.
     */
    private Class<? extends Enum<?>> enumType;

    public EnumPropertyHandler(Object target, String name, Class<? extends Enum<?>> enumType) {
        super (target, name);
        
        this.enumType = enumType;
    }

    /**
     * Use reflection to correctly cast the JsonValue type and invoke the setter.
     */
    @Override
    public void handle(JsonValue value) {
        try {
            if (value.getValueType() == ValueType.NUMBER) {
                int ordinal = ((JsonNumber) value).intValue();
                getSetter().invoke(getTarget(), lookup(ordinal));
            } else {
                logger.warn("Unable to handle property: {} json type: {} value: {}, check target type matches Json value",
                        getName(),
                        value.getValueType(),
                        value);
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Reflection error for property: {} json type: {} value: {}", getName(), value.getValueType(), value);
            throw new RuntimeException("Reflection error for property: " + getName(), e);
        }
    }

    /**
     * Locate the Enum value based on the supplied {@code ordinal}.
     */
    private Enum<?> lookup(int ordinal) {
        try {
            Method m = enumType.getMethod("values", new Class[] {});
            Enum<?>[] values = (Enum<?>[]) m.invoke(enumType, new Object[] {});
            return values[ordinal];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
