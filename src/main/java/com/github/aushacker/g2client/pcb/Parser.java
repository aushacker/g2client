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

package com.github.aushacker.g2client.pcb;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;

/**
 * Parse an Excellon drill file.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class Parser {

	private static String COMMENT = ";";
	private static String H_DELIM = "%";

	private LineNumberReader in;

	private State state;

	private Excellon result;

	public static Excellon parse(Reader in) throws IOException, ParseException {
		return new Parser(in).parse();
	}

	private Parser(Reader in) {
		this.in = new LineNumberReader(in);
		this.state = State.H_START;
		this.result = new Excellon();
	}

	public State getState() {
		return state;
	}

	/**
	 * Return the next non-blank, non-comment line in the source file.
	 */
	private String nextLine() throws IOException {
		while (true) {
			String s = in.readLine();
			s = s.trim();
			if (s.length() > 0 && !s.startsWith(COMMENT))
				return s;
		}
	}

	private Excellon parse() throws IOException, ParseException {
		processHeader();
		processBody();

		return result;
	}

	private void processBody() throws IOException, ParseException {
		String s  = nextLine();
		
		while ( ! (Excellon.M30.equals(s))) {
			Excellon.Tool tool;
			if ((tool = result.getToolContext(s)) != null) {
				s = nextLine();
			} else {
				throw new ParseException("Expecting M30 or tool selection", in.getLineNumber());
			}
			
			s = nextLine();
		}
	}

	/**
	 * Process the Excellon file header.
	 * i.e. everything between the % - %
	 */
	private void processHeader() throws IOException, ParseException {
		// Consume initial % (MUST)
		String s = nextLine();
		if (H_DELIM.equalsIgnoreCase(s)) {
			state = State.H_COMMAND;
		} else {
			throw new ParseException("Failed to find start of header", in.getLineNumber());
		}

		// Consume initial M48 (MUST)
		s = nextLine();
		if (Excellon.M48.equalsIgnoreCase(s)) {
			state = State.H_FIELD;
		} else {
			throw new ParseException("Failed to find M48 (start of header)", in.getLineNumber());
		}

		// Units, tools etc
		while ( ! (H_DELIM.equals(s = nextLine()))) {
			result.processHeader(s);
		}

		// Current line is %, header is complete
		state = State.BODY;
	}

	private enum State {
		H_START,	// Expecting %
		H_COMMAND,	// Expecting M48
		H_FIELD,	// Expecting header field or %
		BODY,		// Expecting tool or M30
		TOOL,		// Expecting next tool, point or M30
		COMPLETE;
	}
}
