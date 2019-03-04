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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the update of a single JsonValue into the MachineState object graph.
 *
 * @author Stephen Davies
 * @since October 2019
 */
public class PropertyHandler extends Handler {

	private final Logger logger;

	/**
	 * Target object, i.e. the one that owns the property.
	 */
	private Object target;

	/**
	 * Property name.
	 */
	private String name;

	/**
	 * Setter method used to update the property value.
	 */
	private Method setter;

	/**
	 * Target property type.
	 */
	private Class<?> type;

	public PropertyHandler(Object target, String name) {
		if (target == null) {
			throw new NullPointerException("target is null");
		}

		logger = LoggerFactory.getLogger(PropertyHandler.class);

		this.target = target;
		this.name = name;
	
		introspect();
	}

	/**
	 * Use reflection to correctly cast the JsonValue type and invoke the setter.
	 */
	@Override
	public void handle(JsonValue value) {
		try {
			if ((value.getValueType() == ValueType.STRING) && (type == String.class)) {
				JsonString js = (JsonString) value;
				setter.invoke(target, js.getString());
			} else if ((value.getValueType() == ValueType.NUMBER) && (type == int.class)) {
				JsonNumber n = (JsonNumber) value;
				setter.invoke(target, n.intValue());
			}
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("Reflection error for property: {} json type: {} value: {}", name, value.getValueType(), value);
			throw new RuntimeException("Reflection error for property: " + name, e);
		}
	}

	/**
	 * Use Java Beans introspection to locate the specified set method and type
	 * for the named property. Only called once. The handle method uses cached values
	 * to reduce run time latency.
	 *
	 * @throws IllegalArgumentException invalid property name or missing setter method
	 */
	private void introspect() {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			PropertyDescriptor pd = null;
			for (int i = 0; i < pds.length; i++) {
				if (pds[i].getName().equals(name)) {
					pd = pds[i];
					break;
				}
			}

			if (pd == null) {
				// No descriptor found
				throw new IllegalArgumentException("Missing property: " + name);
			} else {
				setter = pd.getWriteMethod();
				if (setter == null) {
					throw new IllegalArgumentException("Missing setter method");
				}
				type = pd.getPropertyType();
			}
		}
		catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
}
