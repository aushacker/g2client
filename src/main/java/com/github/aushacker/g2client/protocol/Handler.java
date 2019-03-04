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

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class Handler {

	private final Logger logger;

	/**
	 * Maps the Json field name to the appropriate Handler.
	 */
	private Map<String,Handler> handlers;

	public Handler() {
		logger = LoggerFactory.getLogger(Handler.class);

		handlers = new HashMap<>();
	}

	public void handle(JsonValue v) {
		if (v.getValueType() == ValueType.OBJECT) {
			JsonObject o = (JsonObject) v;
			for (String key : o.keySet()) {
				if (handlers.containsKey(key)) {
					JsonValue child = o.get(key);
					handlers.get(key).handle(child);
				} else {
					logger.error("No registered handler for json key: {}", key);
				}
			}
		}
	}

	public void register(String key, Handler handler) {
		handlers.put(key, handler);
	}
}
